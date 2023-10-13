package com.donovanwilder.android.bloodpressurehq

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.donovanwilder.android.bloodpressurehq.fake.BpRecordDummyData
import com.donovanwilder.android.bloodpressurehq.database.BpRecordDao
import com.donovanwilder.android.bloodpressurehq.database.BpRecordDatabase
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var recordDao: BpRecordDao
    private lateinit var db: BpRecordDatabase

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BpRecordDatabase::class.java).build()
        recordDao = db.bpRecordDao()
    }
    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun Should_ReturnTheAverageBpRecord_When_RangeIsADay() {
        val calendar = GregorianCalendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
        }
        val toDate = calendar.time
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
        }
        val fromDate = calendar.time
        val numberOfRecords = 3
        val bpRecords = com.donovanwilder.android.bloodpressurehq.fake.BpRecordDummyData.generateRecordList(numberOfRecords,fromDate,toDate)


        runTest {
            var (sys, dia, pulse) = arrayOf(0,0,0)
            bpRecords.forEach {
                sys+= it.sys
                dia += it.dia
                pulse += it.pulse
                recordDao.insertAll(it)
            }

            sys/= numberOfRecords
            dia/= numberOfRecords
            pulse /= numberOfRecords

            val result = recordDao.getAvgFromDateRange(fromDate, toDate)
            assertEquals(sys, result.sys)
            assertEquals(dia, result.dia)
            assertEquals(pulse, result.pulse)
        }



    }
    private fun printRecords(recordList: List<BpRecord>): String{
        val string = StringBuilder()
        recordList.forEach {
            string.append("$it \n")
        }
        return string.toString()
    }
}