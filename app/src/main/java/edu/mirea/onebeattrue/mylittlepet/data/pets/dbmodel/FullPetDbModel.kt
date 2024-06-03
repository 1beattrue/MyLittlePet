package edu.mirea.onebeattrue.mylittlepet.data.pets.dbmodel

import androidx.room.Embedded
import androidx.room.Relation

data class FullPetDbModel(
    @Embedded val petDbModel: PetDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "petId"
    )
    val eventList: List<EventDbModel>,

    @Relation(
        parentColumn = "id",
        entityColumn = "petId"
    )
    val noteList: List<NoteDbModel>,

    @Relation(
        parentColumn = "id",
        entityColumn = "petId"
    )
    val medicalDataList: List<MedicalDataDbModel>
)