package edu.mirea.onebeattrue.mylittlepet.data.pets

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petListDao: PetListDao,
    private val mapper: PetMapper
) : PetRepository {
    override suspend fun addPet(pet: Pet) {
        petListDao.addPet(
            mapper.mapEntityToDbModel(pet)
        )
    }

    override suspend fun deletePet(pet: Pet) {
        petListDao.deletePet(pet.id)
    }

    override suspend fun editPet(pet: Pet) {
        addPet(pet)
    }

    override fun getList(): Flow<List<Pet>> = petListDao.getPetList().map {
        mapper.mapListDbModelToListEntity(it)
    }
}