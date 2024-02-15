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
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.domain.main.entity.MainScreenState
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainScreen
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()

        setContent {
            val component = getApplicationComponent()
            val viewModelFactory = component.getViewModelFactory()
            val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)

            MyLittlePetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    MainScreen(
                        viewModelFactory = viewModelFactory,
                        activity = this,
                        initialScreenState = getInitialScreenState(viewModel)
                    )
                }
            }
        }
    }

    private fun getInitialScreenState(
        viewModel: AuthViewModel
    ): MainScreenState {
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