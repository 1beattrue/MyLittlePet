package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import android.app.Activity
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResendVerificationCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Flow<AuthScreenState> {
        return repository.resendVerificationCode()
    }
}