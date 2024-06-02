package edu.mirea.onebeattrue.mylittlepet.data.pets

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.extensions.ImageUtils
import javax.inject.Inject

class PetMapper @Inject constructor(
    private val application: Application
) {
    suspend fun mapEntityToDbModel(entity: Pet): PetDbModel = PetDbModel(
        type = entity.type,
        name = entity.name,
        image = mapUriToImage(entity.imageUri.toUri()),
        id = entity.id,

        dateOfBirth = entity.dateOfBirth,
        weight = entity.weight,
        eventList = entity.eventList,
        noteList = entity.noteList,
        medicalDataList = entity.medicalDataList
    )

    suspend fun mapDbModelToEntity(dbModel: PetDbModel): Pet = Pet(
        type = dbModel.type,
        name = dbModel.name,
        imageUri = mapImageToUri(dbModel.image, dbModel.id).toString(),
        id = dbModel.id,

        dateOfBirth = dbModel.dateOfBirth,
        weight = dbModel.weight,
        eventList = dbModel.eventList,
        noteList = dbModel.noteList,
        medicalDataList = dbModel.medicalDataList
    )

    suspend fun mapListDbModelToListEntity(
        listDbModel: List<PetDbModel>
    ): List<Pet> = listDbModel.map { petDbModel ->
        mapDbModelToEntity(petDbModel)
    }

    suspend fun mapImageToUri(image: ByteArray?, uniqueId: Int): Uri {
        return image?.let { ImageUtils.saveImageToInternalStorage(application, it, uniqueId) }
            ?: Uri.EMPTY
    }

    suspend fun mapUriToImage(uri: Uri): ByteArray? {
        return ImageUtils.uriToByteArray(application, uri)
    }
}