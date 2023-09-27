package com.donovanwilder.android.bloodpressurehq.tools

import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import java.io.File
import java.util.Date
import kotlin.random.Random

class BpRecordDummyData {
    companion object {
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
    }
}