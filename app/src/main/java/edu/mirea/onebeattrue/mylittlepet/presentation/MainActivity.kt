package edu.mirea.onebeattrue.mylittlepet.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import edu.mirea.onebeattrue.mylittlepet.presentation.root.DefaultRootComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.root.RootContent
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyLittlePetApplication).component.inject(this)
        super.onCreate(savedInstanceState)

        val component = rootComponentFactory.create(
            componentContext =  defaultComponentContext(),
            isDarkTheme = isSystemInDarkTheme()
        )

        setContent {
            MyLittlePetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    RootContent(component = component)
                }
            }
        }
    }

    private fun isSystemInDarkTheme(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}
