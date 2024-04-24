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
import com.arkivanov.decompose.defaultComponentContext
import edu.mirea.onebeattrue.mylittlepet.domain.main.entity.MainScreenState
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthContent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthViewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.DefaultAuthComponent
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authComponentFactory: DefaultAuthComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyLittlePetApplication).component.inject(this)
        super.onCreate(savedInstanceState)

        checkPermissions()

        val component = authComponentFactory.create(defaultComponentContext())

        setContent {

            MyLittlePetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    AuthContent(component = component)
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