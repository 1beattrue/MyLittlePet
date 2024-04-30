package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultFeedComponent @AssistedInject constructor(

    @Assisted("componentContext") componentContext: ComponentContext
) : FeedComponent, ComponentContext by componentContext {


    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFeedComponent
    }
}