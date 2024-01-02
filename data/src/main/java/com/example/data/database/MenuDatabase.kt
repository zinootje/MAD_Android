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
 * */
//TODO reset version to 1
@Database(entities = [MenuDataEntity::class], version = 2)
@TypeConverters(DayMenuConverter::class)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao

    companion object {
        @Volatile
        private var Instace: MenuDatabase? = null

        /**
         * Retrieves the MenuDatabase instance.
         *
         * This method is used to get the database instance of type MenuDatabase.
         *
         * @param context The context in which the database is initialized.
         * @return The MenuDatabase instance.
         */
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