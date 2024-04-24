package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneComponent

interface AuthComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Phone(val component: PhoneComponent) : Child
        data class Otp(val component: OtpComponent) : Child
    }
}