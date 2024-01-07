package com.example.mvp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.model.ErrorMessage
import com.example.data.getErrorMessage
import com.example.mvp.R
import com.example.mvp.ui.util.formatErrorMessage

/**
 * A composable function that renders an error view with an optional message and retry functionality.
 *
 * @param message The optional error message to display.
 * @param error The optional [Throwable]  representing the error.
 * @param onRetry The optional callback function to invoke when the retry button is clicked.
 */
@Composable
fun ErrorComponent(
    message: String? = null,
    error: Throwable? = null,
    onRetry: (() -> Unit)? = null
) {
if (message != null) {
        ErrorView(message = message, onRetry = onRetry)
    } else if (error != null) {
    ErrorComponent(errorMessage = error.getErrorMessage(), onRetry = onRetry)
    }
}

/**
 * A composable function that renders an error component with an error message and optional retry functionality.
 *
 * @param errorMessage The error message to display.
 * @param onRetry The callback function to invoke when the retry button is clicked. If null, the retry button will not be shown.
 */
@Composable
fun ErrorComponent(
    errorMessage: ErrorMessage, onRetry: (() -> Unit)? = null
) {
    val message = formatErrorMessage(errorMessage = errorMessage)
    ErrorView(message = message, onRetry = onRetry)
}

/**
 * A composable function that renders an error view with an optional message and retry functionality.
 *
 * @param message The error message to display.
 * @param onRetry The callback function to invoke when the retry button is clicked. If null, the retry button will not be shown.
 */
@Composable
private fun ErrorView(message: String, onRetry: (() -> Unit)?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //show svg
        Image(painter = painterResource(id = R.drawable.undraw_server_down_small), contentDescription = "Error image")
        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (onRetry != null) {
            Button(
                onClick = onRetry,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorViewPreview() {
    ErrorView(message = "Error message", onRetry = null)
}
