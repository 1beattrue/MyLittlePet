package edu.mirea.onebeattrue.mylittlepet.presentation.screens.main

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.MainScreenState
import edu.mirea.onebeattrue.mylittlepet.navigation.NavigationItem
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen
import edu.mirea.onebeattrue.mylittlepet.navigation.graphs.AppNavGraph
import edu.mirea.onebeattrue.mylittlepet.navigation.rememberNavigationState
import edu.mirea.onebeattrue.mylittlepet.presentation.MainActivity
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.auth.AuthScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.main.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModelFactory: ViewModelFactory,
    activity: MainActivity
) {
    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
    val navigationState = rememberNavigationState()

    var startDestination by rememberSaveable {
        mutableStateOf(Screen.Auth.route)
    }

    var bottomBarVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    val screenState by viewModel.mainScreenState.collectAsState(MainScreenState.Initial)
    when (val state = screenState) {
        is MainScreenState.AuthFlow -> {
            startDestination = Screen.Auth.route
            bottomBarVisibility = state.isBottomBarVisible
        }

        is MainScreenState.MainFlow -> {
            startDestination = Screen.Main.route
            bottomBarVisibility = state.isBottomBarVisible
        }

        MainScreenState.Initial -> {
        }
    }

    Scaffold(
        bottomBar = {
            if (bottomBarVisibility) {
                NavigationBar {
                    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                    val items = listOf(
                        NavigationItem.Feed,
                        NavigationItem.Pets,
                        NavigationItem.Profile
                    )

                    items.forEach { item ->

                        val selected = navBackStackEntry?.destination?.route == item.screen.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navigationState.navigateTo(item.screen.route)
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.iconResId),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = stringResource(id = item.titleResId))
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        )
                    }
                }
            }
        }
    ) {
        AppNavGraph(
            navHostController = navigationState.navHostController,
            startDestination = startDestination,
            authScreenContent = {
                AuthScreen(
                    finishAuth = {
                        navigationState.navHostController.navigate(Screen.Main.route)
                        viewModel.finishAuth()
                    },
                    viewModelFactory = viewModelFactory,
                    activity = activity
                )
            },
            feedScreenContent = {
                FeedScreen()
            },
            petsScreenContent = {
                PetsScreen()
            },
            profileScreenContent = {
                ProfileScreen(
                    viewModelFactory = viewModelFactory,
                    signOut = {
                        viewModel.signOut()
                    }
                )
            }
        )
    }
}