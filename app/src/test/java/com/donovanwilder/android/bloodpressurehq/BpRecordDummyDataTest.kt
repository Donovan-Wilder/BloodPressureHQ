package com.donovanwilder.android.bloodpressurehq

import com.donovanwilder.android.bloodpressurehq.testing.BpRecordDummyData
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

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
}