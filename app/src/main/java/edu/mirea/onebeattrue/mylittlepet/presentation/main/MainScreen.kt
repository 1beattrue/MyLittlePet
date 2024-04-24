//package edu.mirea.onebeattrue.mylittlepet.presentation.main
//
//import android.annotation.SuppressLint
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.compose.currentBackStackEntryAsState
//import edu.mirea.onebeattrue.mylittlepet.domain.main.entity.MainScreenState
//import edu.mirea.onebeattrue.mylittlepet.navigation.NavigationItem
//import edu.mirea.onebeattrue.mylittlepet.navigation.Screen
//import edu.mirea.onebeattrue.mylittlepet.navigation.graphs.AppNavGraph
//import edu.mirea.onebeattrue.mylittlepet.navigation.rememberNavigationState
//import edu.mirea.onebeattrue.mylittlepet.presentation.MainActivity
//import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory
//import edu.mirea.onebeattrue.mylittlepet.presentation.feed.FeedScreen
//import edu.mirea.onebeattrue.mylittlepet.presentation.pets.AddPetScreen
//import edu.mirea.onebeattrue.mylittlepet.presentation.pets.PetsScreen
//import edu.mirea.onebeattrue.mylittlepet.presentation.profile.ProfileScreen
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MainScreen(
//    viewModelFactory: ViewModelFactory,
//    activity: MainActivity,
//    initialScreenState: MainScreenState = MainScreenState.AuthFlow()
//) {
//    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
//    val navigationState = rememberNavigationState()
//
//    var startDestination by rememberSaveable {
//        mutableStateOf(Screen.Auth.route)
//    }
//
//    var bottomBarVisibility by rememberSaveable {
//        mutableStateOf(false)
//    }
//
//    val screenState by viewModel.mainScreenState.collectAsState(initialScreenState)
//    when (val state = screenState) {
//        is MainScreenState.AuthFlow -> {
//            startDestination = Screen.Auth.route
//            bottomBarVisibility = state.isBottomBarVisible
//        }
//
//        is MainScreenState.MainFlow -> {
//            startDestination = Screen.Main.route
//            bottomBarVisibility = state.isBottomBarVisible
//        }
//    }
//
//    Scaffold(
//        bottomBar = {
//            AnimatedVisibility(
//                visible = bottomBarVisibility,
//                enter = expandVertically(),
//                exit = shrinkVertically()
//            ) {
//                NavigationBar {
//                    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
//
//                    val items = listOf(
//                        NavigationItem.Feed,
//                        NavigationItem.Pets,
//                        NavigationItem.Profile
//                    )
//
//                    items.forEach { item ->
//
//                        val selected = navBackStackEntry?.destination?.hierarchy?.any {
//                            it.route == item.screen.route
//                        } ?: false
//
//                        NavigationBarItem(
//                            selected = selected,
//                            onClick = {
//                                if (!selected) {
//                                    navigationState.navigateTo(item.screen.route)
//                                }
//                            },
//                            icon = {
//                                Icon(
//                                    painter = painterResource(id = item.iconResId),
//                                    contentDescription = null
//                                )
//                            },
//                            label = {
//                                Text(text = stringResource(id = item.titleResId))
//                            },
//                            colors = NavigationBarItemDefaults.colors(
//                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
//                                unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
//                                unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
//                            )
//                        )
//                    }
//                }
//            }
//        }
//    ) { paddingValues ->
//        AppNavGraph(
//            navHostController = navigationState.navHostController,
//            startDestination = startDestination,
//            authScreenContent = {
//                bottomBarVisibility = false // TODO: Придумать что с этим сделать
//                AuthScreen(
//                    modifier = Modifier.padding(paddingValues),
//                    finishAuth = {
//                        navigationState.navigateWithoutSavingStateTo(Screen.Main.route)
//                        viewModel.finishAuth()
//                    },
//                    viewModelFactory = viewModelFactory,
//                    activity = activity
//                )
//            },
//            feedScreenContent = {
//                bottomBarVisibility = true
//                FeedScreen(
//                    modifier = Modifier.padding(paddingValues),
//                )
//            },
//            petsScreenContent = {
//                bottomBarVisibility = true
//                PetsScreen(
//                    modifier = Modifier.padding(paddingValues),
//                    viewModelFactory = viewModelFactory,
//                    addPet = {
//                        navigationState.navigateWithoutRestoringStateTo(Screen.AddPet.route)
//                    }
//                )
//            },
//            profileScreenContent = {
//                bottomBarVisibility = true
//                ProfileScreen(
//                    modifier = Modifier.padding(paddingValues),
//                    viewModelFactory = viewModelFactory,
//                    signOut = {
//                        viewModel.signOut()
//                    }
//                )
//            },
//            addPetScreenContent = {
//                bottomBarVisibility = false
//                AddPetScreen(
//                    modifier = Modifier.padding(),
//                    viewModelFactory = viewModelFactory,
//                    closeScreen = {
//                        navigationState.navigateWithoutSavingStateTo(Screen.Main.route)
//                    }
//                )
//            }
//        )
//    }
//}