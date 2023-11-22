package edu.mirea.onebeattrue.mylittlepet.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoMap
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.AuthViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.ProfileViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.main.MainViewModel

@DisableInstallInCheck
@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    @Binds
    fun bindAuthViewModel(impl: AuthViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Binds
    fun bindProfileViewModel(impl: ProfileViewModel): ViewModel


    // ... добавлять аналогичные методы для других ViewModel'ей
}