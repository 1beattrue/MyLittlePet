package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomImagePicker
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomReadyButton

@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    component: ImageComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCardExtremeElevation {

            Text(
                text = stringResource(id = R.string.add_pet_photo),
                style = MaterialTheme.typography.titleLarge
            )

            CustomImagePicker(
                uri = state.imageUri,
                onImagePicked = {
                    component.setPetImage(it)
                },
                onImageDeleted = { component.deletePetImage() }
            ) {
               Image(
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(8.dp),
                   painter = painterResource(id = component.petType.getImageId()),
                   contentDescription = null
               )
            }

            CustomReadyButton(onClick = { component.finish() })
        }
    }
}
