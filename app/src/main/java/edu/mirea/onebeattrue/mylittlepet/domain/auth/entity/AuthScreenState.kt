package edu.mirea.onebeattrue.mylittlepet.domain.auth.entity

sealed class AuthScreenState {
    object Initial : AuthScreenState()
    object Success : AuthScreenState()
    data class Failure(val exception: Exception) : AuthScreenState()
    object Loading : AuthScreenState()
    object CodeSent : AuthScreenState()
}

class InvalidPhoneNumberException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid phone number"
    }
}

class InvalidVerificationCodeException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid verification code"
    }
}

class TimeoutVerificationCodeException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Timeout has not expired"
    }
}