package com.donovanwilder.android.bloodpressurehq.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.donovanwilder.android.bloodpressurehq.model.BpRecord

@Database([BpRecord::class], version = 2)
@TypeConverters(BpRecordTypeConverters::class)
abstract class BpRecordDatabase: RoomDatabase() {
    abstract fun bpRecordDao(): BpRecordDao

    companion object{
        const val DATABASE_NAME ="bprecord-database"
    }
}
