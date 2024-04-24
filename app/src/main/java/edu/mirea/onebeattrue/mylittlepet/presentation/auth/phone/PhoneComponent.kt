package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

interface PhoneComponent {
    val model: StateFlow<PhoneStore.State>

    fun changePhone(phone: String)
    fun onConfirmPhone(phone: String, activity: Activity)
}