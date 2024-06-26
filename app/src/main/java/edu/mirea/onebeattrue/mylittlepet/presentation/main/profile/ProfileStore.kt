package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import android.app.Application
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.datastore.preferences.core.edit
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignOutUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.State
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.DataStoreUtils
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.LocaleUtils
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.UiUtils
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.dataStore
import edu.mirea.onebeattrue.mylittlepet.ui.theme.IS_ENGLISH_MODE_KEY
import edu.mirea.onebeattrue.mylittlepet.ui.theme.IS_NIGHT_MODE_KEY
import edu.mirea.onebeattrue.mylittlepet.ui.theme.SUPPORT_EMAIL
import edu.mirea.onebeattrue.mylittlepet.ui.theme.USE_SYSTEM_THEME
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object SignOut : Intent

        data class ChangeTheme(val isDarkTheme: Boolean) : Intent
        data class ChangeUsingSystemTheme(val useSystemTheme: Boolean) : Intent

        data class ChangeLanguage(val isEnglishLanguage: Boolean) : Intent

        data object SendEmail : Intent
        data object OpenBottomSheet : Intent
        data object CloseBottomSheet : Intent

        data object OpenDialog : Intent
        data object CloseDialog : Intent
    }

    data class State(
        val isDarkTheme: Boolean?,
        val useSystemTheme: Boolean,
        val isEnglishLanguage: Boolean,
        val bottomSheetState: Boolean,
        val isLogOutDialogOpen: Boolean
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
            name = STORE_NAME,
            initialState = State(
                useSystemTheme = DataStoreUtils
                    .getLastSavedBoolean(application, USE_SYSTEM_THEME)
                    ?: true,
                isDarkTheme = DataStoreUtils
                    .getLastSavedBoolean(application, IS_NIGHT_MODE_KEY)
                    ?: UiUtils.isSystemInDarkTheme(application),
                isEnglishLanguage = DataStoreUtils
                    .getLastSavedBoolean(application, IS_ENGLISH_MODE_KEY)
                    ?: LocaleUtils.isEnglishLanguage(),
                bottomSheetState = false,
                isLogOutDialogOpen = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeTheme(val isDarkTheme: Boolean) : Msg
        data class ChangeUsingSystemTheme(val useSystemTheme: Boolean) : Msg

        data class ChangeLanguage(val isEnglishLanguage: Boolean) : Msg

        data class BottomSheetState(val bottomSheetState: Boolean) : Msg

        data class LogOutDialogState(val isOpen: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.SignOut -> {
                    scope.launch {
                        signOutUseCase()
                        publish(Label.SignOut)
                    }
                }

                Intent.SendEmail -> {
                    val email = SUPPORT_EMAIL
                    val emailIntent =
                        android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$email")
                            putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.email_subject)
                            setFlags(FLAG_ACTIVITY_NEW_TASK)
                        }
                    application.startActivity(emailIntent)
                }

                Intent.OpenBottomSheet -> {
                    dispatch(Msg.BottomSheetState(true))
                }

                Intent.CloseBottomSheet -> {
                    dispatch(Msg.BottomSheetState(false))
                }

                is Intent.ChangeTheme -> {
                    scope.launch {
                        application.dataStore.edit { preferences ->
                            preferences[IS_NIGHT_MODE_KEY] = intent.isDarkTheme
                        }
                    }
                    dispatch(Msg.ChangeTheme(intent.isDarkTheme))
                }

                is Intent.ChangeUsingSystemTheme -> {
                    scope.launch {
                        val usingSystemTheme = intent.useSystemTheme
                        application.dataStore.edit { preferences ->
                            preferences[USE_SYSTEM_THEME] = usingSystemTheme
                        }
                        dispatch(Msg.ChangeUsingSystemTheme(usingSystemTheme))
                    }
                }

                is Intent.ChangeLanguage -> {
                    scope.launch {
                        application.dataStore.edit { preferences ->
                            preferences[IS_ENGLISH_MODE_KEY] = intent.isEnglishLanguage
                        }
                    }
                    dispatch(Msg.ChangeLanguage(intent.isEnglishLanguage))
                }

                Intent.OpenDialog -> {
                    dispatch(Msg.LogOutDialogState(true))
                }

                Intent.CloseDialog -> {
                    dispatch(Msg.LogOutDialogState(false))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.BottomSheetState -> copy(bottomSheetState = msg.bottomSheetState)
                is Msg.ChangeTheme -> copy(isDarkTheme = msg.isDarkTheme)
                is Msg.ChangeUsingSystemTheme -> copy(useSystemTheme = msg.useSystemTheme)
                is Msg.ChangeLanguage -> copy(isEnglishLanguage = msg.isEnglishLanguage)
                is Msg.LogOutDialogState -> copy(isLogOutDialogOpen = msg.isOpen)
            }
    }

    companion object {
        const val STORE_NAME = "ProfileStore"
    }
}
