package com.example.mvp.Network

import com.example.mvp.model.MenuData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import com.example.mvp.model.Day as ModelDay
import com.example.mvp.model.Menu as ModelMenu
import com.example.mvp.model.Dish as ModelDish
import com.example.mvp.model.special as ModelSpecial
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
    val special: special
)

@Serializable
enum class special {
                   @SerialName("vegan")
    VEGAN,
                      @SerialName("veggie")
    VEGIE,
                        @SerialName("none")
    NONE,
    //TODO
    //if the json is not one of the above, it will be UNKNOWN

    UNKNOWN
}

fun special.asDomainObject(): ModelSpecial {
    return when (this) {
        special.VEGAN -> ModelSpecial.VEGAN
        special.VEGIE -> ModelSpecial.VEGIE
        special.NONE -> ModelSpecial.NONE
        special.UNKNOWN -> ModelSpecial.UNKNOWN
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
                        }
                    }.toMap()
                )
            )
        }
    )
}

fun List<ApiRestoMenu>.asDomainObjects(): List<MenuData> {
    return map {
        it.asDomainObject()
    }
}