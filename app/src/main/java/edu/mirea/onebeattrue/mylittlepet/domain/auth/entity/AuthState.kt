package edu.mirea.onebeattrue.mylittlepet.domain.auth.entity

sealed class AuthState {
    data object Success : AuthState()
    data class Failure(val exception: Exception) : AuthState()
}