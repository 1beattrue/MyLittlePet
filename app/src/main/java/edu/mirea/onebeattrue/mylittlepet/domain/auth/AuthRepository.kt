package edu.mirea.onebeattrue.mylittlepet.domain.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.ConfirmPhoneScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.EnterPhoneScreenState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<EnterPhoneScreenState>

    suspend fun signInWithCredential(
        code: String
    ): Flow<ConfirmPhoneScreenState>
}