package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo.MedicalPhotoComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text.MedicalTextComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type.MedicalTypeComponent

interface AddMedicalDataComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Type(val component: MedicalTypeComponent) : Child()
        class Text(val component: MedicalTextComponent) : Child()
        class Photo(val component: MedicalPhotoComponent) : Child()
    }

    fun onBackClicked()
}