package com.donovanwilder.android.bloodpressurehq.database

import androidx.room.TypeConverter
import java.util.Date

class BpRecordTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long)= Date(value)
    @TypeConverter
    fun dateToTimestamp(date:Date) = date.time
}