package edu.mirea.onebeattrue.mylittlepet.ui.customview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CustomNextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        CustomButton(
            onClick = { onClick() },
            text = stringResource(id = R.string.next),
            enabled = enabled
        )
    }
}

@Composable
fun CustomConfirmButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        CustomButton(
            onClick = { onClick() },
            text = stringResource(id = R.string.confirm),
            enabled = enabled
        )
    }
}

@Composable
fun CustomBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        CustomOutlinedButton(
            onClick = { onClick() },
            text = stringResource(id = R.string.back),
            enabled = enabled
        )
    }
}

@Composable
fun CustomAddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        CustomButton(
            onClick = { onClick() },
            text = stringResource(id = R.string.add),
            enabled = enabled
        )
    }
}

@Composable
fun CustomReadyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        CustomButton(
            onClick = { onClick() },
            text = stringResource(id = R.string.ready),
            enabled = enabled
        )
    }
}

@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    text: String,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        colors = colors
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(4.dp))
        icon()
    }
}

@Composable
fun CustomProgressButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    inProgress: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        enabled = !inProgress && enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        AnimatedVisibility(visible = inProgress) {
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    Modifier.size(24.dp)
                )
            }
        }
    }
}