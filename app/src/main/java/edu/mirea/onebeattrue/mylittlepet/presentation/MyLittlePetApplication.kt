package edu.mirea.onebeattrue.mylittlepet.presentation

import android.app.Application
import edu.mirea.onebeattrue.mylittlepet.di.DaggerApplicationComponent

class MyLittlePetApplication : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}