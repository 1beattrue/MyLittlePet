package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import javax.inject.Inject

class EnterPhoneViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ) = repository.createUserWithPhone(phoneNumber, activity)
}