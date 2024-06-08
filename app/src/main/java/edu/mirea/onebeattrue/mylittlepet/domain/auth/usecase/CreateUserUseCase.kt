package edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase

import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        user: FirebaseUser
    ) {
        repository.createUser(user)
    }
}