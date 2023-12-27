package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.database.convertors.DayMenuConverter

/**

 * Database class that extends RoomDatabase.
 *
 * This class is responsible for creating and managing the database instance.
 * It has an abstract method to get the DAO class and a companion object to get the database instance.
 *
 * @property context The context used to create the database.
 * */
//TODO reset version to 1
@Database(entities = [MenuDataEntity::class], version = 2)
@TypeConverters(DayMenuConverter::class)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao

    companion object {
        @Volatile
        private var Instace: MenuDatabase? = null

        fun getDatabase(context: Context): MenuDatabase {
            return Instace ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MenuDatabase::class.java,
                    //TODO merge the two databases
                    "resto_menu_database"
                )
                    //TODO remove fallbackToDestructiveMigration in production
                    .fallbackToDestructiveMigration()
                    .build().also { Instace = it }

            }
        }
    }
}