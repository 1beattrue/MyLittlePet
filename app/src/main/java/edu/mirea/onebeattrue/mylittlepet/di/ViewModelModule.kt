package edu.mirea.onebeattrue.mylittlepet.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoMap
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.ConfirmPhoneViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.EnterPhoneViewModel

@DisableInstallInCheck
@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(EnterPhoneViewModel::class)
    @Binds
    fun bindEnterPhoneViewModel(impl: EnterPhoneViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ConfirmPhoneViewModel::class)
    @Binds
    fun bindConfirmPhoneViewModel(impl: ConfirmPhoneViewModel): ViewModel

    // ... добавлять аналогичные методы для других ViewModel'ей
}