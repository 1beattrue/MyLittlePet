package edu.mirea.onebeattrue.mylittlepet.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = PetDbModel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("petId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventDbModel(
    @PrimaryKey
    val id: Int,
    val petId: Int,

    val time: Long,
    val label: String,
    val repeatable: Boolean
)