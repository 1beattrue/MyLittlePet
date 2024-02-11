package edu.mirea.onebeattrue.mylittlepet.navigation

sealed class Screen(
    val route: String
) {
    object Auth : Screen(ROUTE_AUTH)

    object Main : Screen(ROUTE_MAIN)
    object Feed : Screen(ROUTE_FEED)
    object Pets : Screen(ROUTE_PETS)
    object Profile : Screen(ROUTE_PROFILE)

    private companion object {
        const val ROUTE_AUTH = "auth"
        const val ROUTE_MAIN = "main"
        const val ROUTE_FEED = "feed"
        const val ROUTE_PETS = "pets"
        const val ROUTE_PROFILE = "profile"
    }
}