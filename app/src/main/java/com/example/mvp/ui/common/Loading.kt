package com.example.mvp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvp.R

@Composable
fun LoadingIndicator() {
    //nonprescription loading
    //TODO maybe fix this
    val contentDesc = stringResource(id = R.string.loading_indicator)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = contentDesc },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
private fun LoadingIndicatorPreview() {
    LoadingIndicator()
}