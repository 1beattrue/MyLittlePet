package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Photo
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomImagePicker
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomReadyButton

@Composable
fun MedicalPhotoContent(
    modifier: Modifier = Modifier,
    component: MedicalPhotoComponent
) {
    val state by component.model.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            CustomCardExtremeElevation {

                Text(
                    text = stringResource(id = R.string.add_medical_photo),
                    style = MaterialTheme.typography.titleLarge
                )

                CustomImagePicker(
                    uri = state.imageUri,
                    onImagePicked = {
                        component.setPhoto(it)
                    },
                    onImageDeleted = {
                        component.deletePhoto()
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageVector = Icons.Rounded.Photo,
                        contentDescription = null
                    )
                }

                CustomReadyButton(onClick = { component.finish() })
            }
        }

    }
}