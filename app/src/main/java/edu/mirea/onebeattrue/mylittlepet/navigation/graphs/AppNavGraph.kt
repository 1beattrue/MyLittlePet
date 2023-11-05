package edu.mirea.onebeattrue.mylittlepet.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    startDestination: String,

    enterPhoneScreenContent: @Composable () -> Unit,
    confirmPhoneScreenContent: @Composable () -> Unit,

    feedScreenContent: @Composable () -> Unit,
    petsScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
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