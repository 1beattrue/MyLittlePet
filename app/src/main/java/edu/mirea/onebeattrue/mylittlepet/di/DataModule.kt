package edu.mirea.onebeattrue.mylittlepet.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.mylittlepet.data.auth.AuthRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.data.database.AppDatabase
import edu.mirea.onebeattrue.mylittlepet.data.pets.AlarmSchedulerImpl
import edu.mirea.onebeattrue.mylittlepet.data.pets.PetListDao
import edu.mirea.onebeattrue.mylittlepet.data.pets.PetRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @ApplicationScope
    @Binds
    fun bindPetRepository(impl: PetRepositoryImpl): PetRepository

    companion object {
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