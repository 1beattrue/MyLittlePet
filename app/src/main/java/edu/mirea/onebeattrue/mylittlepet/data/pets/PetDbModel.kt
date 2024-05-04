package edu.mirea.onebeattrue.mylittlepet.data.pets

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

@Entity(tableName = "pets")
data class PetDbModel(
    val type: PetType,
    val name: String,
    val imageUri: Uri,

    val dateOfBirth: Long?,
    val weight: Float?,

    val eventList: List<Event>,
    val noteList: List<Note>,
    val medicalDataList: List<MedicalData>,

    @PrimaryKey(autoGenerate = true)
    val id: Int
)

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }

    @TypeConverter
    fun fromEventList(eventList: List<Event>): String {
        return Gson().toJson(eventList)
    }

    @TypeConverter
    fun toEventList(eventListString: String): List<Event> {
        val listType = object : TypeToken<List<Event>>() {}.type
        return Gson().fromJson(eventListString, listType)
    }

    @TypeConverter
    fun fromNoteList(noteList: List<Note>): String {
        return Gson().toJson(noteList)
    }

    @TypeConverter
    fun toNoteList(noteListString: String): List<Note> {
        val listType = object : TypeToken<List<Note>>() {}.type
        return Gson().fromJson(noteListString, listType)
    }

    @TypeConverter
    fun fromMedicalDataList(medicalDataList: List<MedicalData>): String {
        return Gson().toJson(medicalDataList)
    }

    @TypeConverter
    fun toMedicalDataList(medicalDataListString: String): List<MedicalData> {
        val listType = object : TypeToken<List<MedicalData>>() {}.type
        return Gson().fromJson(medicalDataListString, listType)
    }
}
