package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import android.app.Application
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

    private val application: Application,

    @Assisted("onChangedBottomMenuVisibility") private val onChangedBottomMenuVisibility: (Boolean) -> Unit,
    @Assisted("onSignOutClicked") private val onSignOutClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : ProfileComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

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
        store.accept(ProfileStore.Intent.ChangeTheme(isDarkTheme))
    }

    override fun changeLanguage(isEnglishLanguage: Boolean) {
        store.accept(ProfileStore.Intent.ChangeLanguage(isEnglishLanguage))
    }

    override fun sendEmail() {
        store.accept(ProfileStore.Intent.SendEmail)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onChangedBottomMenuVisibility") onChangedBottomMenuVisibility: (Boolean) -> Unit,
            @Assisted("onSignOutClicked") onSignOutClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultProfileComponent
    }
}