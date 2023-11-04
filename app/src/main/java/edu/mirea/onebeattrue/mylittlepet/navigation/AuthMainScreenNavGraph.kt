package edu.mirea.onebeattrue.mylittlepet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.authMainScreenNavGraph(
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