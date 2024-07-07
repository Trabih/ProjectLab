package com.example.projectlab1

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): Date? {
        return value?.let { formatter.parse(value) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { formatter.format(date) }
    }
}
