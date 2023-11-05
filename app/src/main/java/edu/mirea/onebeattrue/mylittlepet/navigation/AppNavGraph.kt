package edu.mirea.onebeattrue.mylittlepet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    enterPhoneScreenContent: @Composable () -> Unit,
    confirmPhoneScreenContent: @Composable () -> Unit,

    feedScreenContent: @Composable () -> Unit,
    petsScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit
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
            feedScreenContent = feedScreenContent,
            petsScreenContent = petsScreenContent,
            profileScreenContent = profileScreenContent
        )
    }
}