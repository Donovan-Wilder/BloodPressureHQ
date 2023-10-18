package com.donovanwilder.android.bloodpressurehq.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BpRecordDao {
    @Insert
    suspend fun insertAll(vararg bpRecord: BpRecord)

    @Query("SELECT * FROM bp_record ORDER BY date_added ASC")
    fun getAll(): Flow<List<BpRecord>>

    @Query("SELECT * FROM bp_record WHERE _id = :id")
    fun getBpRecord(id: Int): BpRecord

    @Query("UPDATE bp_record SET sys = :sysValue, dia = :diaValue, pulse = :pulseValue, date_added = :dateAdded WHERE _id = :id")
    suspend fun update(id: Int, dateAdded: Date, sysValue: Int, diaValue: Int, pulseValue: Int)

    @Delete
    suspend fun delete(bpRecord: BpRecord)
}