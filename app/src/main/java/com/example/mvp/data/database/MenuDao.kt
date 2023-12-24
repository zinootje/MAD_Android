package com.example.mvp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertMenuData(menuData: MenuDataEntity)

    @Query("SELECT * FROM menu_data WHERE location = :location")
    fun getMenuData(location: String): Flow<MenuDataEntity?>
}