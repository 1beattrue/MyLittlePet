package edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.auth

sealed class AuthScreenState {
    object Initial : AuthScreenState()
    data class EnterPhone(val phoneNumber: String) : AuthScreenState()
    data class ConfirmPhone(val code: Int) : AuthScreenState()
    object Failure : AuthScreenState()
}