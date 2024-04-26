package edu.mirea.onebeattrue.mylittlepet.presentation.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.PetsComponent

interface MainComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Pets(val component: PetsComponent) : Child
    }
}