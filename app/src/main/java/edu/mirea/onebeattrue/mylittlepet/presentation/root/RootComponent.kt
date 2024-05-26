package edu.mirea.onebeattrue.mylittlepet.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainComponent
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {
    val model: StateFlow<RootStore.State>
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Auth(val component: AuthComponent) : Child()
        class Main(val component: MainComponent) : Child()
    }
}