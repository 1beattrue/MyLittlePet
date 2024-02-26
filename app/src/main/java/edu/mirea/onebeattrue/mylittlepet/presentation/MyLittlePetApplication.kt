package edu.mirea.onebeattrue.mylittlepet.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import edu.mirea.onebeattrue.mylittlepet.di.ApplicationComponent
import edu.mirea.onebeattrue.mylittlepet.di.DaggerApplicationComponent

class MyLittlePetApplication : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("ApplicationComponent", "getApplicationComponent")
    return (LocalContext.current.applicationContext as MyLittlePetApplication).component
}