package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.AddPetContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListContent

@Composable
fun PetsContent(
    modifier: Modifier = Modifier,
    component: PetsComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is PetsComponent.Child.AddPet -> {
                AddPetContent(component = instance.component)
            }

            is PetsComponent.Child.Details -> {
                Text(text = "details")
            }

            is PetsComponent.Child.EditPet -> {
                Text(text = "edit pet")
            }

            is PetsComponent.Child.PetList -> {
                PetListContent(component = instance.component)
            }
        }
    }
}