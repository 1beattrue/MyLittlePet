package edu.mirea.onebeattrue.mylittlepet.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import edu.mirea.onebeattrue.mylittlepet.domain.main.MainScreenState
import edu.mirea.onebeattrue.mylittlepet.presentation.screens.main.MainScreen
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.AuthViewModel
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    private val component by lazy {
        (application as MyLittlePetApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        checkPermissions()

        setContent {
            MyLittlePetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    MainScreen(
                        viewModelFactory = viewModelFactory,
                        activity = this,
                        initialScreenState = getInitialScreenState()
                    )
                }
            }
        }
    }

    private fun getInitialScreenState(): MainScreenState {
        if (viewModel.isLoggedIn) return MainScreenState.MainFlow()
        return MainScreenState.AuthFlow()
    }

    private fun checkPermissions() {
        val permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECEIVE_SMS),
            RECEIVE_SMS_REQUEST_CODE
        )
    }

    companion object {
        private const val RECEIVE_SMS_REQUEST_CODE = 228
    }
}