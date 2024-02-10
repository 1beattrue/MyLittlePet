package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        code: String
    ): Flow<AuthScreenState> {
        return repository.signInWithCredential(code)
    }
}