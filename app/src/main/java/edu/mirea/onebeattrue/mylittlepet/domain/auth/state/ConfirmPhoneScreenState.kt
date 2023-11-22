package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class ConfirmPhoneScreenState {
    object Initial : ConfirmPhoneScreenState()
    object Success : ConfirmPhoneScreenState()
    data class Failure(val exception: Exception) : ConfirmPhoneScreenState()
    object Loading : ConfirmPhoneScreenState()
}

class InvalidVerificationCodeException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid verification code"
    }
}