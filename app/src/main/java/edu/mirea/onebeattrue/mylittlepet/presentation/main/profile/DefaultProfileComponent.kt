package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultProfileComponent @AssistedInject constructor(
    private val storeFactory: ProfileStoreFactory,
    @Assisted("onChangedBottomMenuVisibility") private val onChangedBottomMenuVisibility: (Boolean) -> Unit,
    @Assisted("isDarkTheme") private val isDarkTheme: Boolean,
    @Assisted("onSignOutClicked") private val onSignOutClicked: () -> Unit,
    @Assisted("onChangedThemeClicked") private val onChangedThemeClicked: (Boolean) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : ProfileComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(isDarkTheme) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    ProfileStore.Label.SignOut -> {
                        onSignOutClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ProfileStore.State>
        get() = store.stateFlow

    override fun signOut() {
        store.accept(ProfileStore.Intent.SignOut)
    }

    override fun changeTheme(isDarkTheme: Boolean) {
        onChangedThemeClicked(isDarkTheme)
        store.accept(ProfileStore.Intent.ChangeTheme(isDarkTheme))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onChangedBottomMenuVisibility") onChangedBottomMenuVisibility: (Boolean) -> Unit,
            @Assisted("isDarkTheme") isDarkTheme: Boolean,
            @Assisted("onSignOutClicked") onSignOutClicked: () -> Unit,
            @Assisted("onChangedThemeClicked") onChangedThemeClicked: (Boolean) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultProfileComponent
    }
}