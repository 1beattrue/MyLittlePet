package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER

@Composable
fun EventTextContent(
    modifier: Modifier = Modifier,
    component: EventTextComponent
) {
    val state by component.model.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            CustomCardExtremeElevation {

                Text(
                    text = stringResource(id = R.string.enter_event_title),
                    style = MaterialTheme.typography.titleLarge
                )

                EnteredTextField(
                    text = state.text,
                    onValueChange = { text ->
                        component.onEventTextChanged(text)
                    },
                    isIncorrect = state.isIncorrect
                )

                CustomNextButton(onClick = { component.next() })
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
                Text(text = stringResource(id = R.string.error_event_text))
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