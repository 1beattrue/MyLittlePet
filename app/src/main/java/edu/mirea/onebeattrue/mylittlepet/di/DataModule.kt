package edu.mirea.onebeattrue.mylittlepet.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import edu.mirea.onebeattrue.mylittlepet.data.auth.AuthRepositoryImpl
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}