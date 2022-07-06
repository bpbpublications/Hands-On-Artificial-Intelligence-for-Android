package com.book.example.room

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

}

class LevelConverters {

    @TypeConverter
    fun fromName(value: String?): UserLevel? {
        return value?.let { UserLevel.valueOf(value) }
    }

    @TypeConverter
    fun levelToName(level: UserLevel?): String? {
        return level?.name
    }

}
