package com.donovanwilder.android.bloodpressurehq.tools

import org.junit.Assert.*
import org.junit.Test

class CsvToolsTest {
    @Test
    fun Should_OutputCorrectNumberOfRecords_When_CreatingBpRecordList() {
        val csvString =
            """
                _id, date_added, sys, dia, pulse
                0, 100, 120, 70, 60
                1, 400, 125, 75, 65
                2, 600, 130, 80, 70
                3, 800, 135, 85, 75
            """.trimIndent()
        val result = CsvTools.createBpRecordList(csvString)

        assertEquals(4, result.size)
    }

    @Test
    fun Should_OutputCorrectValuesOfRecords_When_CreatingBpRecordList() {
        val csvString =
            """
                _id, date_added, sys, dia, pulse
                0, 100, 120, 70, 60
                1, 200, 125, 75, 65
                2, 300, 130, 80, 70
                3, 400, 135, 85, 75
            """.trimIndent()
        val result = CsvTools.createBpRecordList(csvString)

        var ( dateAdded, sys, dia, pulse) = arrayOf(100, 120, 70, 60)
        for (record in result) {

            assertEquals(dateAdded.toLong(), record.dateAdded.time)
            assertEquals(sys, record.sys)
            assertEquals(dia, record.dia)
            assertEquals(pulse, record.pulse)
            dateAdded += 100
            sys += 5
            dia += 5
            pulse += 5
        }
    }
}