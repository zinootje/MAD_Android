package com.example.mvp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Resto::class], version = 1, exportSchema = false)
abstract class RestoDatabase : RoomDatabase() {
    abstract fun restoDao(): RestoDao

    companion object{
        @Volatile
        private var Instace: RestoDatabase? = null

        fun getDatabase(context: Context): RestoDatabase {
            return Instace ?: synchronized(this) {
                 Room.databaseBuilder(
                    context,
                    RestoDatabase::class.java,
                    "resto_list_database"
                ).build().also { Instace = it }

            }
        }
    }
}