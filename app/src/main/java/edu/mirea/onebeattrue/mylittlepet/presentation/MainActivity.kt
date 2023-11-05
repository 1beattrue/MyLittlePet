package edu.mirea.onebeattrue.mylittlepet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.mirea.onebeattrue.mylittlepet.presentation.ui.screens.MainScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.ui.theme.MyLittlePetTheme
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as MyLittlePetApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MyLittlePetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModelFactory)
                }
            }
        }
    }
}