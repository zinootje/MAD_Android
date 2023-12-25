package com.example.mvp.ui.Util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs


//Todo check colors
val COLORS = listOf(
    Color(0xFFEF9A9A),
    Color(0xFFE57373),
    Color(0xFFF44336),
    Color(0xFFE91E63),
)

//function to get a random color base on the name of the resto
fun getColorFromName(name: String): Color{
    val index = abs(name.hashCode() % COLORS.size)
    return COLORS[index]
}