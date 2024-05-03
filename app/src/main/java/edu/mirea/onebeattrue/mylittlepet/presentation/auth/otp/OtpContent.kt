package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomConfirmButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomResendButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OtpContent(
    modifier: Modifier = Modifier,
    component: OtpComponent
) {
    val state by component.model.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    if (state.isFailure) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = state.failureMessage,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
        }
    } else if (state.wasResent) {
        val message = stringResource(id = R.string.code_was_resent)
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                    containerColor = if (state.isFailure) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        SnackbarDefaults.color
                    },
                    contentColor = if (state.isFailure) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        SnackbarDefaults.contentColor
                    },
                    dismissActionContentColor = if (state.isFailure) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        SnackbarDefaults.contentColor
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CustomCardExtremeElevation {

                    Text(
                        text = stringResource(id = R.string.enter_confirmation_code),
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        enabled = state.isEnabled,
                        value = state.otp,
                        onValueChange = {
                            component.onOtpChanged(it)
                        },
                        isError = state.isIncorrect,
                        supportingText = {
                            if (state.isIncorrect) {
                                Text(text = stringResource(id = R.string.error_confirmation_code))
                            }
                        },
                        trailingIcon = {
                            if (state.isIncorrect) {
                                Icon(
                                    imageVector = Icons.Rounded.Warning,
                                    contentDescription = null
                                )
                            }
                        },
                        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = {
                            Text(stringResource(id = R.string.confirmation_code_hint))
                        },
                    )

                    CustomConfirmButton(
                        onClick = {
                            component.onConfirmPhone(
                                otp = state.otp
                            )
                        },
                        enabled = state.isEnabled
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) LinearProgressIndicator()
            }
            CustomResendButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                component.onResendClicked()
            }
        }
    }
}