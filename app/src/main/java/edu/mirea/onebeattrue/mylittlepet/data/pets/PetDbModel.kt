package edu.mirea.onebeattrue.mylittlepet.data.pets

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

@Entity(tableName = "pets")
data class PetDbModel(
    val type: PetType,
    val name: String,
    val imageUri: Uri,
    @PrimaryKey(autoGenerate = true)
    val id: Int
)