package com.donovanwilder.android.bloodpressurehq.tools

import org.junit.Assert.*
import org.junit.Test
import java.util.GregorianCalendar

class BpRecordDummyDataTest{
    @Test
    fun Should_GenerateCorrectNumberofRecords(){
        val calendar = GregorianCalendar.getInstance()
        calendar.clear()
        calendar.set(2023,0,20,0, 0)
        val fromDate = calendar.time
        calendar.set(2023,8,1,0,0)
        val toDate = calendar.time

        val expected = 30

        val result = BpRecordDummyData.generateRecordList(30, fromDate, toDate)


        assertEquals(expected, result.size)

    }
    @Test
    fun Should_GenerateRecordsInAscendingOrder(){
        val calendar = GregorianCalendar.getInstance()
        calendar.clear()
        calendar.set(2023,0,20,0, 0)
        val fromDate = calendar.time
        calendar.set(2023,8,1,0,0)
        val toDate = calendar.time

        val result = BpRecordDummyData.generateRecordList(30, fromDate, toDate)
        var lastDate = 0L
        result.forEach {
            assertTrue(lastDate<=it.dateAdded.time)
            lastDate = it.dateAdded.time
        }
    }
}