package edu.mirea.onebeattrue.mylittlepet.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowLeft
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomTextButton
import kotlinx.coroutines.launch

@Composable
fun OnboardingContent(
    modifier: Modifier = Modifier,
    component: OnboardingComponent
) {
    val images = arrayOf(
        R.drawable.image_dog_face,
        R.drawable.image_notifications,
        R.drawable.image_qrcode
    )
    val description = arrayOf(
        R.string.onboarding_first,
        R.string.onboarding_second,
        R.string.onboarding_third
    )
    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )
    val scope = rememberCoroutineScope()
    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        HorizontalPager(
            state = pagerState,
            key = { images[it] }
        ) { index ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(images[index]),
                        colorFilter = if (isSystemInDarkTheme() && index != 0) ColorFilter.tint(
                            Color.White
                        )
                        else if (!isSystemInDarkTheme() && index != 0) ColorFilter.tint(Color.Black) else null,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = description[index]),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(64.dp)
                    )
                }
            }
        }
        CustomTextButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            onClick = { component.skip() },
            text = if (pagerState.currentPage != images.size - 1) {
                stringResource(R.string.skip)
            } else {
                stringResource(R.string.next)
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        ) {
            ArrowButton(
                destination = Destination.LEFT,
                visible = pagerState.currentPage != 0
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }

            Indicators(size = images.size, index = pagerState.currentPage)

            ArrowButton(
                destination = Destination.RIGHT,
                visible = pagerState.currentPage != images.size - 1
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }
    }
}

private enum class Destination {
    LEFT, RIGHT
}

@Composable
private fun ArrowButton(
    modifier: Modifier = Modifier,
    destination: Destination,
    visible: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = when (destination) {
            Destination.LEFT -> Arrangement.Start
            Destination.RIGHT -> Arrangement.End
        }
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                onClick = {
                    onClick()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = when (destination) {
                        Destination.LEFT -> Icons.AutoMirrored.Rounded.ArrowLeft
                        Destination.RIGHT -> Icons.AutoMirrored.Rounded.ArrowRight
                    },
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Indicators(
    modifier: Modifier = Modifier,
    size: Int,
    index: Int
) {
    Row(
        modifier = modifier.align(Alignment.Center),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
private fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 20.dp else 10.dp,
        animationSpec = tween(easing = FastOutSlowInEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
            )
    )
}