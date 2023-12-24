package com.example.mvp.model

import androidx.annotation.StringRes
import com.example.mvp.R
import kotlinx.serialization.Serializable

data class MenuData(
    val location: String,
    val days: List<Day>
)


data class Day(
    val dag: String,
    val message: String,
    val menu: Menu
)


data class Menu(
    val items: Map<String, List<Dish>>
)
data class Dish(
    val name: String,
    val special: special
)

enum class special(@StringRes val title: Int) {
    VEGAN(R.string.vegan),
    VEGIE(R.string.veggie),
    NONE(R.string.none),
    UNKNOWN(R.string.unknown)
}