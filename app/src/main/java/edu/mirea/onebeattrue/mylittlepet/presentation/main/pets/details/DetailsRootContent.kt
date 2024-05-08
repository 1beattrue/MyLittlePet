package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import androidx.compose.foundation.layout.fillMaxSize
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
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.AddEventContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsRootContent(
    modifier: Modifier = Modifier,
    component: DetailsRootComponent
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.pet_details_app_bar_title)
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
            animation = stackAnimation(fade())
        ) {
            when (val instance = it.instance) {
                is DetailsRootComponent.Child.AddEvent -> AddEventContent(component = instance.component)
                is DetailsRootComponent.Child.Details -> DetailsContent(component = instance.component)
            }
        }
    }
}