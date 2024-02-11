package edu.mirea.onebeattrue.mylittlepet.presentation.main

import androidx.lifecycle.ViewModel
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignOutUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.main.entity.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.AuthFlow())
    val mainScreenState = _mainScreenState.asStateFlow()

    private val currentUser
        get() = authRepository.currentUser

    init {
        if (currentUser != null) finishAuth()
    }

    fun finishAuth() {
        _mainScreenState.value = MainScreenState.MainFlow()
    }

    fun signOut() {
        signOutUseCase()
        _mainScreenState.value = MainScreenState.AuthFlow()
    }
}