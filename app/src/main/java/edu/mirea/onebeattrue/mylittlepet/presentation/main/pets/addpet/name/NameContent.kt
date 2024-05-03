package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun NameContent(
    modifier: Modifier = Modifier,
    component: NameComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCardExtremeElevation {

            Text(
                text = stringResource(id = R.string.enter_pet_name),
                style = MaterialTheme.typography.titleLarge
            )

            EnteredNameField(
                petName = state.petName,
                onValueChange = { petName ->
                    component.setPetName(petName)
                },
                isIncorrect = state.isIncorrect
            )

            CustomNextButton(onClick = { component.next() })
        }
    }
}

@Composable
private fun EnteredNameField(
    modifier: Modifier = Modifier,
    petName: String,
    onValueChange: (String) -> Unit,
    isIncorrect: Boolean,

    ) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = petName,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(stringResource(id = R.string.pet_name_hint))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        singleLine = true,
        isError = isIncorrect,
        supportingText = {
            if (isIncorrect) {
                Text(text = stringResource(id = R.string.error_pet_name))
            }
        },
        trailingIcon = {
            if (isIncorrect) {
                Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
            }
        },
    )
}