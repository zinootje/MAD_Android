package com.example.mvp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RestoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(resto: Resto)

    @Update
    suspend fun update(resto: Resto)

    @Delete
    suspend fun delete(resto: Resto)

    //get single resto
    @Query("SELECT * FROM resto_list_table WHERE name = :name")
    fun getResto(name: String): Flow<Resto>

    //get all restos
    @Query("SELECT * FROM resto_list_table ORDER BY name ASC")
    fun getRestoList(): Flow<List<Resto>>
}