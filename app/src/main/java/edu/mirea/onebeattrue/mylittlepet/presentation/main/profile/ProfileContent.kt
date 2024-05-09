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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomLanguageSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomThemeSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DEFAULT_ELEVATION

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
            text = "profile component"
        )
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
        CustomCard(
            elevation = DEFAULT_ELEVATION,
            onClick = {

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