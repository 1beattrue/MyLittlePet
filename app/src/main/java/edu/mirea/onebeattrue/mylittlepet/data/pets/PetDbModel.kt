package edu.mirea.onebeattrue.mylittlepet.data.pets

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
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

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
        .create()

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
        return gson.toJson(eventList)
    }

    @TypeConverter
    fun toEventList(eventListString: String): List<Event> {
        val listType = object : TypeToken<List<Event>>() {}.type
        return gson.fromJson(eventListString, listType)
    }

    @TypeConverter
    fun fromNoteList(noteList: List<Note>): String {
        return gson.toJson(noteList)
    }

    @TypeConverter
    fun toNoteList(noteListString: String): List<Note> {
        val listType = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(noteListString, listType)
    }

    @TypeConverter
    fun fromMedicalDataList(medicalDataList: List<MedicalData>): String {
        return gson.toJson(medicalDataList)
    }

    @TypeConverter
    fun toMedicalDataList(medicalDataListString: String): List<MedicalData> {
        val listType = object : TypeToken<List<MedicalData>>() {}.type
        return gson.fromJson(medicalDataListString, listType)
    }
}

class UriTypeAdapter : TypeAdapter<Uri>() {
    override fun write(out: JsonWriter, value: Uri) {
        out.value(value.toString())
    }

    override fun read(`in`: JsonReader): Uri {
        return Uri.parse(`in`.nextString())
    }
}
