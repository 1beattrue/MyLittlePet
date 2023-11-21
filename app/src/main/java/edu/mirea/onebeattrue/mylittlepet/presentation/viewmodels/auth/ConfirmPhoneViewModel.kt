package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.AuthScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfirmPhoneViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _authScreenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Initial)
    val authScreenState = _authScreenState.asStateFlow()

    fun signUpWithCredential(
        code: String
    ) {
        viewModelScope.launch {
            repository.signInWithCredential(code).collect {
                _authScreenState.emit(it)
            }
        }
    }
}