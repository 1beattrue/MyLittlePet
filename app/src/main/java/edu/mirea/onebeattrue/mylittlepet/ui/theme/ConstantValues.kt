package edu.mirea.onebeattrue.mylittlepet.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey

val USE_SYSTEM_THEME = booleanPreferencesKey("use_system_theme")
val USE_SYSTEM_LANG = booleanPreferencesKey("use_system_lang")
val IS_NIGHT_MODE_KEY = booleanPreferencesKey("is_night_mode")
val IS_ENGLISH_MODE_KEY = booleanPreferencesKey("is_english_mode")

const val DATA_STORE_SETTINGS = "settings"
const val SUPPORT_EMAIL = "firstbadger@inbox.ru"

val CORNER_RADIUS_SURFACE = 32.dp
val CORNER_RADIUS_CONTAINER = 16.dp

val DEFAULT_ELEVATION = 1.dp
val STRONG_ELEVATION = 4.dp
val EXTREME_ELEVATION = 8.dp

val MENU_ITEM_PADDING = PaddingValues(
    start = 16.dp,
    end = 16.dp,
    top = 8.dp,
    bottom = 8.dp
)