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
import kotlinx.coroutines.flow.flow
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
    private var lastRequestTime = LAST_REQUEST_TIME_INITIAL

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
                //lastRequestTime = System.currentTimeMillis()
                forceResendingToken = token
                trySend(AuthState.Success)
            }
        }

        //if (codeCanBeSent()) {
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
//        } else {
//            val currentRequestTime = System.currentTimeMillis()
//            trySend(
//                AuthScreenState.Failure(
//                    TimeoutVerificationCodeException(
//                        String.format(
//                            activity.getString(R.string.resend_code_exception),
//                            (TIMEOUT_MILLIS - (currentRequestTime - lastRequestTime)).toSeconds()
//                        )
//                    )
//                )
//            )
//        }

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

    private fun Long.toSeconds() = this / MILLIS_IN_SECOND + 1

    private fun codeCanBeSent(): Boolean {
        return lastRequestTime == LAST_REQUEST_TIME_INITIAL ||
                System.currentTimeMillis() - lastRequestTime > TIMEOUT_MILLIS
    }

    companion object {
        private const val TIMEOUT = 15L
        private const val RESERVE_TIMEOUT = 5L

        private const val MILLIS_IN_SECOND = 1000
        private const val TIMEOUT_MILLIS = (TIMEOUT + RESERVE_TIMEOUT) * MILLIS_IN_SECOND
        private const val LAST_REQUEST_TIME_INITIAL = -228L
    }
}