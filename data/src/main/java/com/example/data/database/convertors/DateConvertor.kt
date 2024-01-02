package com.example.data.database.convertors

import androidx.room.TypeConverter
import java.util.*


/**
 * Converts date to a timestamp in [Long] used in a database and vice versa.
 *
 * Defines [TypeConverter] methods to convert between Date objects and timestamps in [Long] used in a database.
 */
class DateConvertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

