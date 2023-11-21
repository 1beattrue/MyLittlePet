package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    fun signOut() {
        repository.signOut()
    }
}