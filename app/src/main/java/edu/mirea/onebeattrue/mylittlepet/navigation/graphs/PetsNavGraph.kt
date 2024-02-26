package edu.mirea.onebeattrue.mylittlepet.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen

fun NavGraphBuilder.petsNavGraph(
    petsScreenContent: @Composable () -> Unit,
    addPetScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.PetList.route,
        route = Screen.Pets.route,
    ) {
        composable(Screen.PetList.route) {
            petsScreenContent()
        }
        composable(Screen.AddPet.route) {
            addPetScreenContent()
        }
    }
}