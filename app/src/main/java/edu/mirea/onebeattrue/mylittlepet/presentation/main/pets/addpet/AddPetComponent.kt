package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.NameComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type.TypeComponent

interface AddPetComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Type(val component: TypeComponent) : Child()
        class Name(val component: NameComponent) : Child()
        class Image(val component: ImageComponent) : Child()
    }
}