package edu.mirea.onebeattrue.mylittlepet.presentation.main

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.getValue
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.FeedContent
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
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                BottomNavigation(
                    onNavigationItemClick = component::navigateTo,
                    selectedItem = state.selectedItem,
                    backHandlingEnabled = state.backHandlingEnabled
                )
            }
        }
    ) { paddingValues ->
        Log.d("MainContent", "перерисовка")
        Children(
            modifier = Modifier.padding(paddingValues),
            stack = component.stack,
            animation = stackAnimation(fade())
        ) {
            when (val instance = it.instance) {
                is MainComponent.Child.Feed -> {
                    FeedContent(component = instance.component)
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
    selectedItem: NavigationItem,
    backHandlingEnabled: Boolean
) {
    val navigationItems = listOf(
        NavigationItem.FeedItem,
        NavigationItem.PetsItem,
        NavigationItem.ProfileItem,
    )

    NavigationBar(
        modifier = modifier
    ) {
        navigationItems.forEach { navigationItem ->

            // TODO: мне не очень нравится это решение, возможно стоит найти решение получше
            BackHandler(
                enabled = backHandlingEnabled
            ) {
                onNavigationItemClick(NavigationItem.PetsItem)
            }

            NavigationBarItem(
                selected = selectedItem == navigationItem,
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