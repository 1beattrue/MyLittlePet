package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomLanguageSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomSwitcher
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomThemeSwitcher

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    component: ProfileComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier
    ) {
        Text(text = "profile component")
        Button(onClick = { component.signOut() }) {
            Text(text = "Log out")
        }
        Spacer(modifier = Modifier.height(4.dp))
        CustomThemeSwitcher(
            isDarkTheme = state.isDarkTheme,
            changeTheme = {
                component.changeTheme(it)
            }
        )
        CustomLanguageSwitcher(
            isEnglishLanguage = state.isEnglishLanguage,
            changeLanguage = {
                component.changeLanguage(it)
            }
        )
    }
}