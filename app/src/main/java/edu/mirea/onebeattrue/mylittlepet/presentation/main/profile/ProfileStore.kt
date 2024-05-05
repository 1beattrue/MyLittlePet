package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignOutUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.dataStore
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.State
import edu.mirea.onebeattrue.mylittlepet.ui.theme.IS_NIGHT_MODE_KEY
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object SignOut : Intent
        data class ChangeTheme(val isDarkTheme: Boolean) : Intent
    }

    data class State(
        val isDarkTheme: Boolean
    )

    sealed interface Label {
        data object SignOut : Label
    }
}

class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val signOutUseCase: SignOutUseCase,
    private val application: Application
) {

    fun create(): ProfileStore =
        object : ProfileStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProfileStore",
            initialState = State(
                isDarkTheme =
                (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeTheme(val isDarkTheme: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.SignOut -> {
                    signOutUseCase()
                    publish(Label.SignOut)
                }

                is Intent.ChangeTheme -> {
                    scope.launch {
                        application.dataStore.edit { preferences ->
                            preferences[IS_NIGHT_MODE_KEY] = intent.isDarkTheme
                        }
                    }
                    dispatch(Msg.ChangeTheme(intent.isDarkTheme))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when(msg) {
                is Msg.ChangeTheme -> copy(isDarkTheme = msg.isDarkTheme)
            }
    }
}
