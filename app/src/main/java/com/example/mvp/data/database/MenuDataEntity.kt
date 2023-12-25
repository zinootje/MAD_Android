package com.example.mvp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvp.network.ApiRestoMenu
import com.example.mvp.network.asDomainObject
import com.example.mvp.model.Day
import com.example.mvp.model.MenuData

@Entity(tableName = "menu_data")
data class MenuDataEntity(
    @PrimaryKey
    val location: String,
    val days: List<Day>,

    val timestamp: Long = System.currentTimeMillis()
)


fun MenuDataEntity.toMenuData(): MenuData {
    return MenuData(
        location = this.location,
        days = this.days
    )
}

fun MenuData.toDbMenu(): MenuDataEntity {
    return MenuDataEntity(
        location = location,
        days = days
    )
}

fun ApiRestoMenu.toDbMenu(): MenuDataEntity {
    //Todo check if this can be done in a better way
    return this.asDomainObject().toDbMenu()
}