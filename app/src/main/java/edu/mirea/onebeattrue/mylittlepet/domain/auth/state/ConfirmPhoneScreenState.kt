package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class ConfirmPhoneScreenState {
    object Initial : ConfirmPhoneScreenState()
    object Success : ConfirmPhoneScreenState()
    data class Failure(val exception: Exception) : ConfirmPhoneScreenState()
    object Loading : ConfirmPhoneScreenState()
}
