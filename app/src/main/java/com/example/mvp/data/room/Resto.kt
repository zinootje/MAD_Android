package com.example.mvp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "resto_list_table")
data class Resto(
    //TODO check if primary key is needed
    @PrimaryKey
    val name: String,
    val favorite: Boolean,
)



fun Resto.asDomainObject(): com.example.mvp.model.Resto {
    return com.example.mvp.model.Resto(
        name = name,
        favorite = favorite
    )
}

fun List<Resto>.asDomainObject(): List<com.example.mvp.model.Resto> {
    return map {
        com.example.mvp.model.Resto(
            name = it.name,
            favorite = it.favorite
        )
    }
}