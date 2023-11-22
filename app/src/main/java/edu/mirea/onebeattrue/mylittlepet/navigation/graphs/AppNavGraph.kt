package edu.mirea.onebeattrue.mylittlepet.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    startDestination: String,

    authScreenContent: @Composable () -> Unit,

    feedScreenContent: @Composable () -> Unit,
    petsScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            authScreenContent()
        }

        mainNavGraph(
            feedScreenContent = feedScreenContent,
            petsScreenContent = petsScreenContent,
            profileScreenContent = profileScreenContent
        )
    }
}