package edu.mirea.onebeattrue.mylittlepet.domain.auth

import android.app.Activity
import edu.mirea.onebeattrue.mylittlepet.domain.auth.utils.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<AuthState<String>>

    fun signInWithCredential(
        code: String
    ): Flow<AuthState<String>>
}