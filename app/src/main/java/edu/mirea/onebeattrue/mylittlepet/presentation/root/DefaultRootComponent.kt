package edu.mirea.onebeattrue.mylittlepet.presentation.root

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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

    @Assisted("context") private val context: Context,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private var isDarkTheme: Boolean =
        (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            context.dataStore.data
                .collect {
                    onThemeChanged(it[IS_NIGHT_MODE_KEY] ?: isDarkTheme)
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
                onSignOutClicked = { navigation.replaceAll(Config.Auth) }
            )
            RootComponent.Child.Main(component)
        }
    }

    override fun onThemeChanged(isDarkTheme: Boolean) {
        store.accept(RootStore.Intent.ChangeTheme(isDarkTheme))
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
            @Assisted("context") context: Context,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}
