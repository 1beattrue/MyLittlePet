package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class EnterPhoneScreenState {
    object Initial : EnterPhoneScreenState()
    object Success : EnterPhoneScreenState()
    data class Failure(val exception: Exception) : EnterPhoneScreenState()
    object Loading : EnterPhoneScreenState()
}