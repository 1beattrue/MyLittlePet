package edu.mirea.onebeattrue.mylittlepet.presentation.root

import android.app.Application
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.DefaultAuthComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.UiUtils
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.dataStore
import edu.mirea.onebeattrue.mylittlepet.presentation.main.DefaultMainComponent
import edu.mirea.onebeattrue.mylittlepet.ui.theme.IS_NIGHT_MODE_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val storeFactory: RootStoreFactory,

    private val authComponentFactory: DefaultAuthComponent.Factory,
    private val mainComponentFactory: DefaultMainComponent.Factory,
    private val authRepository: AuthRepository,

    private val application: Application,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val darkTheme: Boolean
        get() = UiUtils.isSystemInDarkTheme(application)
//    private var isEnglishLanguage: Boolean =
//        application.resources.configuration.locales.toLanguageTags() == Language.EN.value

    val store = instanceKeeper.getStore {
        storeFactory.create(isDarkTheme = darkTheme)
    }

    init {
        componentScope.launch {
            application.dataStore.data
                .collect { preferences ->
                    preferences[IS_NIGHT_MODE_KEY].let {
                        onThemeChanged(it ?: darkTheme)
                        UiUtils.isAppInDarkTheme = darkTheme
                    }

                    //onLanguageChanged(it[IS_ENGLISH_MODE_KEY] ?: isEnglishLanguage)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<RootStore.State>
        get() = store.stateFlow

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = if (authRepository.currentUser == null) Config.Auth else Config.Main,
        handleBackButton = true,
        childFactory = ::child,
        key = "root"
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        Config.Auth -> {
            val component = authComponentFactory.create(
                componentContext = componentContext,
                onLoggedIn = { navigation.replaceAll(Config.Main) }
            )
            RootComponent.Child.Auth(component)
        }

        Config.Main -> {
            val component = mainComponentFactory.create(
                componentContext = componentContext,
                onSignOutClicked = { navigation.replaceAll(Config.Auth) },
            )
            RootComponent.Child.Main(component)
        }
    }

    private fun onThemeChanged(isDarkTheme: Boolean) {
        store.accept(RootStore.Intent.ChangeTheme(isDarkTheme))
    }

    private fun onLanguageChanged(isEnglishLanguage: Boolean) {
        store.accept(RootStore.Intent.ChangeLanguage(isEnglishLanguage))
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Auth : Config

        @Serializable
        data object Main : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}
