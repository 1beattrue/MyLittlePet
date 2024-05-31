package edu.mirea.onebeattrue.mylittlepet.presentation.main

import edu.mirea.onebeattrue.mylittlepet.R

sealed class NavigationItem(
    val titleResId: Int,
    val iconResId: Int,
    val config: DefaultMainComponent.Config
) {
    data object FeedItem : NavigationItem(
        titleResId = R.string.navigation_item_feed,
        iconResId = R.drawable.ic_feed,
        config = DefaultMainComponent.Config.Feed
    )

    data object PetsItem : NavigationItem(
        titleResId = R.string.navigation_item_pets,
        iconResId = R.drawable.ic_pets,
        DefaultMainComponent.Config.Pets
    )

    data object ProfileItem : NavigationItem(
        titleResId = R.string.navigation_item_settings,
        iconResId = R.drawable.ic_settings,
        config = DefaultMainComponent.Config.Profile
    )
}