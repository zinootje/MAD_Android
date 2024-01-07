package com.example.data

import com.example.core.model.ErrorMessage

/**
 * Checks whether the Throwable is a network error.
 *
 * This method is an extension on the Throwable class and is used to detect if the Throwable is a network error.
 * It checks if the Throwable is an instance of java.net.UnknownHostException or if it is an instance of retrofit2.HttpException with a code of 503.
 *
 * @return true if the Throwable is a network error, false otherwise.
 */
fun Throwable.isNoNetworkError(): Boolean {
    return this is java.net.UnknownHostException || (this is retrofit2.HttpException && this.code() == 503)
}

/**
 * Returns the appropriate error message based on the type of throwable.
 *
 * @return The error message as an instance of [ErrorMessage].
 */
fun Throwable.getErrorMessage(): ErrorMessage {
    return when {
        this.isNoNetworkError() -> ErrorMessage.NoNetwork
        this is retrofit2.HttpException -> ErrorMessage.MiscNetworkErrorWithCode(this.code())
        else -> ErrorMessage.Unknown
    }
}