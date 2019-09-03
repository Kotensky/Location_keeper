package com.gemicle.locationkeeper.data.base

import androidx.room.TypeConverter
import java.util.*

class TypesConverter {
    @TypeConverter
    fun longToDate(long: Long): Date {
        return Date(long)
    }

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }
}