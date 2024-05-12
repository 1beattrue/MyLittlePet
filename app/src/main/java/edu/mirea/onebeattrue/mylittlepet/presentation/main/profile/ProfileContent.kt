package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomLanguageSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomThemeSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DEFAULT_ELEVATION
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    component: ProfileComponent
) {
    val state by component.model.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = stringResource(id = R.string.privacy_policy))
                Text(text = stringResource(id = R.string.privacy_policy_content))
            }
        },
        sheetPeekHeight = 96.dp,
    ) {
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
                    CustomThemeSwitcher(
                        modifier = modifier,
                        isDarkTheme = state.isDarkTheme,
                        changeTheme = {
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
                        isEnglishLanguage = state.isEnglishLanguage,
                        changeLanguage = {
                            component.changeLanguage(it)
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
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
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
                onClick = { /* TODO() */ }
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
    }
}