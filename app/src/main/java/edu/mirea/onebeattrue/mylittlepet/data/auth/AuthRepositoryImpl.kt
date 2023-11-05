package edu.mirea.onebeattrue.mylittlepet.data.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.utils.AuthState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private lateinit var verificationCode: String

    override fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthState<String>> = callbackFlow {
        trySend(AuthState.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(AuthState.Failure(e))
            }

            override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationCode, token)
                trySend(AuthState.Success("Verification code has been sent"))
                verificationCode = code
            }
        }

        firebaseAuth.useAppLanguage()
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose {
            close()
        }
    }

    override fun signInWithCredential(
        code: String
    ): Flow<AuthState<String>> = callbackFlow {
        trySend(AuthState.Loading)

        val credential = PhoneAuthProvider.getCredential(verificationCode, code)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(AuthState.Success("confirmed"))
                }
            }
            .addOnFailureListener {
                trySend(AuthState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

}