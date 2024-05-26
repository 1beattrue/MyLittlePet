package edu.mirea.onebeattrue.mylittlepet.presentation.utils

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import edu.mirea.onebeattrue.mylittlepet.presentation.MyLittlePetApplication
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DATA_STORE_SETTINGS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale

val ComponentContext.componentScope
    get() = CoroutineScope(
        Dispatchers.Main.immediate + SupervisorJob()
    ).apply {
        lifecycle.doOnDestroy { this.cancel() }
    }


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_SETTINGS)

enum class Language(val value: String) {
    RU("ru"), EN("en")
}

object LocaleUtils {
    fun isEnglishLanguage(): Boolean {
        val currentLocale = Locale.getDefault()
        return currentLocale.language == Locale.ENGLISH.language
    }

    fun setLocale(context: Context, isEnglish: Boolean?) {
        val language = if (isEnglish == null || isEnglish) Language.EN.value else Language.RU.value
        updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String) {
        context.resources.apply {
            val locale = Locale(language)
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, displayMetrics)
        }
    }
}

object UiUtils {
    fun isSystemInDarkTheme(context: Context): Boolean {
        val uiMode = context.resources.configuration.uiMode
        val nightModeFlags = uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}

object DataStoreUtils {
    fun getLastSavedBoolean(application: Application, key: Preferences.Key<Boolean>): Boolean? {
        val dataStore = (application as MyLittlePetApplication).dataStore
        return runBlocking {
            val preferences = dataStore.data.first()
            preferences[key]
        }
    }
}
