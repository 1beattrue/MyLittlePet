package edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.auth

import androidx.compose.runtime.Composable
import edu.mirea.onebeattrue.mylittlepet.navigation.AppNavGraph
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen
import edu.mirea.onebeattrue.mylittlepet.navigation.rememberNavigationState
import edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.main.MainScreen

@Composable
fun AuthMainScreen() {
    val navigationState = rememberNavigationState()
    AppNavGraph(
        navHostController = navigationState.navHostController,
        enterPhoneScreenContent = {
            EnterPhoneScreen(
                onNextButtonClickListener = {
                    navigationState.navHostController.navigate(Screen.ConfirmPhone.route)
                }
            )
        },
        confirmPhoneScreenContent = {
            ConfirmPhoneScreen(
                onBackButtonClickListener = {
                    navigationState.navHostController.navigate(Screen.EnterPhone.route)
                },
                onConfirmButtonClickListener = {
                    navigationState.navHostController.navigate(Screen.Main.route)
                }
            )
        },
        mainScreenContent = {
            MainScreen()
        }
    )
}