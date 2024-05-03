package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val model: StateFlow<ProfileStore.State>

    fun signOut()
}