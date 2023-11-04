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
    petsScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.AuthMain.route
    ) {
        authNavGraph(
            enterPhoneScreenContent = enterPhoneScreenContent,
            confirmPhoneScreenContent = confirmPhoneScreenContent
        )

        mainNavGraph(
            petsScreenContent = petsScreenContent
        )
    }
}