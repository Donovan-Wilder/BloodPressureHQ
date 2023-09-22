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

    @Query("SELECT * FROM ${BpRecord.TABLE_NAME} ORDER BY date_added ASC")
    fun getAll(): Flow<List<BpRecord>>

    @Query("UPDATE ${BpRecord.TABLE_NAME} SET sys = :sysValue, dia = :diaValue, pulse = :pulseValue, date_added = :dateAdded WHERE _id = :id")
    suspend fun update(id: Int, sysValue: Int, diaValue: Int, pulseValue: Int, dateAdded: Date)

    @Query("SELECT * FROM ${BpRecord.TABLE_NAME} WHERE date_added BETWEEN :fromDate  AND :toDate ORDER BY date_added DESC")
     fun getFromDate(fromDate:Date,toDate:Date):Flow<List<BpRecord>>
    @Delete
    suspend fun delete(bpRecord: BpRecord) }