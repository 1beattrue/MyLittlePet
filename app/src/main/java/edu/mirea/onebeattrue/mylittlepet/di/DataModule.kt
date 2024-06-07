package edu.mirea.onebeattrue.mylittlepet.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.mylittlepet.data.local.db.AppDatabase
import edu.mirea.onebeattrue.mylittlepet.data.local.db.PetListDao
import edu.mirea.onebeattrue.mylittlepet.data.remote.api.ApiFactory
import edu.mirea.onebeattrue.mylittlepet.data.remote.api.PetService
import edu.mirea.onebeattrue.mylittlepet.data.remote.api.UserService
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

    @ApplicationScope
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @ApplicationScope
    @Binds
    fun bindPetRepository(impl: PetRepositoryImpl): PetRepository

    @ApplicationScope
    @Binds
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @ApplicationScope
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @ApplicationScope
    @Binds
    fun bindMedicalDataRepository(impl: MedicalDataRepositoryImpl): MedicalDataRepository

    companion object {
        @ApplicationScope
        @Provides
        fun providePetService(): PetService = ApiFactory.petService

        @ApplicationScope
        @Provides
        fun provideUserService(): UserService = ApiFactory.userService

        @ApplicationScope
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @ApplicationScope
        @Provides
        fun providePetListDao(
            application: Application
        ): PetListDao = AppDatabase.getInstance(application).petListDao()

        @ApplicationScope
        @Provides
        fun provideAlarmScheduler(
            application: Application
        ): AlarmScheduler = AlarmSchedulerImpl(application)
    }
}