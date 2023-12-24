package com.example.mvp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvp.model.Resto as ModelResto


@Entity(tableName = "resto_list_table")
data class Resto(
    //TODO check if primary key is needed
    @PrimaryKey
    val name: String,
    val favorite: Boolean,
)



fun Resto.asDomainObject(): ModelResto {
    return ModelResto(
        name = name,
        favorite = favorite
    )
}

fun List<Resto>.asDomainObject(): List<ModelResto> {
    return map {
        ModelResto(
            name = it.name,
            favorite = it.favorite
        )
    }
}

fun ModelResto.asDbObject(): Resto {
    return Resto(
        name = name,
        favorite = favorite
    )
}
