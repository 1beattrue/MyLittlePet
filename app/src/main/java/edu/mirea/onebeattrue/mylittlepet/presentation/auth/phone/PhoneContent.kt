package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER

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
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(id = R.drawable.image_cat_face),
            contentDescription = null,
        )
        CustomCardDefaultElevation {
            Text(
                text = stringResource(id = R.string.enter_phone_number),
                style = MaterialTheme.typography.titleLarge
            )
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
                shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = {
                    Text(stringResource(id = R.string.phone_number_prefix))
                },
                placeholder = {
                    Text(stringResource(id = R.string.phone_number_hint))
                },
            )
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                CustomButton(
                    onClick = {
                        component.onConfirmPhone(
                            phone = state.phone,
                            activity = activity
                        )
                    },
                    text = stringResource(id = R.string.next),
                )
            }
        }
    }
}