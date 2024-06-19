package edu.mirea.onebeattrue.mylittlepet.data.mapper

import edu.mirea.onebeattrue.mylittlepet.data.local.model.MedicalDataDbModel
import edu.mirea.onebeattrue.mylittlepet.data.network.dto.MedicalDataDto
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType

suspend fun MedicalData.mapEntityToDbModel(imageMapper: ImageMapper): MedicalDataDbModel =
    MedicalDataDbModel(
        id = id,
        petId = petId,
        type = type,
        imageBase64 = imageMapper.mapUriToBase64(imageUri),
        text = text
    )

suspend fun MedicalDataDbModel.mapDbModelToEntity(imageMapper: ImageMapper): MedicalData =
    MedicalData(
        id = id,
        petId = petId,
        type = type,
        imageUri = imageMapper.mapBase64ToUri(imageBase64, id),
        text = text
    )

suspend fun MedicalData.mapEntityToDto(imageMapper: ImageMapper): MedicalDataDto = MedicalDataDto(
    id = id,
    petId = petId,
    type = type.mapToString(),
    imageUrl = imageMapper.mapUriToBase64(imageUri),
    text = text
)

suspend fun MedicalDataDto.mapDtoToEntity(imageMapper: ImageMapper): MedicalData = MedicalData(
    id = id,
    petId = petId,
    type = type.mapToMedicalDataType(),
    imageUri = imageMapper.mapBase64ToUri(imageUrl, id),
    text = text
)

suspend fun List<MedicalDataDbModel>.mapDbModelListToEntities(imageMapper: ImageMapper): List<MedicalData> =
    map { it.mapDbModelToEntity(imageMapper) }

suspend fun List<MedicalDataDto>.mapDtoListToEntities(imageMapper: ImageMapper): List<MedicalData> =
    map { it.mapDtoToEntity(imageMapper) }

private fun MedicalDataType.mapToString(): String = this.name

private fun String.mapToMedicalDataType(): MedicalDataType {
    return when (this) {
        MedicalDataType.VACCINATION.name -> MedicalDataType.VACCINATION
        MedicalDataType.ANALYSIS.name -> MedicalDataType.ANALYSIS
        MedicalDataType.ALLERGY.name -> MedicalDataType.ALLERGY
        MedicalDataType.TREATMENT.name -> MedicalDataType.TREATMENT
        MedicalDataType.OTHER.name -> MedicalDataType.OTHER
        else -> {
            throw RuntimeException("Unknown medical data type")
        }
    }
}