package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MENU_ITEM_PADDING


@Composable
fun MedicalTypeContent(
    modifier: Modifier = Modifier,
    component: MedicalTypeComponent
) {
    val state by component.model.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomCardExtremeElevation {

            Text(
                text = stringResource(id = R.string.select_medical_type),
                style = MaterialTheme.typography.titleLarge
            )

            SelectedTypeField(
                expanded = state.expanded,
                selectedMedicalDataType = state.medicalDataType,
                isIncorrect = state.isIncorrect,
                changeExpanded = {
                    component.changeDropdownMenuExpanded(it)
                }
            ) { medicalDataType ->
                component.setType(medicalDataType)
            }

            CustomNextButton(onClick = { component.next() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectedTypeField(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    selectedMedicalDataType: MedicalDataType?,
    isIncorrect: Boolean,
    changeExpanded: (Boolean) -> Unit,
    selectMedicalDataType: (MedicalDataType) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(
                    CORNER_RADIUS_CONTAINER
                )
            )
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { changeExpanded(!expanded) }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedMedicalDataType?.getName()
                        ?: stringResource(id = R.string.initial_medical_type),
                    shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                    onValueChange = { /* :) */ },
                    readOnly = true,
                    trailingIcon = {
                        if (isIncorrect) {
                            Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                        }
                    },
                    isError = isIncorrect,
                    supportingText = {
                        if (isIncorrect) {
                            Text(text = stringResource(id = R.string.error_medical_type))
                        }
                    },
                    leadingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { changeExpanded(false) }
                ) {
                    MedicalDataType.getTypes().forEach { medicalDataType ->
                        val medicalDataTypeName = medicalDataType.getName()
                        DropdownMenuItem(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER)),
                            contentPadding = MENU_ITEM_PADDING,
                            text = {
                                Text(
                                    text = medicalDataTypeName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                selectMedicalDataType(medicalDataType)
                                changeExpanded(false)
                            }
                        )
                    }
                }
            }
        }
    }
}