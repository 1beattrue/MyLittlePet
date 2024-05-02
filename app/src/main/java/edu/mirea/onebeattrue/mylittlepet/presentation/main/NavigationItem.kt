package edu.mirea.onebeattrue.mylittlepet.presentation.main

import edu.mirea.onebeattrue.mylittlepet.R

sealed class NavigationItem(
    val titleResId: Int,
    val iconResId: Int
) {
    data object FeedItem : NavigationItem(
        titleResId = R.string.navigation_item_feed,
        iconResId = R.drawable.ic_feed
    )

    data object PetsItem : NavigationItem(
        titleResId = R.string.navigation_item_pets,
        iconResId = R.drawable.ic_pets
    )

    data object ProfileItem : NavigationItem(
        titleResId = R.string.navigation_item_profile,
        iconResId = R.drawable.ic_profile
    )
}