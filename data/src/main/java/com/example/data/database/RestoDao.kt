package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RestoDao {

    /**
     * Inserts a [Resto] object into the database. If the object already exists, ignore the action.
     *
     * @param resto The [Resto] object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(resto: Resto)

    /**
     * Updates a [Resto] object in the database.
     *
     * @param resto The [Resto] object to be updated.
     */
    @Update
    suspend fun update(resto: Resto)

    /**
     * Deletes a [Resto] object from the database.
     *
     * @param resto The [Resto] object to be deleted.
     */
    @Delete
    suspend fun delete(resto: Resto)


    ///TODO check nullability
    /**
     * Retrieves a single [Resto] object from the database based on the given name.
     *
     * @param name The name of the [Resto] object.
     * @return A [Flow] emitting the [Resto] object.
     */
    @Query("SELECT * FROM resto_list_table WHERE name = :name")
    fun getResto(name: String): Flow<Resto>

    /**
     * Retrieves a list of restaurants from the database. The list is ordered by favorite status and then by name.
     *
     * @return A [Flow] emitting a list of [Resto] objects.
     */
    @Query("SELECT * FROM resto_list_table ORDER BY favorite DESC, name ASC")
    fun getRestoList(): Flow<List<Resto>>


    /**
     * Sets the favorite status of a restaurant in the database.
     *
     * @param name The name of the restaurant.
     * @param favorite The favorite status to be set.
     */
    @Query("UPDATE resto_list_table SET favorite = :favorite WHERE name = :name")
    suspend fun setFavoriteResto(name: String, favorite: Boolean)
}