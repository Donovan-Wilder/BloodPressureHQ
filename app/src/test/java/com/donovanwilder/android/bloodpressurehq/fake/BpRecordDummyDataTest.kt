package com.donovanwilder.android.bloodpressurehq.fake

import com.donovanwilder.android.bloodpressurehq.testing.BpRecordDummyData
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Scanner

class BpRecordDummyDataTest {
    @Test
    fun Should_GenerateCorrectNumberofRecords() {
        val calendar = GregorianCalendar.getInstance()
        calendar.clear()
        calendar.set(2023, 0, 20, 0, 0)
        val fromDate = calendar.time
        calendar.set(2023, 8, 1, 0, 0)
        val toDate = calendar.time

        val expected = 30

        val result = BpRecordDummyData.generateRecordList(30, fromDate, toDate)


        assertEquals(expected, result.size)

    }

    @Test
    fun Should_GenerateRecordsInAscendingOrder() {
        val calendar = GregorianCalendar.getInstance()
        calendar.clear()
        calendar.set(2023, 0, 20, 0, 0)
        val fromDate = calendar.time
        calendar.set(2023, 8, 1, 0, 0)
        val toDate = calendar.time

        val result = BpRecordDummyData.generateRecordList(30, fromDate, toDate)
        var lastDate = 0L
        result.forEach {
            assertTrue(lastDate <= it.dateAdded.time)
            lastDate = it.dateAdded.time
        }
    }

    @Test
    fun Should_GenerateOneRecordPerDay() {

        val data = BpRecordDummyData.getDailyRecordList(Date(), 7, 1)
        var differentDays = 1
        val calendar = GregorianCalendar.getInstance()
        calendar.time = data.get(0).dateAdded
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum((Calendar.MINUTE)))

        for (record in data) {
            if (record.dateAdded.after(calendar.time)) {
                calendar.time = record.dateAdded
                calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, calendar.getActualMaximum((Calendar.MINUTE)))
                differentDays++
            }
        }
        val expected = 7
        assertEquals("the size doesn't match", expected, differentDays)
    }

    @Test
    fun Should_GenerateMulipleRecords_When_RecordPerDayIsMoreThanOne() {
        val expected = 21
        val result = BpRecordDummyData.getDailyRecordList(Date(), 7, 3)

        assertEquals(expected, result.size)
    }

    @Test
    fun Should_GenerateAFile() {
        val calendar = GregorianCalendar.getInstance().apply {
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val fromDate = calendar.time
        calendar.apply {
            set(Calendar.MONTH, 11)
            set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
            set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
        }
        val toDate = calendar.time

        val filename =  "test_${Date().time}"
        val file = File(filename)
        BpRecordDummyData.generateFile(1,fromDate,toDate, file)


        assertTrue( file.exists())
        file.delete()
    }
    @Test
    fun Should_ProduceCorrectNumberOfRecords() {
        val calendar = GregorianCalendar.getInstance().apply {
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val fromDate = calendar.time
        calendar.apply {
            set(Calendar.MONTH, 11)
            set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
            set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
        }
        val toDate = calendar.time

        val filename =  "test_${Date().time}"
        val file = File(filename)
        val numberOfRecords = 15
        BpRecordDummyData.generateFile(numberOfRecords,fromDate,toDate, file)

        val scanner = Scanner(file)
        var result = 0
        while( scanner.hasNextLine()){
            result++
            scanner.nextLine()
        }

        val expected = numberOfRecords + 1 // Include  the Csv header line
        assertEquals(expected, result)
        file.delete()
    }

}