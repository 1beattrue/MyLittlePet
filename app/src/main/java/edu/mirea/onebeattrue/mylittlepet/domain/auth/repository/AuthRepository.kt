package edu.mirea.onebeattrue.mylittlepet.domain.auth.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthScreenState
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

    suspend fun resendVerificationCode(): Flow<AuthScreenState>

    fun signOut()

}