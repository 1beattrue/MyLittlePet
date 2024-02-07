package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.AuthScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.InvalidPhoneNumberException
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.InvalidVerificationCodeException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _screenState =
        MutableStateFlow<AuthScreenState>(AuthScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ) {
        viewModelScope.launch {
            if (!isValidPhoneNumber(phoneNumber)) {
                _screenState.value =
                    AuthScreenState.Failure(InvalidPhoneNumberException())
            } else {
                repository.createUserWithPhone(phoneNumber, activity)
                    .collect {
                        _screenState.value = it
                    }
            }
        }
    }

    fun signUpWithCredential(
        code: String
    ) {
        viewModelScope.launch {
            if (!isValidConfirmationCode(code)) {
                _screenState.value =
                    AuthScreenState.Failure(InvalidVerificationCodeException())
            } else {
                repository.signInWithCredential(code)
                    .collect {
                        _screenState.value = it
                    }
            }
        }
    }

    fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ) {
        viewModelScope.launch {
            repository.resendVerificationCode(
                phoneNumber = phoneNumber,
                activity = activity
            ).collect {
                _screenState.value = it
            }
        }
    }

    fun changePhoneNumber() {
        viewModelScope.launch {
            _screenState.value = AuthScreenState.Initial
        }
    }

    fun parseConfirmationCode(messageBody: String): String? {
        val regex = "\\b\\d{6}\\b".toRegex()
        val matchResult = regex.find(messageBody)
        return matchResult?.value
    }

    private fun isValidConfirmationCode(code: String): Boolean {
        return code.trim().length == 6
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.trim().length == 10
    }
}