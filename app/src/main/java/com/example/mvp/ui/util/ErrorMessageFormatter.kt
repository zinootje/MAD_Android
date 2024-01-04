package com.example.mvp.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.core.model.ErrorMessage


@Composable
fun formatErrorMessage(errorMessage: ErrorMessage): String {
    return when (errorMessage) {
        ErrorMessage.ApplicationError -> stringResource(id = errorMessage.message)
        ErrorMessage.InternalServerError -> stringResource(id = errorMessage.message)
        is ErrorMessage.MiscNetworkErrorWithCode -> stringResource(id = errorMessage.message, errorMessage.errorCode)
        ErrorMessage.NoNetwork -> stringResource(id = errorMessage.message)
        ErrorMessage.Unknown -> stringResource(id = errorMessage.message)
    }
}