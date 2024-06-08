package edu.mirea.onebeattrue.mylittlepet.data.repository

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.data.mapper.AuthExceptionMapper
import edu.mirea.onebeattrue.mylittlepet.data.mapper.UserMapper
import edu.mirea.onebeattrue.mylittlepet.data.remote.api.PetService
import edu.mirea.onebeattrue.mylittlepet.data.remote.api.UserService
import edu.mirea.onebeattrue.mylittlepet.di.ApplicationScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Реализация AuthRepository
 */
@ApplicationScope
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authExceptionMapper: AuthExceptionMapper,
    private val userService: UserService,
    private val petService: PetService,
    private val userMapper: UserMapper
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    private lateinit var verificationCode: String

    private var lastPhone: String? = null
    private var lastActivity: Activity? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    override val loggedIn = flow {
        while (true) {
            if (currentUser == null) emit(false)
            else emit(true)
            delay(3000)
        }
    }

    override suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthState> = callbackFlow {

        lastPhone = phoneNumber
        lastActivity = activity

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        Log.d("AuthRepositoryImpl", "signInWithCredential SUCCESS")
                        //trySend(AuthState.Success)
                    }
                    .addOnFailureListener {
                        Log.d("AuthRepositoryImpl", "signInWithCredential FAILURE")
                        //trySend(AuthState.Failure(authExceptionMapper.mapFirebaseExceptionToAuthException(it)))
                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("AuthRepositoryImpl", "$e")

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
                trySend(
                    AuthState.Failure(
                        authExceptionMapper.mapFirebaseExceptionToAuthException(
                            it
                        )
                    )
                )
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