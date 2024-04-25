package edu.mirea.onebeattrue.mylittlepet.data.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.di.ApplicationScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ApplicationScope
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authExceptionMapper: AuthExceptionMapper
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    private lateinit var verificationCode: String

    private var lastPhone: String? = null
    private var lastActivity: Activity? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    override suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthState> = callbackFlow {

        lastPhone = phoneNumber
        lastActivity = activity

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                trySend(AuthState.Success)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(AuthState.Failure(authExceptionMapper.mapFirebaseExceptionToAuthException(e)))
            }

            override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(code, token)
                verificationCode = code
                forceResendingToken = token
                trySend(AuthState.Success)
            }
        }

        firebaseAuth.useAppLanguage()
        val prefix = activity.getString(R.string.phone_number_prefix)
        val options = if (forceResendingToken == null) {
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(prefix + phoneNumber)
                .setTimeout(TIMEOUT, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
        } else {
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(prefix + phoneNumber)
                .setTimeout(TIMEOUT, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .setForceResendingToken(forceResendingToken!!)
                .build()
        }
        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose {
            close()
        }
    }

    override suspend fun signInWithCredential(
        code: String
    ): Flow<AuthState> = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(verificationCode, code)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                trySend(AuthState.Success)
            }
            .addOnFailureListener {
                trySend(AuthState.Failure(authExceptionMapper.mapFirebaseExceptionToAuthException(it)))
            }

        awaitClose {
            close()
        }
    }

    override suspend fun resendVerificationCode(): Flow<AuthState> =
        createUserWithPhone(
            lastPhone!!,
            lastActivity!!
        )

    override fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        private const val TIMEOUT = 60L
    }
}