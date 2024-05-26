package edu.mirea.onebeattrue.mylittlepet.data.pets

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import javax.inject.Inject

class PetMapper @Inject constructor() {
    fun mapEntityToDbModel(entity: Pet): PetDbModel = PetDbModel(
        type = entity.type,
        name = entity.name,
        imageUri = entity.imageUri,
        id = entity.id,

        dateOfBirth = entity.dateOfBirth,
        weight = entity.weight,
        eventList = entity.eventList,
        noteList = entity.noteList,
        medicalDataList = entity.medicalDataList
    )

    fun mapDbModelToEntity(dbModel: PetDbModel): Pet = Pet(
        type = dbModel.type,
        name = dbModel.name,
        imageUri = dbModel.imageUri,
        id = dbModel.id,

        dateOfBirth = dbModel.dateOfBirth,
        weight = dbModel.weight,
        eventList = dbModel.eventList,
        noteList = dbModel.noteList,
        medicalDataList = dbModel.medicalDataList
    )

    fun mapListDbModelToListEntity(
        listDbModel: List<PetDbModel>
    ): List<Pet> = listDbModel.map { petDbModel ->
        mapDbModelToEntity(petDbModel)
    }
}