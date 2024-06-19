package edu.mirea.onebeattrue.mylittlepet.data.local.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.mirea.onebeattrue.mylittlepet.data.local.model.EventDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.MedicalDataDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.NoteDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.PetDbModel

@Database(
    entities = [
        PetDbModel::class,
        EventDbModel::class,
        NoteDbModel::class,
        MedicalDataDbModel::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun eventDao(): EventDao
    abstract fun noteDao(): NoteDao
    abstract fun medicalDataDao(): MedicalDataDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "app_database.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) { // double check
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}