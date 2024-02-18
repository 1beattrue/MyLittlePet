package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import android.annotation.SuppressLint
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.InvalidPhoneNumberException
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.InvalidVerificationCodeException
import edu.mirea.onebeattrue.mylittlepet.presentation.MainActivity
import edu.mirea.onebeattrue.mylittlepet.presentation.SmsReceiver
import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomBackButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    finishAuth: () -> Unit,
    viewModelFactory: ViewModelFactory,
    activity: MainActivity
) {
    // states --------------------------------------------------------------------------------------
    val phoneNumber = rememberSaveable {
        mutableStateOf("")
    }

    val isPhoneTextFieldError = rememberSaveable {
        mutableStateOf(false)
    }

    val code = rememberSaveable {
        mutableStateOf("")
    }

    val isConfirmPhoneTextFieldError = rememberSaveable {
        mutableStateOf(false)
    }

    var progress by rememberSaveable {
        mutableStateOf(false)
    }

    var isCodeSent by rememberSaveable {
        mutableStateOf(false)
    }

    val snackbarHostState = SnackbarHostState()

    var backHandlingEnabled by rememberSaveable {
        mutableStateOf(false)
    }
    // ---------------------------------------------------------------------------------------------

    val scope = rememberCoroutineScope()
    val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    val authScreenState by viewModel.screenState.collectAsState(
        AuthScreenState.Initial
    )

    SmsReceiver(systemAction = Telephony.Sms.Intents.SMS_RECEIVED_ACTION) { intent ->
        intent?.let { smsIntent ->
            val smsMessages: Array<SmsMessage>? =
                Telephony.Sms.Intents.getMessagesFromIntent(smsIntent)
            smsMessages?.forEach { smsMessage ->
                val messageBody: String = smsMessage.messageBody
                viewModel.parseConfirmationCode(messageBody)?.let {
                    code.value = it
                    if (isCodeSent) {
                        viewModel.signUpWithCredential(
                            code = code.value
                        )
                    }
                }
            }
        }
    }

    when (val screenState = authScreenState) {
        is AuthScreenState.Failure -> {
            scope.launch {
                progress = false

                when (screenState.exception) {
                    is InvalidPhoneNumberException -> {
                        isPhoneTextFieldError.value = true
                    }

                    is InvalidVerificationCodeException -> {
                        isConfirmPhoneTextFieldError.value = true
                    }

                    else -> {
                        snackbarHostState.showSnackbar(
                            message = screenState.exception.message.toString(),
                            duration = SnackbarDuration.Long,
                            withDismissAction = true
                        )
                    }
                }
            }
        }

        AuthScreenState.Loading -> {
            progress = true
        }

        AuthScreenState.Success -> {
            progress = false
            finishAuth()
        }

        AuthScreenState.CodeSent -> {
            progress = false
            isCodeSent = true
            backHandlingEnabled = true
        }

        AuthScreenState.Initial -> {
            progress = false
            isCodeSent = false
            backHandlingEnabled = false

            code.value = ""
            isConfirmPhoneTextFieldError.value = false
        }
    }

    BackHandler(backHandlingEnabled) {
        viewModel.changePhoneNumber()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.image_cat_face),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomCardDefaultElevation {
                AnimatedVisibility(
                    visible = !isCodeSent,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_phone_number),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                PhoneTextField(
                    modifier = Modifier.fillMaxWidth(),
                    phoneNumber = phoneNumber,
                    isError = isPhoneTextFieldError,
                    isEnabled = !isCodeSent && !progress
                )
                AnimatedVisibility(
                    visible = isCodeSent,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_confirmation_code),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                AnimatedVisibility(
                    visible = isCodeSent,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    ConfirmPhoneTextField(
                        modifier = Modifier.fillMaxWidth(),
                        code = code,
                        isError = isConfirmPhoneTextFieldError,
                        isEnabled = !progress
                    )
                }
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    CustomButton(
                        onClick = {
                            if (!isCodeSent) {
                                viewModel.createUserWithPhone(
                                    phoneNumber = phoneNumber.value,
                                    activity = activity
                                )
                            } else {
                                viewModel.signUpWithCredential(
                                    code = code.value
                                )
                            }
                        },
                        text = stringResource(
                            id = if (!isCodeSent) R.string.next else R.string.confirm
                        ),
                        enabled = !progress
                    )
                    Column {
                        AnimatedVisibility(
                            visible = isCodeSent,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            CustomBackButton(
                                onClick = { viewModel.changePhoneNumber() },
                                enabled = !progress
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = isCodeSent && !progress,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            viewModel.resendVerificationCode(
                                phoneNumber = phoneNumber.value,
                                activity = activity
                            )
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
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = progress,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                LinearProgressIndicator()
            }
        }
    }
}

@Composable
private fun PhoneTextField(
    modifier: Modifier = Modifier,
    phoneNumber: MutableState<String>,
    isError: MutableState<Boolean>,
    isEnabled: Boolean
) {
    OutlinedTextField(
        modifier = modifier,
        value = phoneNumber.value,
        onValueChange = {
            phoneNumber.value = it.filter { symbol -> symbol.isDigit() }
            isError.value = false
        },
        label = {
            Text(stringResource(id = R.string.phone_number_hint))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        singleLine = true,
        prefix = {
            Text(stringResource(id = R.string.phone_number_prefix))
        },
        isError = isError.value,
        supportingText = {
            if (isError.value) {
                Text(text = stringResource(id = R.string.error_phone_number))
            }
        },
        trailingIcon = {
            if (isError.value) {
                Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
            }
        },
        enabled = isEnabled
    )
}

@Composable
private fun ConfirmPhoneTextField(
    modifier: Modifier = Modifier,
    code: MutableState<String>,
    isError: MutableState<Boolean>,
    isEnabled: Boolean
) {
    OutlinedTextField(
        modifier = modifier,
        value = code.value,
        onValueChange = {
            code.value = it.filter { symbol -> symbol.isDigit() }
            isError.value = false
        },
        label = {
            Text(stringResource(id = R.string.confirmation_code_hint))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        singleLine = true,
        isError = isError.value,
        supportingText = {
            if (isError.value) {
                Text(text = stringResource(id = R.string.error_confirmation_code))
            }
        },
        trailingIcon = {
            if (isError.value) {
                Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
            }
        },
        enabled = isEnabled
    )
}