package edu.mirea.onebeattrue.mylittlepet.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

@Entity(tableName = "pets")
data class PetDbModel(
    @PrimaryKey
    val id: Int,
    val type: PetType,
    val name: String,
    val imageBase64: String?,
    val dateOfBirth: Long?,
    val weight: Float?
)