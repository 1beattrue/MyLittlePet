package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import android.app.Application
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.res.Configuration
import android.net.Uri
import androidx.datastore.preferences.core.edit
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignOutUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.Language
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.dataStore
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.State
import edu.mirea.onebeattrue.mylittlepet.ui.theme.SUPPORT_EMAIL
import edu.mirea.onebeattrue.mylittlepet.ui.theme.IS_ENGLISH_MODE_KEY
import edu.mirea.onebeattrue.mylittlepet.ui.theme.IS_NIGHT_MODE_KEY
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object SignOut : Intent
        data class ChangeTheme(val isDarkTheme: Boolean) : Intent
        data class ChangeLanguage(val isEnglishLanguage: Boolean) : Intent
        data object SendEmail : Intent
        data object OpenBottomSheet: Intent
        data object CloseBottomSheet: Intent
    }

    data class State(
        val isDarkTheme: Boolean,
        val isEnglishLanguage: Boolean,
        val bottomSheetState: Boolean
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
                isDarkTheme = (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES,
                isEnglishLanguage = application.resources.configuration.locales.toLanguageTags() == Language.EN.value,
                bottomSheetState = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeTheme(val isDarkTheme: Boolean) : Msg
        data class ChangeLanguage(val isEnglishLanguage: Boolean) : Msg
        data object SendEmail : Msg
        data class BottomSheetState(val bottomSheetState: Boolean) : Msg
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

                is Intent.ChangeLanguage -> {
                    scope.launch {
                        application.dataStore.edit { preferences ->
                            preferences[IS_ENGLISH_MODE_KEY] = intent.isEnglishLanguage
                        }
                    }
                    dispatch(Msg.ChangeLanguage(intent.isEnglishLanguage))
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
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeTheme -> copy(isDarkTheme = msg.isDarkTheme)
                is Msg.ChangeLanguage -> copy(isEnglishLanguage = msg.isEnglishLanguage)
                Msg.SendEmail -> TODO()
                is Msg.BottomSheetState -> copy(bottomSheetState = msg.bottomSheetState)
            }
    }
}
