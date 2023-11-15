package com.example.myapplication.common

import androidx.room.TypeConverter
import java.time.Instant

class InstantLongConverter {
    @TypeConverter
    fun instantToLong(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun longToInstant(long: Long?): Instant? {
        return long?.let { Instant.ofEpochMilli(it) }
    }
}