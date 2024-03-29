package com.donovanwilder.android.bloodpressurehq

import android.content.Context
import androidx.room.Room
import com.donovanwilder.android.bloodpressurehq.database.BpRecordDatabase
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import java.util.*

class BpRecordRepository private constructor(private val context: Context){
    private val database = Room.databaseBuilder(this.context,BpRecordDatabase::class.java,
        BpRecordDatabase.DATABASE_NAME).build()

    suspend fun getAvgFromDateRange(fromDate:Date, toDate:Date) = database.bpRecordDao().getAvgFromDateRange( fromDate,toDate)
    suspend fun addRecord(vararg bpRecord: BpRecord) = database.bpRecordDao().insertAll(*bpRecord)
    fun getAllRecords()= database.bpRecordDao().getAll()
    suspend fun getEarliestDate() = database.bpRecordDao().getEarliestDate()
    suspend fun updateRecord(id:Int, sysValue: Int, diaValue:Int, pulseValue:Int,dateAdded:Date) = database.bpRecordDao().update(id,sysValue,diaValue,pulseValue,dateAdded)
    suspend fun deleteRecord(bpRecord: BpRecord)=database.bpRecordDao().delete(bpRecord)
    companion object{
        private var instance: BpRecordRepository? = null
        fun initialize(context:Context){
            if(instance == null){
                instance = BpRecordRepository(context)
            }
        }
        fun getInstance() = instance ?: throw Exception("BpRecordRepository has not been initialized.")
    }
}