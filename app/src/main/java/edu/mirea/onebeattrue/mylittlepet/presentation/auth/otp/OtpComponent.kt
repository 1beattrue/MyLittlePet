package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

import kotlinx.coroutines.flow.StateFlow

interface OtpComponent {
    val model: StateFlow<OtpStore.State>

    fun changeOtp(otp: String)
    fun onConfirmOtp(otp: String)
    fun onClickResend()
    fun onClickBack()
}