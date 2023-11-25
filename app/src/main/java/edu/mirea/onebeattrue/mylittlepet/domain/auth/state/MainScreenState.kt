package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class MainScreenState {
    data class AuthFlow(
        val isBottomBarVisible: Boolean = false
    ) : MainScreenState()
    data class MainFlow(
        val isBottomBarVisible: Boolean = true
    ) : MainScreenState()
}
