package edu.mirea.onebeattrue.mylittlepet.domain.auth.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    val loggedIn: Flow<Boolean>

    suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthState>

    suspend fun signInWithCredential(
        code: String
    ): Flow<AuthState>

    suspend fun resendVerificationCode(): Flow<AuthState>

    fun signOut()

    suspend fun synchronizeWithServer()
}