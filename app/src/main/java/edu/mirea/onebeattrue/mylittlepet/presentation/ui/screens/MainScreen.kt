package edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import edu.mirea.onebeattrue.mylittlepet.navigation.AppNavGraph
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen
import edu.mirea.onebeattrue.mylittlepet.navigation.rememberNavigationState
import edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.auth.ConfirmPhoneScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.auth.EnterPhoneScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.main.PetsScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold {
        AppNavGraph(
            navHostController = navigationState.navHostController,
            enterPhoneScreenContent = {
                EnterPhoneScreen(
                    onNextButtonClickListener = {
                        navigationState.navigateTo(Screen.ConfirmPhone.route)
                    }
                )
            },
            confirmPhoneScreenContent = {
                ConfirmPhoneScreen(
                    onBackButtonClickListener = {
                        navigationState.navigateTo(Screen.EnterPhone.route)
                    },
                    onConfirmButtonClickListener = {
                        navigationState.navHostController.navigate(Screen.Main.route) {
                            popUpTo(navigationState.navHostController.graph.startDestinationId)
                        }
                    }
                )
            },
            petsScreenContent = {
                PetsScreen()
            }
        )
    }
}