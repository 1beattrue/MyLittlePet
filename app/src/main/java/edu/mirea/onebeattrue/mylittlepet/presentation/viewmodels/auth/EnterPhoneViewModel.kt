package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth

import android.app.Activity
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterPhoneViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState>
        get() = _authState

    fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ) {
        Log.d("tag", phoneNumber)
        viewModelScope.launch {
            repository.createUserWithPhone(phoneNumber, activity).collect {
                _authState.emit(it)
            }
        }
    }
}