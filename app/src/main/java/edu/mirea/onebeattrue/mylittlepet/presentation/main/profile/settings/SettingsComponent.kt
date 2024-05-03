package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.settings

import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {
    val model: StateFlow<SettingsStore.State>
}