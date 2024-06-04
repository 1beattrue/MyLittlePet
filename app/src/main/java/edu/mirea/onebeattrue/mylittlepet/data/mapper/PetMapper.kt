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
import javax.inject.Inject

class PetMapper @Inject constructor(
    private val application: Application
) {
    fun mapPetEntityToDbModel(entity: Pet): PetDbModel = PetDbModel(
        id = entity.id,
        type = entity.type,
        name = entity.name,
        image = mapUriToImage(entity.imageUri.toUri()),
        dateOfBirth = entity.dateOfBirth,
        weight = entity.weight
    )

    fun mapPetDbModelToEntity(dbModel: FullPetDbModel): Pet = Pet(
        type = dbModel.petDbModel.type,
        name = dbModel.petDbModel.name,
        imageUri = mapImageToUri(
            dbModel.petDbModel.image,
            dbModel.petDbModel.id
        ).toString(),
        id = dbModel.petDbModel.id,
        dateOfBirth = dbModel.petDbModel.dateOfBirth,
        weight = dbModel.petDbModel.weight,
        eventList = dbModel.eventList.map { mapEventDbModelToEntity(it) },
        noteList = dbModel.noteList.map { mapNoteDbModelToEntity(it) },
        medicalDataList = dbModel.medicalDataList.map { mapMedicalDataDbModelToEntity(it) }
    )

    fun mapListDbModelToListEntity(
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

    fun mapMedicalDataEntityToDbModel(entity: MedicalData): MedicalDataDbModel =
        MedicalDataDbModel(
            id = entity.id,
            petId = entity.petId,
            type = entity.type,
            image = mapUriToImage(entity.imageUri.toUri()),
            text = entity.text
        )

    fun mapMedicalDataDbModelToEntity(dbModel: MedicalDataDbModel): MedicalData =
        MedicalData(
            id = dbModel.id,
            petId = dbModel.petId,
            type = dbModel.type,
            imageUri = mapImageToUri(
                image = dbModel.image,
                uniqueId = dbModel.id
            ).toString(),
            text = dbModel.text
        )


    private fun mapImageToUri(image: ByteArray?, uniqueId: Int): Uri {
        return image?.let { ImageUtils.saveImageToInternalStorage(application, it, uniqueId) }
            ?: Uri.EMPTY
    }

    private fun mapUriToImage(uri: Uri): ByteArray? {
        return ImageUtils.uriToByteArray(application, uri)
    }
}