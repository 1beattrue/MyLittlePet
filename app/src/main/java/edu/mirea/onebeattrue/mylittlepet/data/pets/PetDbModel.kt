package edu.mirea.onebeattrue.mylittlepet.data.pets

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetDbModel(
    val type: String,
    val name: String,
    val picture: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)