package edu.mirea.onebeattrue.mylittlepet.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.mylittlepet.data.local.db.AppDatabase
import edu.mirea.onebeattrue.mylittlepet.data.local.db.EventDao
import edu.mirea.onebeattrue.mylittlepet.data.local.db.MedicalDataDao
import edu.mirea.onebeattrue.mylittlepet.data.local.db.NoteDao
import edu.mirea.onebeattrue.mylittlepet.data.local.db.PetDao
import edu.mirea.onebeattrue.mylittlepet.data.network.api.ApiFactory
import edu.mirea.onebeattrue.mylittlepet.data.network.api.EventApiService
import edu.mirea.onebeattrue.mylittlepet.data.network.api.MedicalDataApiService
import edu.mirea.onebeattrue.mylittlepet.data.network.api.NoteApiService
import edu.mirea.onebeattrue.mylittlepet.data.network.api.PetApiService
import edu.mirea.onebeattrue.mylittlepet.data.network.api.UserApiService
import edu.mirea.onebeattrue.mylittlepet.data.repository.AlarmSchedulerImpl
import edu.mirea.onebeattrue.mylittlepet.data.repository.AuthRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.data.repository.EventRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.data.repository.MedicalDataRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.data.repository.NoteRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.data.repository.PetRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.MedicalDataRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.NoteRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository

@Module
interface DataModule {

    @[Binds ApplicationScope]
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @[Binds ApplicationScope]
    fun bindPetRepository(impl: PetRepositoryImpl): PetRepository

    @[Binds ApplicationScope]
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @[Binds ApplicationScope]
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @[Binds ApplicationScope]
    fun bindMedicalDataRepository(impl: MedicalDataRepositoryImpl): MedicalDataRepository

    companion object {

        @[Provides ApplicationScope]
        fun provideUserApiService(): UserApiService = ApiFactory.userApiService

        @[Provides ApplicationScope]
        fun providePetApiService(): PetApiService = ApiFactory.petApiService

        @[Provides ApplicationScope]
        fun provideEventApiService(): EventApiService = ApiFactory.eventApiService

        @[Provides ApplicationScope]
        fun provideNoteApiService(): NoteApiService = ApiFactory.noteApiService

        @[Provides ApplicationScope]
        fun provideMedicalDataApiService(): MedicalDataApiService = ApiFactory.medicalDataApiService

        @[Provides ApplicationScope]
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @[Provides ApplicationScope]
        fun providePetDao(
            application: Application
        ): PetDao = AppDatabase.getInstance(application).petDao()

        @[Provides ApplicationScope]
        fun provideEventDao(
            application: Application
        ): EventDao = AppDatabase.getInstance(application).eventDao()

        @[Provides ApplicationScope]
        fun provideNoteDao(
            application: Application
        ): NoteDao = AppDatabase.getInstance(application).noteDao()

        @[Provides ApplicationScope]
        fun provideMedicalDataDao(
            application: Application
        ): MedicalDataDao = AppDatabase.getInstance(application).medicalDataDao()

        @[Provides ApplicationScope]
        fun provideAlarmScheduler(
            application: Application
        ): AlarmScheduler = AlarmSchedulerImpl(application)
    }
}