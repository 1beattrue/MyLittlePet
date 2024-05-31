package edu.mirea.onebeattrue.mylittlepet.presentation.onboarding

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultOnboardingComponent @AssistedInject constructor(
    @Assisted("onSkipOnboarding") private val onSkipOnboarding: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : OnboardingComponent, ComponentContext by componentContext {
    override fun skip() {
        onSkipOnboarding()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onSkipOnboarding") onSkipOnboarding: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultOnboardingComponent
    }
}