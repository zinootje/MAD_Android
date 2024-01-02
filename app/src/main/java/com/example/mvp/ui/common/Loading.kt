package com.example.mvp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvp.R
import com.example.mvp.ui.util.contentDescription

/**
 * Displays a loading indicator.
 *
 * @param modifier The [Modifier] to be applied to the loading indicator.
 */
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    //nonprescription loading
    Box(
        modifier = modifier
            .fillMaxSize()
            .contentDescription(stringResource(id = R.string.loading_indicator)),
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