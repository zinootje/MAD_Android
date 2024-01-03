package com.example.mvp.utils

import androidx.compose.ui.test.SemanticsNodeInteractionCollection

fun SemanticsNodeInteractionCollection.assertNodeCountIsNotZero(): SemanticsNodeInteractionCollection {
    val errorOnFail = "Failed to assert that node count is not 0."
    val matchedNodes = fetchSemanticsNodes(errorMessageOnFail = errorOnFail)
    if (matchedNodes.isEmpty()) {
        throw AssertionError(
            "Failed to find any nodes matching the given criteria. $errorOnFail"
        )
    }
    return this
}