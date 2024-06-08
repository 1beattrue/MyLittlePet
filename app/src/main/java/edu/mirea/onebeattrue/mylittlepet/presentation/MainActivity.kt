package edu.mirea.onebeattrue.mylittlepet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import coil.map.Mapper
import com.arkivanov.decompose.defaultComponentContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.data.mapper.UserMapper
import edu.mirea.onebeattrue.mylittlepet.data.remote.api.ApiFactory
import edu.mirea.onebeattrue.mylittlepet.data.remote.dto.UserDto
import edu.mirea.onebeattrue.mylittlepet.presentation.root.DefaultRootComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.root.RootContent
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    lateinit var smth: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyLittlePetApplication).component.inject(this)
        super.onCreate(savedInstanceState)

        val component = rootComponentFactory.create(
            componentContext = defaultComponentContext()
        )

        lifecycleScope.launch {
            aboba()
        }

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

    suspend fun aboba() {
        val userService = ApiFactory.userService
        val userMapper = UserMapper()
        smth = userService.createUser(userMapper.mapUserEntityToDto(FirebaseAuth.getInstance().currentUser!!))
    }
}
