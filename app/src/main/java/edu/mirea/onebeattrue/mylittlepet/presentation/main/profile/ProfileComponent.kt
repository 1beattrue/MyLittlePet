package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val model: StateFlow<ProfileStore.State>

    fun signOut()

    fun changeTheme(isDarkTheme: Boolean)
    fun changeLanguage(isEnglishLanguage: Boolean)
    fun sendEmail()
    fun openBottomSheet()
    fun closeBottomSheet()
    fun changeUseSystemTheme(useSystemTheme: Boolean)
}