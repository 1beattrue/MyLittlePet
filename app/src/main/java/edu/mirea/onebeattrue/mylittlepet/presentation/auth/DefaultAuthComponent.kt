package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.DefaultOtpComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.DefaultPhoneComponent
import kotlinx.parcelize.Parcelize

class DefaultAuthComponent @AssistedInject constructor(
    private val phoneComponentFactory: DefaultPhoneComponent.Factory,
    private val otpComponentFactory: DefaultOtpComponent.Factory,

    @Assisted("onLoggedIn") private val onLoggedIn: () -> Unit,

    @Assisted("componentContext") componentContext: ComponentContext
) : AuthComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AuthComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = Config.Phone,
            handleBackButton = true,
            childFactory = ::child
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


    sealed interface Config : Parcelable {
        @Parcelize
        data object Phone : Config

        @Parcelize
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