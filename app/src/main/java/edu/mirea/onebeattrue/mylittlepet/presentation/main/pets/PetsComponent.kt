package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.AddPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet.EditPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListComponent

interface PetsComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class PetList(val component: PetListComponent) : Child
        data class AddPet(val component: AddPetComponent) : Child
        data class EditPet(val component: EditPetComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }
}