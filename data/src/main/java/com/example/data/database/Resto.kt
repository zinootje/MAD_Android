package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.Resto as ModelResto


@Entity(tableName = "resto_list_table")
data class Resto(
    @PrimaryKey
    val name: String,
    val favorite: Boolean,
)



fun Resto.asDomainObject(): ModelResto {
    return ModelResto(
        name = name,
        isFavorite = favorite
    )
}

fun List<Resto>.asDomainObject(): List<ModelResto> {
    return map {
        ModelResto(
            name = it.name,
            isFavorite = it.favorite
        )
    }
}

fun ModelResto.asDbObject(): Resto {
    return Resto(
        name = name,
        favorite = isFavorite
    )
}
