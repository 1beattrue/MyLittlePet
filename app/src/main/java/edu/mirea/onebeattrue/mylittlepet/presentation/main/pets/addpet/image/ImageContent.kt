package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomAddButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    component: ImageComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCardExtremeElevation {

            Text(
                text = stringResource(id = R.string.add_pet_photo),
                style = MaterialTheme.typography.titleLarge
            )

//            GlideImage(
//                model = if (state.imageUri == Uri.EMPTY) {
//                    painterResource(component.petType.getImageId())
//                } else {
//                    state.imageUri
//                },
//                contentDescription = null
//            )

            CustomAddButton(onClick = { component.addPet() })
        }
    }
}