package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDateContent(
    modifier: Modifier = Modifier,
    component: EventDateComponent
) {
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCardExtremeElevation {
            Text(
                text = stringResource(id = R.string.set_event_date_title),
                style = MaterialTheme.typography.titleLarge
            )

            DatePicker(
                title = {},
                state = datePickerState
            )

            val confirmEnabled by derivedStateOf {
                datePickerState.selectedDateMillis != null
            }
            CustomNextButton(
                enabled = confirmEnabled,
                onClick = { component.finish(datePickerState.selectedDateMillis!!) }
            )
        }
    }
}