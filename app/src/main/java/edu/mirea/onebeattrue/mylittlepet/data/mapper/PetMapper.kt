package edu.mirea.onebeattrue.mylittlepet.data.mapper

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import edu.mirea.onebeattrue.mylittlepet.data.local.model.EventDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.FullPetDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.MedicalDataDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.NoteDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.PetDbModel
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.extensions.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PetMapper @Inject constructor(
    private val application: Application
) {
    suspend fun mapPetEntityToDbModel(entity: Pet): PetDbModel = PetDbModel(
        id = entity.id,
        type = entity.type,
        name = entity.name,
        imageBase64 = mapUriToBase64(entity.imageUri.toUri()),
        dateOfBirth = entity.dateOfBirth,
        weight = entity.weight
    )

    suspend fun mapPetDbModelToEntity(dbModel: FullPetDbModel): Pet = Pet(
        type = dbModel.petDbModel.type,
        name = dbModel.petDbModel.name,
        imageUri = mapBase64ToUri(
            dbModel.petDbModel.imageBase64,
            dbModel.petDbModel.id
        ).toString(),
        id = dbModel.petDbModel.id,
        dateOfBirth = dbModel.petDbModel.dateOfBirth,
        weight = dbModel.petDbModel.weight,
        eventList = dbModel.eventList.map { mapEventDbModelToEntity(it) },
        noteList = dbModel.noteList.map { mapNoteDbModelToEntity(it) },
        medicalDataList = dbModel.medicalDataList.map { mapMedicalDataDbModelToEntity(it) }
    )

    suspend fun mapListDbModelToListEntity(
        listDbModel: List<FullPetDbModel>
    ): List<Pet> = listDbModel.map { petDbModel ->
        mapPetDbModelToEntity(petDbModel)
    }

    fun mapEventEntityToDbModel(entity: Event): EventDbModel = EventDbModel(
        id = entity.id,
        petId = entity.petId,
        time = entity.time,
        label = entity.label,
        repeatable = entity.repeatable
    )

    fun mapEventDbModelToEntity(dbModel: EventDbModel): Event = Event(
        id = dbModel.id,
        petId = dbModel.petId,
        time = dbModel.time,
        label = dbModel.label,
        repeatable = dbModel.repeatable
    )

    fun mapNoteEntityToDbModel(entity: Note): NoteDbModel = NoteDbModel(
        id = entity.id,
        petId = entity.petId,
        text = entity.text,
        iconResId = entity.iconResId
    )

    fun mapNoteDbModelToEntity(dbModel: NoteDbModel): Note = Note(
        id = dbModel.id,
        petId = dbModel.petId,
        text = dbModel.text,
        iconResId = dbModel.iconResId
    )

    suspend fun mapMedicalDataEntityToDbModel(entity: MedicalData): MedicalDataDbModel =
        MedicalDataDbModel(
            id = entity.id,
            petId = entity.petId,
            type = entity.type,
            imageBase64 = mapUriToBase64(entity.imageUri.toUri()),
            text = entity.text
        )

    suspend fun mapMedicalDataDbModelToEntity(dbModel: MedicalDataDbModel): MedicalData =
        MedicalData(
            id = dbModel.id,
            petId = dbModel.petId,
            type = dbModel.type,
            imageUri = mapBase64ToUri(
                image = dbModel.imageBase64,
                uniqueId = dbModel.id
            ).toString(),
            text = dbModel.text
        )


    private suspend fun mapBase64ToUri(image: String?, uniqueId: Int): Uri {
        return withContext(Dispatchers.IO) {
            image?.let { ImageUtils.saveImageToInternalStorage(application, it, uniqueId) }
                ?: Uri.EMPTY
        }
    }

    private suspend fun mapUriToBase64(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            ImageUtils.uriToBase64(application, uri)
        }
    }
}