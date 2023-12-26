package com.example.mvp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mvp.R

@Composable
fun ErrorComponent(
    message: String? = null,
    error: Throwable? = null,
    onRetry: (() -> Unit)? = null
) {
if (message != null) {
        ErrorView(message = message, onRetry = onRetry)
    } else if (error != null) {
        ErrorView(message = error.message ?: "Unknown error", onRetry = onRetry)
    }
}

//TODO add svg
@Composable
fun ErrorView(message: String, onRetry:( () -> Unit)?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        //in debug show whole error
        //TODO build config
        if (true) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorViewPreview() {
    ErrorView(message = "Error message", onRetry = null)
}
