package edu.mirea.onebeattrue.mylittlepet.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType

@Entity(
    tableName = "medical_data",
    foreignKeys = [ForeignKey(
        entity = PetDbModel::class,
        parentColumns = ["id"],
        childColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MedicalDataDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val petId: Int,

    val type: MedicalDataType,
    val image: ByteArray?,
    val text: String
)