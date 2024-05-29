package edu.mirea.onebeattrue.mylittlepet.data.pets

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petListDao: PetListDao,
    private val mapper: PetMapper,
    private val alarmScheduler: AlarmScheduler
) : PetRepository {
    override suspend fun addPet(pet: Pet) {
        petListDao.addPet(
            mapper.mapEntityToDbModel(pet)
        )
    }

    override suspend fun deletePet(pet: Pet) {
        pet.eventList.forEach { event ->
            alarmScheduler.cancel(
                AlarmItem(
                    time = event.time,
                    title = pet.name,
                    text = event.label,
                    repeatable = event.repeatable
                )
            )
        }

        petListDao.deletePet(pet.id)
    }

    override suspend fun editPet(pet: Pet) {
        addPet(pet)
    }

    override fun getPetList(): Flow<List<Pet>> = petListDao.getPetList().map {
        mapper.mapListDbModelToListEntity(it)
    }

    override fun getPetById(petId: Int): Flow<Pet> = petListDao.getPetById(petId).map {
        mapper.mapDbModelToEntity(it)
    }
}