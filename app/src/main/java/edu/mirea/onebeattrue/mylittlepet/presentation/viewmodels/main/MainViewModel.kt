package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.main

import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.Initial)
    val mainScreenState = _mainScreenState.asStateFlow()

    private val currentUser = authRepository.currentUser

    init {
        if (currentUser == null) {
            _mainScreenState.value = MainScreenState.AuthFlow(

            )
        } else {
            _mainScreenState.value = MainScreenState.MainFlow(
                isBottomBarVisible = true
            )
        }
    }

    fun finishAuth() {
        _mainScreenState.value = MainScreenState.MainFlow(
            isBottomBarVisible = true
        )
    }

    fun signOut() {
        authRepository.signOut()
        _mainScreenState.value = MainScreenState.AuthFlow(
        )
    }
}