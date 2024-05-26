package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.AddPetContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsRootContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet.EditPetContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListContent

@Composable
fun PetsContent(
    modifier: Modifier = Modifier,
    component: PetsComponent
) {
    val stack = component.stack

    Children(
        modifier = modifier,
        stack = stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is PetsComponent.Child.AddPet -> {
                AddPetContent(component = instance.component)
            }

            is PetsComponent.Child.Details -> {
                DetailsRootContent(component = instance.component)
            }

            is PetsComponent.Child.EditPet -> {
                EditPetContent(component = instance.component)
            }

            is PetsComponent.Child.PetList -> {
                PetListContent(component = instance.component)
            }
        }
    }
}