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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onSignOutClicked") onSignOutClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultProfileComponent
    }
}