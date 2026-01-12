package com.ruhaan.orangeeditor.data.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
  @TypeConverter
  fun fromLocalDatetime(value: LocalDateTime): Long {
    return value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
  }

  @TypeConverter
  fun toLocalDateTime(value: Long): LocalDateTime {
    return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDateTime()
  }
}
