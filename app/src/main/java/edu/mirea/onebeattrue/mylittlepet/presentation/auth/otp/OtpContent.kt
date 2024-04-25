package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER

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
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(id = R.drawable.image_cat_face),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomCardDefaultElevation {
            Text(
                text = stringResource(id = R.string.enter_confirmation_code),
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = state.otp,
                onValueChange = {
                    component.changeOtp(it)
                },
                placeholder = {
                    Text(stringResource(id = R.string.confirmation_code_hint))
                },
                singleLine = true,
                isError = state.isIncorrectOtp,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
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
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                CustomButton(
                    onClick = {
                        component.onConfirmOtp(
                            otp = state.otp
                        )
                    },
                    text = stringResource(id = R.string.confirm),
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .clickable {
                    component.onClickResend()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.resend_code),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null
            )
        }

    }
}
