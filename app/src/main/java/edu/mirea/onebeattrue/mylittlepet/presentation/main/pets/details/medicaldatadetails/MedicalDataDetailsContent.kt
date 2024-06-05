package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatadetails

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import edu.mirea.onebeattrue.mylittlepet.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MedicalDataDetailsContent(
    modifier: Modifier = Modifier,
    component: MedicalDataDetailsComponent
) {
    var aspectRatio by remember { mutableFloatStateOf(1f) }

    val context = LocalContext.current
    LaunchedEffect(component.medicalData.imageUri) {
        val sizeRetriever = GlideSizeRetriever(context)
        sizeRetriever.getImageSize(Uri.parse(component.medicalData.imageUri)) { width, height ->
            aspectRatio = if (height != 0) width.toFloat() / height.toFloat() else 1f
        }
    }

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
                        text = stringResource(R.string.medical_details_app_bar_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onBackClick() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var scale by remember {
                mutableFloatStateOf(1f)
            }

//            var rotation by remember {
//                mutableStateOf(1f)
//            }

            var offset by remember {
                mutableStateOf(Offset.Zero)
            }

            BoxWithConstraints(
                modifier = Modifier
                    //.fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .background(Color.Black)
            ) {
                val state =
                    rememberTransformableState { zoomChange, panChange, rotationChange ->
                        scale = (scale * zoomChange).coerceIn(1f, 5f)

                        //rotation += rotationChange

                        val extraWidth = (scale - 1) * constraints.maxWidth
                        val extraHeight = (scale - 1) * constraints.maxHeight

                        val maxX = extraWidth / 2
                        val maxY = extraHeight / 2

                        offset = Offset(
                            x = (offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                            y = (offset.y + panChange.y * scale).coerceIn(-maxY, maxY)
                        )
                    }

                GlideImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
//                            rotationZ = rotation
                            translationX = offset.x
                            translationY = offset.y
                        }
                        .transformable(state),
                    model = component.medicalData.imageUri,
                    contentDescription = null,
                )
            }
        }
    }
}

class GlideSizeRetriever(private val context: Context) {
    fun getImageSize(imageUri: Uri, onSizeReady: (Int, Int) -> Unit) {
        Glide.with(context)
            .load(imageUri)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    onSizeReady(resource.intrinsicWidth, resource.intrinsicHeight)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}