package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class AuthState {
    object Initial : AuthState()
    object Success : AuthState()
    data class Failure(val exception: Exception) : AuthState()
    object Loading : AuthState()
}