package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class EnterPhoneScreenState {
    object Initial : EnterPhoneScreenState()
    object Success : EnterPhoneScreenState()
    data class Failure(val exception: Exception) : EnterPhoneScreenState()
    object Loading : EnterPhoneScreenState()
}

class InvalidPhoneNumberException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid phone number"
    }
}