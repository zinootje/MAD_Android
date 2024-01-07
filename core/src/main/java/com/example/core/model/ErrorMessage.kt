package com.example.core.model

import androidx.annotation.StringRes
import com.example.core.R

sealed class ErrorMessage(@StringRes val message: Int) {
    data object NoNetwork : ErrorMessage(R.string.no_network)
    data object InternalServerError : ErrorMessage(R.string.internal_server_error)
    data object ApplicationError : ErrorMessage(R.string.application_error)
    data class MiscNetworkErrorWithCode(val errorCode: Int) : ErrorMessage(R.string.misc_network_error)
    data object Unknown : ErrorMessage(R.string.unknown_error)
}