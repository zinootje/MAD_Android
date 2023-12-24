package com.example.mvp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MenuDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    fun insertMenuData(menuData: MenuDataEntity)

    @Query("SELECT * FROM MenuDataEntity WHERE location = :location")
    fun getMenuData(location: String): List<MenuDataEntity>
}