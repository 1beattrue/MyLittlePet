package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ClickableCustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    component: FeedComponent
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.feed_app_bar_title)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { component.openScanner() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QrCodeScanner,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                OpenQrCodeScannerCard {
                    component.openScanner()
                }
            }
            item {
                LastPetScannedCard(pet = null) {

                }
            }
        }
    }
}

@Composable
private fun OpenQrCodeScannerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        paddingValues = PaddingValues(start = 16.dp),
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Rounded.QrCodeScanner,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

@Composable
private fun LastPetScannedCard(
    modifier: Modifier = Modifier,
    pet: Pet?,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        paddingValues = PaddingValues(end = 16.dp),
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(
                if (pet != null) {
                    pet.type.getImageId()
                } else {
                    R.drawable.ic_question
                }
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}