package com.example.mvp.ui.Util

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import kotlin.math.abs


//Todo check colors
private val COLORS = listOf(
    Color(0xFFEF9A9A),
    Color(0xFFE57373),
    Color(0xFFF44336),
    Color(0xFFE91E63),
)

/**
 * Retrieves a color based on the name of the restaurant.
 *
 * @param name The name of the restaurant.
 * @return The color associated with the restaurant name.
 */
//function to get a random color base on the name of the resto
fun getColorFromName(name: String): Color{
    val index = abs(name.hashCode() % COLORS.size)
    return COLORS[index]
}

/**
 * Sets the [contentDescription] for this Modifier.
 *
 * @param contentDescription The content description to set.
 * @return The modified Modifier object.
 */
fun Modifier.contentDescription(contentDescription: String): Modifier {
    return this.semantics { this.contentDescription = contentDescription }
}

///**
// * Sets the content description of the Modifier using a string resource ID.
// *
// * @param stringId the string resource ID to be used as the content description
// * @return the updated Modifier with the content description set
// */
//fun Modifier.contentDescriptionStringId(@StringRes stringId: Int): Modifier = composed {
//    this?
//    this.semantics { this.contentDescription =  }
//}