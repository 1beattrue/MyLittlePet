package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.phone.SmsRetriever
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PhoneContent(
    modifier: Modifier = Modifier,
    component: PhoneComponent
) {
    val state by component.model.collectAsState()

    val context = LocalContext.current
    val activity = context as Activity

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
    }

    remember {
        val client = SmsRetriever.getClient(context)
        val task = client.startSmsUserConsent(null)
        task.addOnSuccessListener {
            Log.d("PhoneContent", "SMS Retriever started successfully")
        }
        task.addOnFailureListener {
            Log.d("PhoneContent", "SMS Retriever start failure")
        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CustomCardExtremeElevation {

                Text(
                    text = stringResource(id = R.string.enter_phone_number),
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    enabled = state.isEnabled,
                    value = state.phone,
                    onValueChange = {
                        component.onPhoneChanged(it)
                    },
                    isError = state.isIncorrect,
                    supportingText = {
                        if (state.isIncorrect) {
                            Text(text = stringResource(id = R.string.error_phone_number))
                        }
                    },
                    trailingIcon = {
                        if (state.isIncorrect) {
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

                CustomNextButton(
                    onClick = {
                        component.onCodeSent(
                            phone = state.phone,
                            activity = activity
                        )
                    },
                    enabled = state.isEnabled
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) LinearProgressIndicator()
        }
    }
}