package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.CreateUserWithPhoneUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.ResendVerificationCodeUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignInWithCredentialUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val createUserWithPhoneUseCase: CreateUserWithPhoneUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val resendVerificationCodeUseCase: ResendVerificationCodeUseCase,
) : ViewModel() {
    // private val _screenState = MutableStateFlow<AuthScreenState>(AuthScreenState.Initial)
    //val screenState = _screenState.asStateFlow()

    val isLoggedIn
        get() = repository.currentUser != null

    fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ) {
        viewModelScope.launch {
            if (!isValidPhoneNumber(phoneNumber)) {
                //_screenState.value = AuthScreenState.Failure(InvalidPhoneNumberException())
            } else {
                createUserWithPhoneUseCase(
                    phoneNumber = phoneNumber,
                    activity = activity
                )
//                    .collect {
//                        _screenState.value = it
//                    }
            }
        }
    }

    fun signUpWithCredential(
        code: String
    ) {
        viewModelScope.launch {
            if (!isValidConfirmationCode(code)) {
//                _screenState.value =
//                    AuthScreenState.Failure(InvalidVerificationCodeException())
            } else {
                signInWithCredentialUseCase(
                    code = code
                )
//                    .collect {
//                        _screenState.value = it
//                    }
            }
        }
    }

    fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ) {
        viewModelScope.launch {
//            resendVerificationCodeUseCase(
//                phoneNumber = phoneNumber,
//                activity = activity
//            ).collect {
//                _screenState.value = it
//            }
        }
    }

    fun changePhoneNumber() {
        viewModelScope.launch {
            //_screenState.value = AuthScreenState.Initial
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