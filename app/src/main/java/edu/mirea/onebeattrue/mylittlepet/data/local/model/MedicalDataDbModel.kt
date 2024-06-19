package edu.mirea.onebeattrue.mylittlepet.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType

@Entity(
    tableName = "medical_data",
    foreignKeys = [ForeignKey(
        entity = PetDbModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("petId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MedicalDataDbModel(
    @PrimaryKey
    val id: Int,
    val petId: Int,

    val type: MedicalDataType,
    val imageBase64: String?,
    val text: String
)