package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.convertMillisToDayMonthYear
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ClickableCustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInfoContent(
    modifier: Modifier = Modifier,
    component: PetInfoComponent
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.pet_info_app_bar_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onClickBack() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            component.addPet()
                            component.onClickBack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddCircle,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (val pet = component.pet) {
                null -> {
                    item {
                        Error()
                    }
                }

                else -> {
                    item {
                        PetCard(pet = pet)
                    }

                    item {
                        PetTypeCard(petType = pet.type)
                    }

                    item {
                        PetDateOfBirthCard(dateOfBirth = pet.dateOfBirth)
                    }

                    item {
                        PetWeightCard(weight = pet.weight)
                    }
                }
            }
        }
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
) {
    CustomCardExtremeElevation(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.Error,
            contentDescription = null
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.something_went_wrong),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION
    ) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = pet.name,
            fontWeight = FontWeight.Bold,
        )

        Box(
            modifier = Modifier.clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER))
        ) {
            if (Uri.parse(pet.imageUri) == Uri.EMPTY) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = pet.type.getImageId()),
                    contentDescription = null
                )
            } else {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    model = pet.imageUri,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun PetTypeCard(
    modifier: Modifier = Modifier,
    petType: PetType,
) {
    CustomCardExtremeElevation(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.pet_type),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
            )
            Icon(
                painter = painterResource(petType.getImageId()),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PetDateOfBirthCard(
    modifier: Modifier = Modifier,
    dateOfBirth: Long?,
) {
    CustomCardExtremeElevation(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.age_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
            )
            val dobText = if (dateOfBirth == null) {
                stringResource(R.string.unknown)
            } else {
                val (day, month, year) = dateOfBirth.convertMillisToDayMonthYear()
                stringResource(R.string.date_format, day, month.getName(), year)
            }
            Text(text = dobText)
        }
    }
}

@Composable
private fun PetWeightCard(
    modifier: Modifier = Modifier,
    weight: Float?,
) {
    CustomCardExtremeElevation(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.weight_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
            )
            val weightText = if (weight == null) {
                stringResource(R.string.unknown)
            } else {
                "${weight} ${stringResource(R.string.weight_suffix)}"
            }
            Text(text = weightText)
        }
    }
}