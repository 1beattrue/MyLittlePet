package edu.mirea.onebeattrue.mylittlepet.domain.auth.state

sealed class MainScreenState {
    object Initial : MainScreenState()
    object AuthFlow : MainScreenState()
    data class MainFlow(
        val isBottomBarVisible: Boolean
    ) : MainScreenState()
}
