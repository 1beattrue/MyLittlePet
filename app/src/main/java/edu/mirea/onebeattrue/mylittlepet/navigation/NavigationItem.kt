package edu.mirea.onebeattrue.mylittlepet.navigation

import edu.mirea.onebeattrue.mylittlepet.R

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val iconResId: Int
) {
    object Feed : NavigationItem(
        screen = Screen.Feed,
        titleResId = R.string.navigation_item_feed,
        iconResId = R.drawable.ic_feed
    )

    object Pets : NavigationItem(
        screen = Screen.Pets,
        titleResId = R.string.navigation_item_pets,
        iconResId = R.drawable.ic_pets
    )

    object Profile : NavigationItem(
        screen = Screen.Profile,
        titleResId = R.string.navigation_item_profile,
        iconResId = R.drawable.ic_profile
    )
}