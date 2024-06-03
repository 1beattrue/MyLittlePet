package edu.mirea.onebeattrue.mylittlepet.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

@Entity(tableName = "pets")
data class PetDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val type: PetType,
    val name: String,
    val image: ByteArray?,
    val dateOfBirth: Long?,
    val weight: Float?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PetDbModel

        if (id != other.id) return false
        if (type != other.type) return false
        if (name != other.name) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + (dateOfBirth?.hashCode() ?: 0)
        result = 31 * result + (weight?.hashCode() ?: 0)
        return result
    }
}