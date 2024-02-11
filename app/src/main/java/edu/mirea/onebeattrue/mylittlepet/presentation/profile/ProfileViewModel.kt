package edu.mirea.onebeattrue.mylittlepet.presentation.profile

import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

}