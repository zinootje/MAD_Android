package com.example.network

import com.example.core.model.MenuData
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.core.model.Day as ModelDay
import com.example.core.model.Dish as ModelDish
import com.example.core.model.Menu as ModelMenu
import com.example.core.model.Special as ModelSpecial

@Serializable
data class ApiRestoMenu(
    val location: String,
    val days: List<Day>
)

@Serializable
data class Day(
    val dag: String,
    val message: String,
    val menu: Map<String, List<Dish>>
)



@Serializable
data class Dish(
    val name: String,
    val special: Special
)

@Serializable
enum class Special {
                   @SerialName("vegan")
    VEGAN,
                      @SerialName("veggie")
    VEGGIE,
                        @SerialName("none")
    NONE,
    //TODO
    //if the json is not one of the above, it will be UNKNOWN

    UNKNOWN
}

fun Special.asDomainObject(): ModelSpecial {
    return when (this) {
        Special.VEGAN -> ModelSpecial.VEGAN
        Special.VEGGIE -> ModelSpecial.VEGIE
        Special.NONE -> ModelSpecial.NONE
        Special.UNKNOWN -> ModelSpecial.UNKNOWN
    }
}

fun Flow<List<ApiRestoMenu>>.asDomainObjects(): Flow<List<MenuData>> {
    return this.map {
        it.asDomainObjects()
    }
}

fun ApiRestoMenu.asDomainObject(): MenuData {
    return MenuData(
        location = this.location,
        days = this.days.map { day ->
            ModelDay(
                dag = day.dag,
                message = day.message,
                menu = ModelMenu(
                    items = day.menu.map { (key, value) ->
                        key to value.map { dish ->
                            ModelDish(
                                name = dish.name,
                                special = dish.special.asDomainObject()
                            )
                        }.toImmutableList()
                    }.toMap().toImmutableMap()
                )
            )
        }.toImmutableList()
    )
}

fun List<ApiRestoMenu>.asDomainObjects(): List<MenuData> {
    return map {
        it.asDomainObject()
    }
}