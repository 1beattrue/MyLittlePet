package edu.mirea.onebeattrue.mylittlepet.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.EnterPhoneScreenState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.state.InvalidPhoneNumberException
import edu.mirea.onebeattrue.mylittlepet.presentation.MainActivity
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.EnterPhoneViewModel
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EnterPhoneScreen(
    modifier: Modifier = Modifier,
    nextScreen: () -> Unit,
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

    var progress by rememberSaveable {
        mutableStateOf(false)
    }

    val snackbarHostState = SnackbarHostState()
    // ---------------------------------------------------------------------------------------------

    val scope = rememberCoroutineScope()
    val viewModel: EnterPhoneViewModel = viewModel(factory = viewModelFactory)

    val enterPhoneScreenState by viewModel.enterPhoneScreenState.collectAsState(
        EnterPhoneScreenState.Initial
    )
    Log.d("EnterPhoneScreen", enterPhoneScreenState.toString())
    when (val screenState = enterPhoneScreenState) {
        is EnterPhoneScreenState.Failure -> {
            scope.launch {
                progress = false

                if (screenState.exception is InvalidPhoneNumberException) {
                    isPhoneTextFieldError.value = true
                } else {
                    snackbarHostState.showSnackbar(
                        message = screenState.exception.message.toString(),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }

        EnterPhoneScreenState.Loading -> {
            progress = true
        }

        EnterPhoneScreenState.Success -> {
            progress = false
            nextScreen()
        }

        EnterPhoneScreenState.Initial -> {
            progress = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(16.dp),
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
            Card(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp
                    )
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 32.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 24.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.enter_phone_number),
                        fontSize = 24.sp
                    )
                    PhoneTextField(
                        modifier = Modifier.fillMaxWidth(),
                        phoneNumber = phoneNumber,
                        isError = isPhoneTextFieldError
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                viewModel.createUserWithPhone(phoneNumber.value, activity)
                            },
                            shape = RoundedCornerShape(16.dp),
                            enabled = !progress
                        ) {
                            Text(
                                text = stringResource(id = R.string.next),
                                fontSize = 16.sp
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (progress) LinearProgressIndicator()
        }
    }
}

@Composable
private fun PhoneTextField(
    modifier: Modifier = Modifier,
    phoneNumber: MutableState<String>,
    isError: MutableState<Boolean>
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
        shape = RoundedCornerShape(16.dp),
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
        }
    )
}

//@Preview
//@Composable
//private fun EnterPhoneScreenPreviewLight() {
//    MyLittlePetTheme(darkTheme = false) {
//        EnterPhoneScreen()
//    }
//}
//
//@Preview
//@Composable
//private fun EnterPhoneScreenPreviewDark() {
//    MyLittlePetTheme(darkTheme = true) {
//        EnterPhoneScreen()
//    }
//}