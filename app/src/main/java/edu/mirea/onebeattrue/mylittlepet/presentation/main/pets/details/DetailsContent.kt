package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import android.annotation.SuppressLint
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import kotlinx.coroutines.launch

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent
) {
    val state by component.model.collectAsState()

    Button(onClick = { component.onAddEventClick() }) {

    }

    EventBottomSheet(
        isExpanded = state.eventBottomSheetState,
        onCloseBottomSheet = {
            component.onCloseBottomSheetClick()
        },
        onAddEvent = { event ->
            component.addEvent(event)
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventBottomSheet(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onCloseBottomSheet: () -> Unit,
    onAddEvent: (Event) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (isExpanded) {
        ModalBottomSheet(
            onDismissRequest = {
                onCloseBottomSheet()
            },
            sheetState = sheetState
        ) {
            Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onCloseBottomSheet()
                    }
                }
            }) {
                Text("Hide bottom sheet")
            }
        }
    }
}