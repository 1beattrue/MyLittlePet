package edu.mirea.onebeattrue.mylittlepet.presentation.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.FeedComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.PetsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileComponent
import kotlinx.coroutines.flow.StateFlow

interface MainComponent {
    val model: StateFlow<MainStore.State>

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Feed(val component: FeedComponent) : Child
        data class Pets(val component: PetsComponent) : Child
        data class Profile(val component: ProfileComponent) : Child
    }

    fun navigateTo(navigationItem: NavigationItem)
}