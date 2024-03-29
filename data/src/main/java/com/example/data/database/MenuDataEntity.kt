package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.Day
import com.example.core.model.MenuData
import com.example.network.ApiRestoMenu
import com.example.network.asDomainObject
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.annotations.TestOnly


/**
 * Represents a MenuDataEntity object.
 * This object is used to store menu data in the database.
 *
 * @property location The location for which the menu data is stored.
 * @property days The list of [Day] objects representing the menu data for each day.
 * @property timestamp The timestamp indicating when the menu data was stored. Defaults to the current timestamp.
 * */
@Entity(tableName = "menu_data")
data class MenuDataEntity(
    @PrimaryKey
    val location: String,
    val days: List<Day>,
    val timestamp: Long = System.currentTimeMillis()
)


/**
 * Converts a [MenuDataEntity] databaseobject to a [MenuData] (domain)object.
 *
 * @receiver The [MenuDataEntity] object to be converted.
 * @return The converted [MenuData] object.
 */
fun MenuDataEntity.toMenuData(): MenuData {
    return MenuData(
        location = this.location,
        days = this.days.toImmutableList()
    )
}

/**
 * Converts a [MenuData] (domain)object  to a [MenuDataEntity] databaseobject used in the database.
 *
 * @receiver The [MenuData] object to be converted.
 * @return A [MenuDataEntity] object representing the menu data.
 */
fun MenuData.toDbMenu(): MenuDataEntity {
    return MenuDataEntity(
        location = location,
        days = days
    )
}


/**
 * TEST ONLY not used in production
 * Converts an [ApiRestoMenu] (network)object to a [MenuDataEntity] (database)object.
 *
 * @return A [MenuDataEntity] object representing the menu data.
 */
@TestOnly
fun ApiRestoMenu.toDbMenu(): MenuDataEntity {
    return this.asDomainObject().toDbMenu()
}