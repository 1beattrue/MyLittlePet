package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.EnterPhoneScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class EnterPhoneViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _enterPhoneScreenState =
        MutableStateFlow<EnterPhoneScreenState>(EnterPhoneScreenState.Initial)
    val enterPhoneScreenState = _enterPhoneScreenState.asStateFlow()

    fun createUserWithPhone(
        phoneNumber: String,
        activity: Activity
    ) {
        Log.d("tag", phoneNumber)
        viewModelScope.launch {
            repository.createUserWithPhone(phoneNumber, activity)
                .onEach {
                    _enterPhoneScreenState.value = it
                }
                .launchIn(viewModelScope)
        }
    }
}