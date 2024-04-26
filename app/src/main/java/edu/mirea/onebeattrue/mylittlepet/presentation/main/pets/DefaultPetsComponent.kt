package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignOutUseCase

class DefaultPetsComponent @AssistedInject constructor(
    // TODO: подзалупный творожок (для тестирования)
    @Assisted("onLoggedOut") private val onLoggedOut: () -> Unit,
    private val signOutUseCase: SignOutUseCase,

    @Assisted("componentContext") componentContext: ComponentContext
) : PetsComponent, ComponentContext by componentContext {

    override fun logOut() {
        signOutUseCase()
        onLoggedOut()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            // TODO: подзалупный творожок (для тестирования)
            @Assisted("onLoggedOut") onLoggedOut: () -> Unit,

            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetsComponent
    }
}