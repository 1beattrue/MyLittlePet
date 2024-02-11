package edu.mirea.onebeattrue.mylittlepet.domain.auth.entity

class InvalidPhoneNumberException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid phone number"
    }
}

class InvalidVerificationCodeException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid verification code"
    }
}

class TimeoutVerificationCodeException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Timeout has not expired"
    }
}

class UnknownAuthException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Unknown exception"
    }
}

class NetworkAuthException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Network exception"
    }
}

class InvalidCredentialsAuthException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid confirmation code"
    }
}

class TooManyRequestsAuthException(message: String = TEXT_MESSAGE) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Too many requests"
    }
}