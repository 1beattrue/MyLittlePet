package edu.mirea.onebeattrue.mylittlepet.domain.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthScreenState>

    suspend fun signInWithCredential(
        code: String
    ): Flow<AuthScreenState>

    suspend fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthScreenState>

    fun signOut()

}