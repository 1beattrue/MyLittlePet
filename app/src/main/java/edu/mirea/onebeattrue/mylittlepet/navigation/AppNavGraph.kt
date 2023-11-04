package edu.mirea.onebeattrue.mylittlepet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    enterPhoneScreenContent: @Composable () -> Unit,
    confirmPhoneScreenContent: @Composable () -> Unit,
    mainScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.AuthMain.route
    ) {
        authMainScreenNavGraph(
            enterPhoneScreenContent = enterPhoneScreenContent,
            confirmPhoneScreenContent = confirmPhoneScreenContent
        )

        composable(Screen.Main.route) {
            mainScreenContent()
        }
    }
}