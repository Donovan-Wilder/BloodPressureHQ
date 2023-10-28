package com.donovanwilder.android.bloodpressurehq.tools

import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import org.junit.Assert.*
import org.junit.Test
import java.util.Date
import java.util.Scanner
import java.util.regex.Pattern

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

        var (dateAdded, sys, dia, pulse) = arrayOf(100, 120, 70, 60)
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

    @Test
    fun Should_CreateTheRightNumberOfLines_When_CreatingACsvString() {
        val recordData = listOf(
            BpRecord(0, Date(500), 120, 85, 60),
            BpRecord(0, Date(400), 115, 80, 55),
            BpRecord(0, Date(300), 110, 75, 50)
        )
        val result = CsvTools.createCsvString(recordData)
        val scanner = Scanner(result)

        var lineCounter = -1 // Subtract one for the column names line
        while (scanner.hasNextLine()) {
            scanner.nextLine()
            lineCounter++
        }

        assertEquals(recordData.size, lineCounter)
    }

    @Test
    fun Should_CreateTheRightValuesInRecord_When_CreatingACsvString() {

        val recordData = listOf(
            BpRecord(0, Date(500), 120, 85, 60),
            BpRecord(0, Date(400), 115, 80, 55),
            BpRecord(0, Date(300), 110, 75, 50)
        )
        val result = CsvTools.createCsvString(recordData)
        val scanner = Scanner(result)

        val parsingRegex = "^(\\d+),\\s?(\\d+),\\s?(\\d+),\\s?(\\d+),\\s?(\\d+)"
        val pattern = Pattern.compile(parsingRegex)


        scanner.nextLine()
        var index = 0
        while (scanner.hasNextLine()) {
            val line = scanner.nextLine()
            val matcher = pattern.matcher(line)
            println("index: $index\nrecord: ${recordData[index]}\nline: $line\nmatcher: $matcher.")
            if (matcher.find()) {
                assertEquals(
                    recordData[index].dateAdded.time,
                    matcher.group(2)?.toLong() ?: throw Exception("No group value")
                )
                assertEquals(
                    recordData[index].sys,
                    matcher.group(3)?.toInt() ?: throw Exception("No group value")
                )
                assertEquals(
                    recordData[index].dia,
                    matcher.group(4)?.toInt() ?: throw Exception("No group value")
                )
                assertEquals(
                    recordData[index].pulse,
                    matcher.group(5)?.toInt() ?: throw Exception("No group value")
                )
                index++
            }
        }

    }
}