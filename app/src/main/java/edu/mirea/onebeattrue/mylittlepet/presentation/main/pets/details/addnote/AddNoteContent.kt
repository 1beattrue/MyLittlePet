package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomReadyButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteContent(
    modifier: Modifier = Modifier,
    component: AddNoteComponent
) {
    val state by component.model.collectAsState()

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
                        text = stringResource(R.string.add_note_app_bar_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onBackClicked() },
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                CustomCardExtremeElevation {
                    IconPicker(
                        onIconClicked = { noteIcon ->
                            component.iconChanged(noteIcon)
                        },
                        selectedIcon = state.selectedIcon
                    )
                    EnteredTextField(
                        text = state.text,
                        onValueChange = { component.noteTextChanged(it) },
                        isIncorrect = state.isIncorrect
                    )
                    CustomReadyButton(onClick = { component.addNote() })
                }
            }
        }
    }
}

@Composable
private fun IconPicker(
    modifier: Modifier = Modifier,
    onIconClicked: (NoteIcon) -> Unit,
    selectedIcon: NoteIcon
) {
    Text(
        text = stringResource(id = R.string.select_note_icon),
        style = MaterialTheme.typography.titleLarge
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NoteIcon.getItems().forEach { noteIcon ->
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (noteIcon == selectedIcon) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    contentColor = if (noteIcon == selectedIcon) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
                onClick = { onIconClicked(noteIcon) }
            ) {
                Icon(
                    painter = painterResource(noteIcon.iconResId),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun EnteredTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    isIncorrect: Boolean,
) {
    Text(
        text = stringResource(id = R.string.enter_note_text),
        style = MaterialTheme.typography.titleLarge
    )
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(stringResource(id = R.string.new_event_hint))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        isError = isIncorrect,
        supportingText = {
            if (isIncorrect) {
                Text(text = stringResource(id = R.string.error_note_text))
            }
        },
        trailingIcon = {
            if (isIncorrect) {
                Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
            }
        },
        minLines = 3,
        maxLines = 3
    )
}