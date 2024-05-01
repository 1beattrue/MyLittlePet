package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneComponent

interface AuthComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Phone(val component: PhoneComponent) : Child()
        class Otp(val component: OtpComponent) : Child()
    }
}