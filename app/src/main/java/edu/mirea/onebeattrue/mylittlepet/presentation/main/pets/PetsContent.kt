package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.AddPetContent
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
                LaunchedEffect(key1 = stack) {
                    component.changeBottomMenuVisibility(false)
                }
                AddPetContent(component = instance.component)
            }

            is PetsComponent.Child.Details -> {
                Text(text = "details")
            }

            is PetsComponent.Child.EditPet -> {
                Text(text = "edit pet")
                LaunchedEffect(key1 = stack) {
                    component.changeBottomMenuVisibility(false)
                }
                EditPetContent(component = instance.component)
            }

            is PetsComponent.Child.PetList -> {
                LaunchedEffect(key1 = stack) {
                    component.changeBottomMenuVisibility(true)
                }
                PetListContent(component = instance.component)
            }
        }
    }
}