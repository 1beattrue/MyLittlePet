package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels

import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository

class ConfirmPhoneViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    fun signUpWithCredential(
        code: String
    ) = repository.signInWithCredential(code)
}