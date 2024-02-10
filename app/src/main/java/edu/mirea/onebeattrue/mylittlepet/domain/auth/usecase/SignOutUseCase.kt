package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() {
        repository.signOut()
    }
}