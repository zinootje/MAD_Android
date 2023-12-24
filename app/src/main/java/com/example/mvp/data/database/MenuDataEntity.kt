package com.example.mvp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvp.model.Day

@Entity(tableName = "menu_data")
data class MenuDataEntity(
    @PrimaryKey()
    val location: String,
    val days: List<Day>,
    )
