package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import edu.mirea.onebeattrue.mylittlepet.ui.theme.ROUNDED_CORNER_SIZE_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.SMALL_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelFactory,
    close: () -> Unit
) {
    val viewModel: PetsViewModel = viewModel(factory = viewModelFactory)
    val petTypes = listOf(
        PetType.DOG,
        PetType.CAT,
        PetType.RABBIT,
        PetType.BIRD,
        PetType.FISH,
        PetType.SNAKE,
        PetType.TIGER,
        PetType.MOUSE,
        PetType.TURTLE
    ).map { it.getName() }

    val context = LocalContext.current.applicationContext

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedText by rememberSaveable { mutableStateOf(context.getString(R.string.pet_type_choose)) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
            elevation = CardDefaults.cardElevation(defaultElevation = SMALL_ELEVATION)
        ) {
            Box(
                modifier = Modifier
                    .padding(32.dp)

            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        value = selectedText,
                        shape = RoundedCornerShape(16.dp),
                        onValueChange = {
                                        // TODO(): выбранный элемент обработать тут
                        },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        petTypes.forEach { petType ->
                            DropdownMenuItem(
                                text = { Text(text = petType) },
                                onClick = {
                                    selectedText = petType
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

        }

    }

}

@Preview
@Composable
fun AddPetScreenPreviewLight() {
    MyLittlePetTheme(
        darkTheme = false
    ) {
        AddPetScreen(viewModelFactory = ViewModelFactory(mapOf())) {

        }
    }
}

@Preview
@Composable
fun AddPetScreenPreviewDark() {
    MyLittlePetTheme(
        darkTheme = true
    ) {
        AddPetScreen(viewModelFactory = ViewModelFactory(mapOf())) {

        }
    }
}