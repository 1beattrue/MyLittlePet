package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.settings

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.settings.SettingsStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.settings.SettingsStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.settings.SettingsStore.State
import javax.inject.Inject

interface SettingsStore : Store<Intent, State, Label> {
    sealed interface Intent {

    }

    data class State(
        val language: Boolean,
        val theme: Boolean
    )

    sealed interface Label {

    }
}

class SettingsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
) {

}