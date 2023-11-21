package edu.mirea.onebeattrue.mylittlepet.domain.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthState>

    suspend fun signInWithCredential(
        code: String
    ): Flow<AuthState>
}