package edu.mirea.onebeattrue.mylittlepet.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.mirea.onebeattrue.mylittlepet.navigation.Screen

fun NavGraphBuilder.authNavGraph(
    enterPhoneScreenContent: @Composable () -> Unit,
    confirmPhoneScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.EnterPhone.route,
        route = Screen.AuthMain.route,
    ) {
        composable(Screen.EnterPhone.route) {
            enterPhoneScreenContent()
        }

        composable(Screen.ConfirmPhone.route) {
            confirmPhoneScreenContent()
        }
    }
}