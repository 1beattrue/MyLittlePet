package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

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
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R

@Composable
fun OtpContent(
    modifier: Modifier = Modifier,
    component: OtpComponent
) {
    val state by component.model.collectAsState()

    when (val otpState = state.otpState) {
        OtpStore.State.OtpState.Initial -> {

        }
        is OtpStore.State.OtpState.Error -> {
            Text(text = otpState.message)
        }
        OtpStore.State.OtpState.Loading -> {
            CircularProgressIndicator()
        }
        OtpStore.State.OtpState.Resent -> {
            Text(text = "Resent")
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.otp,
            onValueChange = {
                component.changeOtp(it)
            },
            isError = state.isIncorrectOtp,
            supportingText = {
                if (state.isIncorrectOtp) {
                    Text(text = stringResource(id = R.string.error_confirmation_code))
                }
            },
            trailingIcon = {
                if (state.isIncorrectOtp) {
                    Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                }
            },
        )
        Button(onClick = { component.onConfirmOtp(
            otp = state.otp
        ) }) {
            Text(text = "Confirm")
        }
    }
}