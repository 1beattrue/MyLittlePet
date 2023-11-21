package edu.mirea.onebeattrue.mylittlepet.data.auth

import android.app.Activity
import android.util.Log
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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ApplicationScope
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override val currentUser: FirebaseUser? = firebaseAuth.currentUser
    private lateinit var verificationCode: String

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
                Log.d("AuthRepositoryImpl", e.toString())
            }

            override fun onCodeSent(code: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(code, token)
                trySend(AuthScreenState.Success)
                verificationCode = code
            }
        }

        firebaseAuth.useAppLanguage()
        val prefix = activity.getString(R.string.phone_number_prefix)

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(prefix + phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

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

}