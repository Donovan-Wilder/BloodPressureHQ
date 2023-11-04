package com.donovanwilder.android.bloodpressurehq

import android.content.Context
import androidx.room.Room
import com.donovanwilder.android.bloodpressurehq.database.BpRecordDatabase
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import java.util.*

class BpRecordRepository private constructor(private val context: Context) {
    private val database = Room.databaseBuilder(
        this.context, BpRecordDatabase::class.java,
        BpRecordDatabase.DATABASE_NAME
    ).build()

    suspend fun addRecord(vararg bpRecord: BpRecord) = database.bpRecordDao().insertAll(*bpRecord)
    fun getAllRecords() = database.bpRecordDao().getAll()
    suspend fun updateRecord(
        id: Int,
        dateAdded: Date,
        sysValue: Int,
        diaValue: Int,
        pulseValue: Int
    ) = database.bpRecordDao().update(id, dateAdded, sysValue, diaValue, pulseValue)

    suspend fun deleteRecord(bpRecord: BpRecord) = database.bpRecordDao().delete(bpRecord)

    companion object {
        private var instance: BpRecordRepository? = null
        fun initialize(context: Context) {
            if (instance == null) {
                instance = BpRecordRepository(context)
            }
        }

        fun getInstance() =
            instance ?: throw Exception("BpRecordRepository has not been initialized.")
    }
}