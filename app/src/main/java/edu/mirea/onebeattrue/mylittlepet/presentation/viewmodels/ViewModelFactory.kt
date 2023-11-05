package edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.mirea.onebeattrue.mylittlepet.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScope
class ViewModelFactory @Inject constructor(
    private val viewModels: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModels[modelClass]?.get() as T
    }
}