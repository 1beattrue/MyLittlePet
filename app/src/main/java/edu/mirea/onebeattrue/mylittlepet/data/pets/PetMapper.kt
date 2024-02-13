package edu.mirea.onebeattrue.mylittlepet.data.pets

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import javax.inject.Inject

class PetMapper @Inject constructor() {
    fun mapEntityToDbModel(entity: Pet): PetDbModel = PetDbModel(
        type = entity.type,
        name = entity.name,
        picture = entity.picture,
        id = entity.id
    )

    fun mapDbModelToEntity(dbModel: PetDbModel): Pet = Pet(
        type = dbModel.type,
        name = dbModel.name,
        picture = dbModel.picture,
        id = dbModel.id
    )

    fun mapListDbModelToListEntity(
        listDbModel: List<PetDbModel>
    ): List<Pet> = listDbModel.map { petDbModel ->
        mapDbModelToEntity(petDbModel)
    }
}