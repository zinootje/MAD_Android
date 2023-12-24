package com.example.mvp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvp.data.database.convertors.DayConverter


@Database(entities = [MenuDataEntity::class], version = 1)
@TypeConverters(DayConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
}