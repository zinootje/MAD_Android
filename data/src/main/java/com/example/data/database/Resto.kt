package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.Resto as ModelResto


/**
 * Represents a restaurant in the application.
 *
 * @property name The name of the restaurant.
 * @property favorite A boolean value indicating whether the restaurant is marked as a favorite or not.
 */
@Entity(tableName = "resto_list_table")
data class Resto(
    @PrimaryKey
    val name: String,
    val favorite: Boolean,
)


/**
 * Converts a [Resto] (database)object into a [ModelResto] (domain)object.
 *
 * @receiver The [Resto] object to be converted.
 * @return A new [ModelResto] object with the same name and favorite status as the Resto object.
 */
fun Resto.asDomainObject(): ModelResto {
    return ModelResto(
        name = name,
        isFavorite = favorite
    )
}

/**
 * Converts a list of [Resto] (domain)objects to a list of [ModelResto] (database)objects.
 *
 * @receiver The list of [Resto] objects to be converted.
 * @return A new list containing the converted [ModelResto] objects.
 */
fun List<Resto>.asDomainObject(): List<ModelResto> {
    return map {
        ModelResto(
            name = it.name,
            isFavorite = it.favorite
        )
    }
}

/**
 * Converts a [ModelResto] (domain)object into a [Resto] (database)object.
 *
 * @return The converted [Resto] object.
 */
fun ModelResto.asDbObject(): Resto {
    return Resto(
        name = name,
        favorite = isFavorite
    )
}
