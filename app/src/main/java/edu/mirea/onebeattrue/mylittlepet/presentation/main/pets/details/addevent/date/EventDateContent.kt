package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            title = {},
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        val confirmEnabled by derivedStateOf {
            datePickerState.selectedDateMillis != null
        }
        CustomNextButton(
            modifier = Modifier.padding(end = 16.dp),
            enabled = confirmEnabled,
            onClick = { component.finish(datePickerState.selectedDateMillis!!) }
        )
    }
}