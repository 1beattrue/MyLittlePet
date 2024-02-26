package edu.mirea.onebeattrue.mylittlepet.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.profile.ProfileViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.pets.AddPetViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.pets.PetsViewModel

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

    @IntoMap
    @ViewModelKey(PetsViewModel::class)
    @Binds
    fun bindPetsViewModel(impl: PetsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(AddPetViewModel::class)
    @Binds
    fun bindAddPetViewModel(impl: AddPetViewModel): ViewModel

    // ... добавлять аналогичные методы для других ViewModel'ей
}