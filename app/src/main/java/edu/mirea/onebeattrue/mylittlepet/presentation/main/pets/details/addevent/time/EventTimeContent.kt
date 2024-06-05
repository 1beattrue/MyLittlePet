package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomReadyButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTimeContent(
    modifier: Modifier = Modifier,
    component: EventTimeComponent
) {
    val state by component.model.collectAsState()

    val timePickerState = rememberTimePickerState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            CustomCard(
                elevation = EXTREME_ELEVATION,
                innerPadding = PaddingValues(
                    vertical = 32.dp
                )
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 32.dp
                    ),
                    text = stringResource(id = R.string.set_event_time_title),
                    style = MaterialTheme.typography.titleLarge
                )

                TimePicker(state = timePickerState)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER))
                        .clickable {
                            component.onPeriodChanged(!state.isDaily)
                        },
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
                        text = stringResource(id = R.string.daily_checkbox),
                    )
                }


                CustomReadyButton(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onClick = { component.next(timePickerState.hour, timePickerState.minute) }
                )
            }
        }
    }
}