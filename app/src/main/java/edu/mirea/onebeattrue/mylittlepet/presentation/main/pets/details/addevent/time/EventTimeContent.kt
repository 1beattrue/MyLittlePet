package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTimeContent(
    modifier: Modifier = Modifier,
    component: EventTimeComponent
) {
    val state by component.model.collectAsState()

    val timePickerState = rememberTimePickerState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCardExtremeElevation {

            Text(
                text = stringResource(id = R.string.set_event_time_title),
                style = MaterialTheme.typography.titleLarge
            )

            TimePicker(state = timePickerState)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.isDaily,
                    onCheckedChange = {
                        component.onPeriodChanged(it)
                    }
                )
                Text(
                    modifier = Modifier.clickable {
                        component.onPeriodChanged(!state.isDaily)
                    },
                    text = stringResource(id = R.string.daily_checkbox),
                )
            }


            CustomNextButton(
                onClick = { component.next(timePickerState.hour, timePickerState.minute) }
            )
        }
    }
}