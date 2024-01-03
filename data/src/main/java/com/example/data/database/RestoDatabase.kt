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
@TypeConverters(DayMenuConverter::class)
@Database(entities = [Resto::class, MenuDataEntity::class], version = 2, exportSchema = false)
abstract class RestoDatabase : RoomDatabase() {
    abstract fun restoDao(): RestoDao

    abstract fun menuDao(): MenuDao


    companion object{
        @Volatile

        private var Instace: RestoDatabase? = null


        /**
         * Retrieves the RestoDatabase instance.
         *
         * This method is used to get the database instance of type RestoDatabase.
         *
         * @param context The context in which the database is initialized.
         * @return The RestoDatabase instance.
         */
        fun getDatabase(context: Context): RestoDatabase {
            return Instace ?: synchronized(this) {
                 Room.databaseBuilder(
                    context,
                    RestoDatabase::class.java,
                    "resto_list_database"
                 )
                     //TODO remove in prod
                     .fallbackToDestructiveMigration()
                     .build().also { Instace = it }

            }
        }
    }
}