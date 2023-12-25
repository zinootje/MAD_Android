package com.example.mvp.data.database.convertors

import androidx.room.TypeConverter
import java.util.Date

class DateConvertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

