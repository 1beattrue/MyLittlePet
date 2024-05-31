package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.Language
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ClickableCustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.STRONG_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    component: ProfileComponent
) {
    val state by component.model.collectAsState()

    if (state.isEnglishLanguage) {
        Any()
    } // TODO: костыль для перерисовки

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    if (state.isEnglishLanguage) {
                        Any()
                    } // TODO: костыль для перерисовки

                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.settings_app_bar_title)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { component.openDialog() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Logout,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ChangeThemeCard(
                    isDarkTheme = state.isDarkTheme ?: isSystemInDarkTheme(),
                    onThemeChanged = { isDarkTheme ->
                        component.changeTheme(isDarkTheme)
                    },
                    useSystemTheme = state.useSystemTheme,
                    onUsingSystemThemeChanged = {
                        component.changeUseSystemTheme(it)
                    }
                )
            }
            item {
                ChangeLanguageCard(
                    isEnglish = state.isEnglishLanguage,
                    onLanguageChanged = { isEnglish ->
                        component.changeLanguage(isEnglish)
                    },
                )
            }
            item {
                ContactCard {
                    component.sendEmail()
                }
            }
            item {
                PrivacyPolicyCard {
                    component.openBottomSheet()
                }
            }
        }

    }

    PrivacyPolicyBottomSheet(
        isExpanded = state.bottomSheetState,
        onCloseBottomSheet = { component.closeBottomSheet() }
    )

    if (state.isLogOutDialogOpen) {
        LogOutDialog(
            onDismissRequest = {
                component.closeDialog()
            },
            onConfirmation = {
                component.closeDialog()
                component.signOut()
            }
        )
    }
}

@Composable
private fun PrivacyPolicyCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.privacy_policy),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
            )
            Icon(
                imageVector = Icons.Rounded.Shield,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ContactCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.contact_us),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,

                )
            Icon(
                imageVector = Icons.Rounded.Mail,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ChangeLanguageCard(
    modifier: Modifier = Modifier,
    isEnglish: Boolean,
    onLanguageChanged: (Boolean) -> Unit,
) {
    CustomCardExtremeElevation(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.lang_title),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.change_language),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = Language.RU.value, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isEnglish,
                onCheckedChange = { english ->
                    onLanguageChanged(english)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = Language.EN.value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ChangeThemeCard(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit,
    useSystemTheme: Boolean,
    onUsingSystemThemeChanged: (Boolean) -> Unit
) {
    CustomCardExtremeElevation(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.theme_title),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER))
                .clickable {
                    onUsingSystemThemeChanged(!useSystemTheme)
                },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = useSystemTheme,
                onCheckedChange = {
                    onUsingSystemThemeChanged(it)
                }
            )
            Text(
                text = stringResource(R.string.use_system_theme),
            )
        }
        AnimatedVisibility(
            visible = !useSystemTheme
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(0.7f),
                    overflow = TextOverflow.Ellipsis,
                    text = stringResource(R.string.change_theme),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_lite_theme),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { darkTheme ->
                        onThemeChanged(darkTheme)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_dark_theme),
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrivacyPolicyBottomSheet(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onCloseBottomSheet: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (isExpanded) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = { onCloseBottomSheet() },
            sheetState = sheetState
        ) {
            LazyColumn(
                //modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 64.dp
                )
            ) {
                item {
                    CustomCard(elevation = STRONG_ELEVATION) {
                        Text(text = stringResource(id = R.string.privacy_policy_content))
                    }
                }
            }
        }
    }
}

@Composable
private fun LogOutDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest() },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Logout,
                contentDescription = null,
            )
        },
        title = {
            Text(text = stringResource(R.string.log_out))
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.log_out_dialog_text),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = stringResource(R.string.log_out),
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}