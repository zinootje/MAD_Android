package com.example.core.model

import androidx.annotation.StringRes
import com.example.core.R
//TODO fix
//import com.example.mvp.R
import kotlinx.serialization.Serializable

data class MenuData(
    val location: String,
    val days: List<Day>
)

//Made this serializable so that it can be used in the database
@Serializable()
data class Day(
    val dag: String,
    val message: String,
    val menu: Menu
)

@Serializable
data class Menu(
    val items: Map<String, List<Dish>>
)

@Serializable
data class Dish(
    val name: String,
    val special: Special
)

//TODO fix
@Serializable
enum class Special(@StringRes val title: Int) {
    VEGAN(R.string.vegan),
        VEGIE(R.string.veggie),
        NONE(R.string.none),
    UNKNOWN(R.string.unknown)
//    VEGAN(0),
//    VEGIE(0),
//    NONE(0),
//    UNKNOWN(0)
}