package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardStrongElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomLanguageSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DEFAULT_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    component: ProfileComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.navigation_item_profile)
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomCardDefaultElevation {
            Row {
                Text(text = stringResource(id = R.string.change_theme))
                CustomSwitcher(
                    modifier = modifier,
                    booleanState = state.isDarkTheme,
                    action = {
                        component.changeTheme(it)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        CustomCardDefaultElevation {
            Row {
                Text(text = stringResource(id = R.string.change_language))
                CustomLanguageSwitcher(
                    modifier = modifier,
                    booleanState = state.isEnglishLanguage,
                    action = {
                        component.changeLanguage(!state.isEnglishLanguage)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        CustomCard(
            elevation = DEFAULT_ELEVATION,
            onClick = {
                component.sendEmail()
            }
        ) {
            Row {
                Text(text = stringResource(id = R.string.contact_us))
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Icon")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        CustomCard(
            elevation = DEFAULT_ELEVATION,
            onClick = {
                component.openBottomSheet()
            }
        ) {
            Row {
                Text(text = stringResource(id = R.string.privacy_policy))
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Icon")
            }
        }
        Spacer(modifier = Modifier.height(4.dp))



        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                Log.d("ProfileContent", "${state.bottomSheetState}")
            /* TODO() */
            }
        ) {
            Text(text = stringResource(id = R.string.delete_all_data))
        }
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = { component.signOut() }
        ) {
            Text(text = stringResource(id = R.string.log_out))
        }
    }

    PrivacyPolicyBottomSheet(
        isExpanded = state.bottomSheetState,
        closeBottomSheet = { component.closeBottomSheet() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrivacyPolicyBottomSheet(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    closeBottomSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (isExpanded) {
        ModalBottomSheet(
            onDismissRequest = { closeBottomSheet() },
            sheetState = sheetState
        ) {
            CustomCardStrongElevation {
                Text(text = stringResource(id = R.string.privacy_policy))
                Text(text = stringResource(id = R.string.privacy_policy_content))
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}