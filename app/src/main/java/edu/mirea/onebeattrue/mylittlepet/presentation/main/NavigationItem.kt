package edu.mirea.onebeattrue.mylittlepet.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import edu.mirea.onebeattrue.mylittlepet.R

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector
) {
    data object FeedItem : NavigationItem(
        titleResId = R.string.navigation_item_feed,
        icon = Icons.Rounded.Home
    )

    data object PetsItem : NavigationItem(
        titleResId = R.string.navigation_item_pets,
        icon = Icons.AutoMirrored.Rounded.List
    )

    data object ProfileItem : NavigationItem(
        titleResId = R.string.navigation_item_profile,
        icon = Icons.Rounded.AccountCircle
    )
}