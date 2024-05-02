package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.DefaultOtpComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.DefaultPhoneComponent
import kotlinx.serialization.Serializable

class DefaultAuthComponent @AssistedInject constructor(
    private val phoneComponentFactory: DefaultPhoneComponent.Factory,
    private val otpComponentFactory: DefaultOtpComponent.Factory,

    @Assisted("onLoggedIn") private val onLoggedIn: () -> Unit,

    @Assisted("componentContext") componentContext: ComponentContext
) : AuthComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AuthComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Phone,
        handleBackButton = true,
        childFactory = ::child,
        key = "auth"
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): AuthComponent.Child = when (config) {
        Config.Phone -> {
            val component = phoneComponentFactory.create(
                onAuthFinished = { onLoggedIn() },
                onConfirmPhone = { navigation.push(Config.Otp) },
                componentContext = componentContext
            )
            AuthComponent.Child.Phone(component)
        }

        Config.Otp -> {
            val component = otpComponentFactory.create(
                onAuthFinished = { onLoggedIn() },
                onClickBack = { navigation.pop() },
                componentContext = componentContext
            )
            AuthComponent.Child.Otp(component)
        }
    }


    @Serializable
    private sealed interface Config {
        @Serializable
        data object Phone : Config

        @Serializable
        data object Otp : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onLoggedIn") onLoggedIn: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAuthComponent
    }
}