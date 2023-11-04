package com.donovanwilder.android.bloodpressurehq.database

import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class BpRecordTypeConvertersTest{
    @Test
    fun Should_ConvertDateToLong(){
        val expected = 100L
        val date = Date(expected)


        val typeConverter = BpRecordTypeConverters()
        val result = typeConverter.dateToTimestamp(date)

        assertEquals(expected, result)
    }

    @Test
    fun Should_ConvertLongToDate(){
        val long = 200L
        val expected = Date(long)

        val typeConverter = BpRecordTypeConverters()
        val result = typeConverter.fromTimestamp(long)
        assertEquals(expected, result)
    }
}