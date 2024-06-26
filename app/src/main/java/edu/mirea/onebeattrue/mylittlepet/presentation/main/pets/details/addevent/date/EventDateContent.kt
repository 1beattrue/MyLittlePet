package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDateContent(
    modifier: Modifier = Modifier,
    component: EventDateComponent
) {
    val datePickerState = rememberDatePickerState()

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
                    text = stringResource(id = R.string.set_event_date_title),
                    style = MaterialTheme.typography.titleLarge
                )
                DatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    state = datePickerState
                )
                val confirmEnabled by derivedStateOf {
                    datePickerState.selectedDateMillis != null
                }
                CustomNextButton(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    enabled = confirmEnabled,
                    onClick = { component.finish(datePickerState.selectedDateMillis!!) }
                )
            }
        }
    }
}