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
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.AuthScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.TimeoutVerificationCodeException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ApplicationScope
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    private lateinit var verificationCode: String
    private var lastRequestTime = LAST_REQUEST_TIME_INITIAL

    override suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthScreenState> = callbackFlow {
        trySend(AuthScreenState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                trySend(AuthScreenState.Success)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(AuthScreenState.Failure(e))
            }

            override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(code, token)
                trySend(AuthScreenState.CodeSent)
                verificationCode = code
                lastRequestTime = System.currentTimeMillis()
            }
        }

        if (codeCanBeSent()) {
            firebaseAuth.useAppLanguage()
            val prefix = activity.getString(R.string.phone_number_prefix)
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(prefix + phoneNumber)
                .setTimeout(TIMEOUT, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            val currentRequestTime = System.currentTimeMillis()
            trySend(
                AuthScreenState.Failure(
                    TimeoutVerificationCodeException(
                        String.format(
                            activity.getString(R.string.resend_code_exception),
                            (TIMEOUT_MILLIS - (currentRequestTime - lastRequestTime)).toSeconds()
                        )
                    )
                )
            )
        }

        awaitClose {
            close()
        }
    }

    override suspend fun signInWithCredential(
        code: String
    ): Flow<AuthScreenState> = callbackFlow {
        trySend(AuthScreenState.Loading)

        val credential = PhoneAuthProvider.getCredential(verificationCode, code)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(AuthScreenState.Success)
                }
            }
            .addOnFailureListener {
                trySend(AuthScreenState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override suspend fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthScreenState> {
        return createUserWithPhone(
            phoneNumber = phoneNumber,
            activity = activity
        )
    }

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