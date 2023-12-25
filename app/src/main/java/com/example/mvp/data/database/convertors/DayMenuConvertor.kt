package com.example.mvp.data.database.convertors

import androidx.room.TypeConverter
import com.example.mvp.model.Day
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
