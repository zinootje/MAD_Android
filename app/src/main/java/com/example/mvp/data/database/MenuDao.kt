package com.example.mvp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing the MenuDao class.
 *
 * This interface provides methods to insert [MenuDataEntity] objects into the database and to retrieve menu data from the database.
 */
@Dao
interface MenuDao {
    /**
     * Inserts menu data into the database.
     * If the data already exists, replace it.
     *
     * @param menuData The [MenuDataEntity] object to be inserted.
     */
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertMenuData(menuData: MenuDataEntity)

    /**
     * Retrieves menu data from the database based on the location.
     *
     * @param location The location for which the menu data is retrieved.
     * @return A [Flow] of [MenuDataEntity] representing the menu data for the specified location, or null if no data is found.
     */
    @Query("SELECT * FROM menu_data WHERE location = :location")
    fun getMenuData(location: String): Flow<MenuDataEntity?>
}