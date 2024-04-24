package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R

@Composable
fun PhoneContent(
    modifier: Modifier = Modifier,
    component: PhoneComponent
) {
    val state by component.model.collectAsState()

    val activity = LocalContext.current as Activity

    when (val phoneState = state.phoneState) {
        PhoneStore.State.PhoneState.Initial -> {

        }

        is PhoneStore.State.PhoneState.Error -> {
            Text(text = phoneState.message)
        }

        PhoneStore.State.PhoneState.Loading -> {
            CircularProgressIndicator()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.phone,
            onValueChange = {
                component.changePhone(it)
            },
            isError = state.isIncorrectPhone,
            supportingText = {
                if (state.isIncorrectPhone) {
                    Text(text = stringResource(id = R.string.error_phone_number))
                }
            },
            trailingIcon = {
                if (state.isIncorrectPhone) {
                    Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                }
            },
        )
        Button(onClick = { component.onConfirmPhone(
            phone = state.phone,
            activity = activity
        ) }) {
            Text(text = "Next")
        }
    }
}