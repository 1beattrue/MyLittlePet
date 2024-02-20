package edu.mirea.onebeattrue.mylittlepet.ui.animation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

val ANIMATION_TRANSITION_SLIDE_IN = (slideInHorizontally { width -> width } + fadeIn())
    .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())

val ANIMATION_TRANSITION_SLIDE_OUT = (slideInHorizontally { width -> -width } + fadeIn())
    .togetherWith(slideOutHorizontally { width -> width } + fadeOut())

val ANIMATION_TRANSITION_FADE_IN = fadeIn() togetherWith fadeOut()