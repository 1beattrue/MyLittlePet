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

    private val _screenState = MutableStateFlow<MainScreenState>(MainScreenState.Initial)
    val screenState = _screenState.asStateFlow()


    init {
        if (authRepository.currentUser == null) {
            _screenState.value = MainScreenState.AuthFlow
        } else {
            _screenState.value = MainScreenState.MainFlow(isBottomBarVisible = true)
        }
    }

    fun finishAuth() {
        _screenState.value = MainScreenState.MainFlow(isBottomBarVisible = true)
    }
}