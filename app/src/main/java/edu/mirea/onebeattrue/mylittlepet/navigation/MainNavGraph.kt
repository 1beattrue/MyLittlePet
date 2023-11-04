package edu.mirea.onebeattrue.mylittlepet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.mainNavGraph(
    petsScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.Pets.route,
        route = Screen.Main.route,
    ) {
        composable(Screen.Pets.route) {
            petsScreenContent()
        }
    }
}