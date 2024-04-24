package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        code: String
    ): Flow<AuthState> {
        return repository.signInWithCredential(code)
    }
}