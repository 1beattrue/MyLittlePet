package edu.mirea.onebeattrue.mylittlepet.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.mirea.onebeattrue.mylittlepet.navigation.graphs.AppNavGraph
import edu.mirea.onebeattrue.mylittlepet.navigation.NavigationItem
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen
import edu.mirea.onebeattrue.mylittlepet.navigation.rememberNavigationState
import edu.mirea.onebeattrue.mylittlepet.presentation.MainActivity
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.auth.ConfirmPhoneScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.auth.EnterPhoneScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.main.FeedScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.main.PetsScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.main.ProfileScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.ViewModelFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModelFactory: ViewModelFactory,
    activity: MainActivity
) {
    val navigationState = rememberNavigationState()
    var isBottomBarVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isAuthFinished by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
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
            startDestination = if (isAuthFinished) Screen.Main.route else Screen.AuthMain.route,
            enterPhoneScreenContent = {
                isBottomBarVisible = false
                EnterPhoneScreen(
                    nextScreen = {
                        navigationState.navigateTo(Screen.ConfirmPhone.route)
                    },
                    viewModelFactory = viewModelFactory,
                    activity = activity
                )
            },
            confirmPhoneScreenContent = {
                isBottomBarVisible = false
                ConfirmPhoneScreen(
                    previousScreen = {
                        navigationState.navigateTo(Screen.EnterPhone.route)
                    },
                    nextScreen = {
                        navigationState.navHostController.navigate(Screen.Main.route)
                        isAuthFinished = true
                    },
                    viewModelFactory = viewModelFactory
                )
            },
            feedScreenContent = {
                isBottomBarVisible = true
                FeedScreen()
            },
            petsScreenContent = {
                isBottomBarVisible = true
                PetsScreen()
            },
            profileScreenContent = {
                isBottomBarVisible = true
                ProfileScreen()
            }
        )
    }
}