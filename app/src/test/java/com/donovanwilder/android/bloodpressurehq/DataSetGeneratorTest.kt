package com.donovanwilder.android.bloodpressurehq

import com.donovanwilder.android.bloodpressurehq.data.fake.BpRecordDummyData
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.donovanwilder.android.bloodpressurehq.tools.DataSetGenerator
import com.donovanwilder.android.bloodpressurehq.tools.TimeFrame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar


class DataSetGeneratorTest {
    @Test
    fun Should_OutputOneRecord_When_AllRecordsExistInADay(){
        val calendar = GregorianCalendar.getInstance()
        calendar.set(
            2023,
            8,
            20,
            calendar.getActualMinimum(Calendar.HOUR_OF_DAY),
            calendar.getActualMinimum(Calendar.MINUTE)
        )
        val fromDate = calendar.time
        calendar.set(
            2023,
            8,
            20,
            calendar.getActualMaximum(Calendar.HOUR_OF_DAY),
            calendar.getActualMaximum(Calendar.MINUTE)
        )
        val toDate = calendar.time

        val data = BpRecordDummyData.generateRecordList(5,fromDate, toDate)

        val expected = 1

        val result = DataSetGenerator.generateDailyRecordAverageList(TimeFrame.Daily, data)

        assertEquals(expected, result.size)
    }

    @Test
    fun Should_OutputMultipleRecords_When_RecordsExistAcrossSeveralDays(){
        val calendar = GregorianCalendar.getInstance()
        calendar.set(
            2023,
            8,
            20,
            calendar.getActualMinimum(Calendar.HOUR_OF_DAY),
            calendar.getActualMinimum(Calendar.MINUTE)
        )
        val fromDate = calendar.time
        calendar.set(
            2023,
            8,
            27,
            calendar.getActualMaximum(Calendar.HOUR_OF_DAY),
            calendar.getActualMaximum(Calendar.MINUTE)
        )
        val toDate = calendar.time

        val data = BpRecordDummyData.generateRecordList(25,fromDate, toDate)//Todo: Fix this test depends on randomness come up with a solution to get more consistent data


        val expected = 8

        val result = DataSetGenerator.generateDailyRecordAverageList(TimeFrame.Daily, data)

        assertEquals(expected, result.size)
    }
    @Test
    fun Should_OutputMultiple_When_RecordsExistAcrossSeveralDays(){
        val calendar = GregorianCalendar.getInstance()
        val data = arrayListOf<BpRecord>()
        val expected = 30
        for (i in 1..expected){
           data.add(BpRecord(0,calendar.time, 120, 70, 60))
           calendar.add(Calendar.DAY_OF_MONTH, -1)
        }


        val results = DataSetGenerator.generateDailyRecordAverageList(TimeFrame.Daily, data)

        assertEquals(expected, results.size)
    }
    @Test
    fun Should_OutputTheAverage_When_SpanAcrossADay() {
        val calendar = GregorianCalendar.getInstance()
        calendar.set(
            2023,
            8,
            20,
            calendar.getActualMinimum(Calendar.HOUR_OF_DAY),
            calendar.getActualMinimum(Calendar.MINUTE)
        )
        val fromDate = calendar.time
        calendar.set(
            2023,
            8,
            20,
            calendar.getActualMaximum(Calendar.HOUR_OF_DAY),
            calendar.getActualMaximum(Calendar.MINUTE)
        )
        val toDate = calendar.time

        val data = BpRecordDummyData.generateRecordList(2,fromDate, toDate)

        var (sys,dia,pulse)  = arrayOf(0,0,0)

        data.forEach {
            sys += it.sys
            dia += it.dia
            pulse += it.pulse
        }

        val expected = BpRecord(0, Date(), sys / data.size, dia / data.size, pulse / data.size)

        val result = DataSetGenerator.generateDailyRecordAverageList(TimeFrame.Daily, data)

        assertEquals("expected:\n${printRecords(data)}",expected.sys, result.get(0).sys)
        assertEquals(expected.dia, result.get(0).dia)
        assertEquals(expected.pulse, result.get(0).pulse)
    }

    @Test
    fun Should_OutputOneRecord_When_AllRecordsExistInAWeek(){
        val calendar = GregorianCalendar.getInstance()
        calendar.clear()
        calendar.set(
            Calendar.WEEK_OF_YEAR,
            32
        )
        val fromDate = calendar.time

        calendar.set(
            Calendar.WEEK_OF_YEAR,
            33
        )
        calendar.add(Calendar.MINUTE,-1)
        val toDate = calendar.time

        val data = BpRecordDummyData.generateRecordList(50,fromDate, toDate)

        val expected = 1

        val result = DataSetGenerator.generateDailyRecordAverageList(TimeFrame.Weekly, data)

        assertEquals(expected, result.size)
    }
    @Test
    fun Should_OutputMultiple_When_RecordsExistAcrossSeveralWeeks(){
        assertTrue(false)
    }
    @Test
    fun Should_OutputTheAverage_When_SpanAcrossAWeek() {
        assertTrue(false)
    }
    private fun printRecords(recordList: List<BpRecord>): String{
       val string = StringBuilder()
       recordList.forEach {
           string.append("$it \n")
       }
        return string.toString()
    }
}