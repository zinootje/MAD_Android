package com.example.mvp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvp.data.database.convertors.DayConverter

//TODO reset version to 1
@Database(entities = [MenuDataEntity::class], version = 2)
@TypeConverters(DayConverter::class)
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