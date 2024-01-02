package com.example.mvp.ui.util

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

//make a semantic

/**
 * Represents a key used for storing and accessing the value associated with a semantic property called "TabKey".
 *
 * @see SemanticsPropertyKey
 */
val TabKey = SemanticsPropertyKey<String>("TabKey")

/**
 * Represents a semantics property receiver with a tab key.
 *
 * The `tabKey` property is used to assign a unique identifier to a tab in the `MenuContent`.
 * It is of type [SemanticsPropertyReceiver] and can be accessed using the `tabKey` property.
 *
 * Usage:
 * ```
 * Modifier.semantics {
 *    tabKey = "tab1"
 *    // ...
 *    // other semantics properties
 *    // ...
 *    }
 * ```
 *
 * @see SemanticsPropertyReceiver
 * @see TabKey
 */
var SemanticsPropertyReceiver.tabKey by TabKey
