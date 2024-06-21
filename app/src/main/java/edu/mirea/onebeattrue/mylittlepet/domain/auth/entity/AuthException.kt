package edu.mirea.onebeattrue.mylittlepet.domain.auth.entity

data class UnknownAuthException(
    override val message: String = TEXT_MESSAGE
) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Authorization error"
    }
}

data class NetworkAuthException(
    override val message: String = TEXT_MESSAGE
) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Network error"
    }
}

data class InvalidCredentialsAuthException(
    override val message: String = TEXT_MESSAGE
) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Invalid credentials"
    }
}

data class TooManyRequestsAuthException(
    override val message: String = TEXT_MESSAGE
) : Exception(message) {
    companion object {
        private const val TEXT_MESSAGE = "Too many requests"
    }
}