package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.ConfirmPhoneScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfirmPhoneViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _confirmPhoneScreenState = MutableStateFlow<ConfirmPhoneScreenState>(ConfirmPhoneScreenState.Initial)
    val confirmPhoneScreenState = _confirmPhoneScreenState.asStateFlow()

    fun signUpWithCredential(
        code: String
    ) {
        viewModelScope.launch {
            repository.signInWithCredential(code).collect {
                _confirmPhoneScreenState.emit(it)
            }
        }
    }
}