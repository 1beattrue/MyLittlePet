package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.main

import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _isBottomBarVisible = MutableStateFlow(false)
    val isBottomBarVisible = _isBottomBarVisible.asStateFlow()

    private val _isAuthFinished = MutableStateFlow(false)
    val isAuthFinished = _isAuthFinished.asStateFlow()


    init {
        if (authRepository.currentUser != null) {
            _isAuthFinished.value = true
        }
    }

    fun makeBottomBarVisible() {
        _isBottomBarVisible.value = true
    }

    fun makeBottomBarInvisible() {
        _isBottomBarVisible.value = true
    }

    fun finishAuth() {
        _isAuthFinished.value = true
    }
}