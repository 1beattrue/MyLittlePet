package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class AuthScreenState {
    object Initial : AuthScreenState()
    object Success : AuthScreenState()
    data class Failure(val exception: Exception) : AuthScreenState()
    object Loading : AuthScreenState()
}