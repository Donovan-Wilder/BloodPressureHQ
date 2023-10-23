package com.donovanwilder.android.bloodpressurehq

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.donovanwilder.android.bloodpressurehq.database.BpRecordDao
import com.donovanwilder.android.bloodpressurehq.database.BpRecordDatabase
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.donovanwilder.android.bloodpressurehq.testing.BpRecordDummyData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class BpRecordDaoTest {
    private lateinit var recordDao: BpRecordDao
    private lateinit var db: BpRecordDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BpRecordDatabase::class.java
        ).build()
        recordDao = db.bpRecordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    fun Should_InsertRecords() = runTest {
        val calender = GregorianCalendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 9)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val toDate = calender.time
        calender.add(Calendar.DAY_OF_MONTH, -5)
        val fromDate = calender.time
        val recordList = BpRecordDummyData.generateRecordList(15, fromDate, toDate)
        for (record in recordList) {
            recordDao.insertAll(record)
        }

        val result = recordDao.getAll().first()

        assertEquals(15, result.size)
    }

    @Test
    fun Should_ReturnListOfRecordsInAscendingOrder() = runTest {
        val calender = GregorianCalendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, 9)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val toDate = calender.time
        calender.add(Calendar.DAY_OF_MONTH, -5)
        val fromDate = calender.time
        val recordList = BpRecordDummyData.generateRecordList(15, fromDate, toDate)
        recordList.random()
        for (record in recordList) {
            recordDao.insertAll(record)
        }

        val result = recordDao.getAll().first()

        var isSorted = false
        var lastRecord = result[0]
        for (i in 1 until result.size) {
            if (lastRecord.dateAdded.time <= result[i].dateAdded.time) {
                isSorted = true
            }
            lastRecord = result[i]
        }

        assertTrue(isSorted)
    }

    @Test
    fun Should_UpdateRecord() = runTest {
        val recordData = arrayOf(
            BpRecord(0, Date(0), 120, 70, 60),
            BpRecord(0, Date(0), 120, 70, 60),
            BpRecord(0, Date(0), 120, 70, 60)
        )
        recordDao.insertAll(*recordData)

        val (sys, dia, pulse) = arrayOf(135, 85, 45)
        val date = Date(4)
        recordDao.update(2, date, sys, dia, pulse)

        val result = recordDao.getBpRecord(2)

        assertEquals(sys, result.sys)
        assertEquals(dia, result.dia)
        assertEquals(pulse, result.pulse)
        assertEquals(date, result.dateAdded)


    }

    @Test
    fun Should_DeleteRecord()= runTest{

        val deletedRecord = BpRecord(0, Date(1), 121, 71, 61)
        val recordData = arrayOf(
            BpRecord(0, Date(0), 120, 70, 60),
            deletedRecord,
            BpRecord(0, Date(3), 123, 73, 63)
        )

        recordDao.insertAll(*recordData)

        recordDao.delete(BpRecord(1,Date(0),0,0,0))


        val result = recordDao.getAll().first()

        assertEquals(2, result.size)
        assertNotEquals(deletedRecord, result[1])


    }
}