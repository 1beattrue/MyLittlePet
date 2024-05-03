package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.AddPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.NameContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type.TypeContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetContent(
    modifier: Modifier = Modifier,
    component: EditPetComponent
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.edit_pet_app_bar_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onBackClicked() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Children(
            modifier = Modifier.padding(paddingValues),
            stack = component.stack,
            animation = stackAnimation(slide() + fade())
        ) {
            when (val instance = it.instance) {
                is EditPetComponent.Child.Name -> NameContent(component = instance.component)
                is EditPetComponent.Child.Image -> ImageContent(component = instance.component)
            }
        }
    }
}