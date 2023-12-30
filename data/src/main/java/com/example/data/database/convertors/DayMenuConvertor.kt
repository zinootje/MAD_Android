package com.example.data.database.convertors

import androidx.room.TypeConverter
import com.example.core.model.Day
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Converts a list of [Day] objects to a JSON string representation and vice versa.
 *
 * This class provides methods to convert a list of [Day] objects to a JSON string using Kotlinx serialization,
 * and to convert a JSON string back to a list of [Day] objects.
 *
 * Defines [TypeConverter] methods to convert a list of [Day] objects to a JSON string representation and vice versa.
 */
class DayMenuConverter {
    @TypeConverter
    fun fromDayList(days: List<Day>): String {
        return Json.encodeToString(days)
    }

    @TypeConverter
    fun toDayList(dayString: String): List<Day> {
        return Json.decodeFromString(dayString)
    }
}
