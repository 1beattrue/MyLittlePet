package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    operator fun invoke() {
        repository.signOut()
    }
}