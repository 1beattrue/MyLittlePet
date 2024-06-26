package edu.mirea.onebeattrue.mylittlepet.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.FeedRootContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.PetsContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileContent

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    component: MainComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = state.bottomMenuVisibility,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                BottomNavigation(
                    onNavigationItemClick = component::navigateTo,
                    stack = component.stack,
                )
            }
        }
    ) { paddingValues ->
        Children(
            modifier = Modifier.padding(paddingValues),
            stack = component.stack,
            animation = stackAnimation(fade())
        ) {
            when (val instance = it.instance) {
                is MainComponent.Child.Feed -> {
                    FeedRootContent(component = instance.component)
                }

                is MainComponent.Child.Pets -> {
                    PetsContent(component = instance.component)
                }

                is MainComponent.Child.Profile -> {
                    ProfileContent(component = instance.component)
                }
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    onNavigationItemClick: (NavigationItem) -> Unit,
    stack: Value<ChildStack<*, MainComponent.Child>>,
) {
    val selectedConfig by stack.subscribeAsState()

    val navigationItems = listOf(
        NavigationItem.FeedItem,
        NavigationItem.PetsItem,
        NavigationItem.ProfileItem,
    )

    NavigationBar(
        modifier = modifier
    ) {
        navigationItems.forEach { navigationItem ->

            NavigationBarItem(
                selected = selectedConfig.active.configuration == navigationItem.config,
                onClick = { onNavigationItemClick(navigationItem) },
                icon = {
                    Icon(
                        painter = painterResource(navigationItem.iconResId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = navigationItem.titleResId))
                }
            )
        }
    }
}