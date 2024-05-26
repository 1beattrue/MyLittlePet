package edu.mirea.onebeattrue.mylittlepet.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType

fun MedicalDataType.getStringId(): Int = when (this) {
    MedicalDataType.VACCINATION -> R.string.vaccination
    MedicalDataType.ANALYSIS -> R.string.analysis
    MedicalDataType.ALLERGY -> R.string.allergy
    MedicalDataType.TREATMENT -> R.string.treatment
    MedicalDataType.OTHER -> R.string.other
}

@Composable
fun MedicalDataType.getName(): String = stringResource(id = this.getStringId())