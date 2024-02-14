package edu.mirea.onebeattrue.mylittlepet.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen

fun NavGraphBuilder.mainNavGraph(
    feedScreenContent: @Composable () -> Unit,
    petsScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,

    addPetScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.Pets.route,
        route = Screen.Main.route,
    ) {
        composable(Screen.Feed.route) {
            feedScreenContent()
        }
        petsNavGraph(
            addPetScreenContent = addPetScreenContent,
            petsScreenContent = petsScreenContent
        )
        composable(Screen.Profile.route) {
            profileScreenContent()
        }
    }
}