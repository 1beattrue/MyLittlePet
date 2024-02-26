package edu.mirea.onebeattrue.mylittlepet.ui.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

val ANIMATION_FADE_IN = fadeIn(animationSpec = tween())
val ANIMATION_FADE_OUT = fadeOut(animationSpec = tween())

val ANIMATION_TRANSITION_SLIDE_FROM_END_TO_START = (slideInHorizontally { width -> width } + ANIMATION_FADE_IN)
    .togetherWith(slideOutHorizontally { width -> -width } + ANIMATION_FADE_OUT)

val ANIMATION_TRANSITION_SLIDE_FROM_START_TO_END = (slideInHorizontally { width -> -width } + ANIMATION_FADE_IN)
    .togetherWith(slideOutHorizontally { width -> width } + ANIMATION_FADE_OUT)

val ANIMATION_TRANSITION_FADE_IN_FADE_OUT = ANIMATION_FADE_IN togetherWith ANIMATION_FADE_OUT