package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent? = null
) {
    val state by component!!.model.collectAsState()

    Column(
        modifier = modifier
    ) {

    }
    CustomCard(elevation = 0.dp) {

        GlideImage(
            model = Uri.EMPTY,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun DetailsContentPreviewLight() {
    MyLittlePetTheme(darkTheme = false) {
        DetailsContent()
    }
}

@Preview
@Composable
private fun DetailsContentPreviewDark() {
    MyLittlePetTheme(darkTheme = true) {
        DetailsContent()
    }
}