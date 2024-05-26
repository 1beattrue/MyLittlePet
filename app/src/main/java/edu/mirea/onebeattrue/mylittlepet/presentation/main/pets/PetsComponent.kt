package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.AddPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsRootComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet.EditPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListComponent

interface PetsComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class PetList(val component: PetListComponent) : Child()
        class AddPet(val component: AddPetComponent) : Child()
        class EditPet(val component: EditPetComponent) : Child()
        class Details(val component: DetailsRootComponent) : Child()
    }
}