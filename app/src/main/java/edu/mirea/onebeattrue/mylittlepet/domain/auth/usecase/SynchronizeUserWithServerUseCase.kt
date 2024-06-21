package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SynchronizeUserWithServerUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.synchronizeWithServer()
    }
}