package com.donovanwilder.android.bloodpressurehq.data.fake

import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.random.Random

fun main() {
    val calendar= GregorianCalendar.getInstance()
    val toDate = calendar.time
    calendar.add(Calendar.DAY_OF_MONTH,-7)
    val fromDate = calendar.time
    BpRecordDummyData.generateFile(75, fromDate, toDate)
}

class BpRecordDummyData {
    companion object {
        private val calendar = GregorianCalendar.getInstance()
        fun generateRecordList(numberOfRecords: Int, fromDate: Date, toDate: Date): List<BpRecord> {
            val recordList = arrayListOf<BpRecord>()
            for (i in 0 until numberOfRecords) {
                val dateAdded = Date(Random(Date().time * i).nextLong(fromDate.time, toDate.time))
                val sys = Random(Date().time * i).nextInt(88, 170)
                val dia = Random(Date().time * i).nextInt(60, 90)
                val pulse = Random(Date().time * i).nextInt(30, 200)
                recordList.add(BpRecord(0, dateAdded, sys, dia, pulse))
            }
            return recordList.sortedBy { it.dateAdded.time }
        }

        fun generateFile(
            numberOfRecords: Int,
            fromDate: Date,
            toDate: Date,
            file: File = File("test.txt")
        ) {

            val file = file
            val writer = file.writer()

            writer.write("_id, date_added, sys, dia, pulse\n")

            for (i in 0..numberOfRecords) {


                val dateAdded = Random(Date().time * i).nextLong(fromDate.time, toDate.time)
                val sys = Random(Date().time * i).nextInt(88, 170)
                val dia = Random(Date().time * i).nextInt(60, 90)
                val pulse = Random(Date().time * i).nextInt(30, 200)

                writer.write("$i, $dateAdded, $sys, $dia, $pulse\n")
            }
            writer.flush()
            writer.close()

        }

        fun getDailyRecordList(start: Date, daysAfter: Int, recordsPerDay: Int): List<BpRecord> {

            val newRecordList = arrayListOf<BpRecord>()
            calendar.time = start
            for (i in 1..daysAfter) {
                val (startDate, endDate) = getTimePeriod()
                val record = generateRecordList(recordsPerDay, startDate, endDate)
                for (n in record) {
                    newRecordList.add(n)
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            return newRecordList
        }

        /***
         * This receives a Date and returns the full scope of the day
         */
        private fun getTimePeriod(): Pair<Date, Date> {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            val startDate = calendar.time
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endDate = calendar.time
            return Pair(startDate, endDate)
        }
    }
}