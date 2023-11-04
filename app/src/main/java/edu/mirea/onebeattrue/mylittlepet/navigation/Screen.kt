package edu.mirea.onebeattrue.mylittlepet.navigation

sealed class Screen(
    val route: String
) {
    object AuthMain : Screen(ROUTE_AUTH_MAIN)
    object EnterPhone : Screen(ROUTE_ENTER_PHONE)
    object ConfirmPhone : Screen(ROUTE_CONFIRM_PHONE)

    object Main : Screen(ROUTE_MAIN)

    private companion object {
        const val ROUTE_AUTH_MAIN = "auth_main"
        const val ROUTE_ENTER_PHONE = "enter_phone"
        const val ROUTE_CONFIRM_PHONE = "confirm_phone"
        const val ROUTE_MAIN = "main"
    }
}