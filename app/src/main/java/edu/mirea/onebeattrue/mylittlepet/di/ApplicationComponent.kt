package edu.mirea.onebeattrue.mylittlepet.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import edu.mirea.onebeattrue.mylittlepet.presentation.MainActivity

@ApplicationScope
@Component(
    modules = [DataModule::class, PresentationModule::class]
)
interface ApplicationComponent {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}