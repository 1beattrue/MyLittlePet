package edu.mirea.onebeattrue.mylittlepet.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory

@ApplicationScope
@Component(
    modules = [DataModule::class, ViewModelModule::class]
)
interface ApplicationComponent {
    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}