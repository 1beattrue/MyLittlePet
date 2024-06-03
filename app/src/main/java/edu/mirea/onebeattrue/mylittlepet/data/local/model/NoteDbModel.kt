package edu.mirea.onebeattrue.mylittlepet.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = PetDbModel::class,
        parentColumns = ["id"],
        childColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NoteDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val petId: Int,

    val text: String,
    val iconResId: Int,
)