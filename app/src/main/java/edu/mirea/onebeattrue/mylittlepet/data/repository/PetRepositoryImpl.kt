package edu.mirea.onebeattrue.mylittlepet.data.repository

import android.graphics.Bitmap
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import edu.mirea.onebeattrue.mylittlepet.data.local.db.PetDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.ImageMapper
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelListToEntities
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelToEntity
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDtoToDbModel
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDbModel
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDto
import edu.mirea.onebeattrue.mylittlepet.data.network.api.PetApiService
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petListDao: PetDao,
    private val alarmScheduler: AlarmScheduler,
    private val imageMapper: ImageMapper,
    private val petApiService: PetApiService,
    private val authRepository: AuthRepository
) : PetRepository {

    private val lastPetScanned = MutableStateFlow<Pet?>(null)
    private val userToken: String
        get() = authRepository.currentUser?.mapEntityToDto()?.token
            ?: throw RuntimeException("User == null")

    override suspend fun addPet(pet: Pet) {

        val petId = petApiService.createPet(pet.mapEntityToDto(userToken, imageMapper))

        petListDao.addPet(pet.mapEntityToDbModel(imageMapper).copy(id = petId))
    }

    override suspend fun deletePet(pet: Pet) {

        petApiService.deletePet(pet.id)

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

        petApiService.updatePet(pet.id, pet.mapEntityToDto(userToken, imageMapper))

        petListDao.addPet(pet.mapEntityToDbModel(imageMapper))
    }

    override fun getPetList(): Flow<List<Pet>> = petListDao.getPetList().map {
        it.mapDbModelListToEntities(imageMapper)
    }

    override fun getPetById(petId: Int): Flow<Pet> = petListDao.getPetById(petId).map {
        it.mapDbModelToEntity(imageMapper)
    }

    override suspend fun generateQrCode(pet: Pet): Bitmap {
        val petString = Gson().toJson(pet)
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            petString,
            BarcodeFormat.QR_CODE,
            QR_CODE_WIDTH,
            QR_CODE_HEIGHT
        )
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }

    override suspend fun setLastPetScanned(pet: Pet) {
        lastPetScanned.value = pet
    }

    override fun getLastPetScanned(): Flow<Pet?> = lastPetScanned.asStateFlow()

    override suspend fun synchronizeWithServer() {

        val petDtoList = petApiService.getPetsByUserToken(userToken)

        petDtoList.forEach { petDto ->
            petListDao.addPet(petDto.mapDtoToDbModel())
        }
    }

    companion object {
        private const val QR_CODE_WIDTH = 1080
        private const val QR_CODE_HEIGHT = 1080
    }
}