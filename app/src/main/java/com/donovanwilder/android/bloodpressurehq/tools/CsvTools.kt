package com.donovanwilder.android.bloodpressurehq.tools

import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import java.util.*

class CsvTools {
    companion object {
        fun createBpRecordList(csvString: String): List<BpRecord> {
            val listOfRecords = arrayListOf<BpRecord>()
            val lines = csvString.lines()
            for (i in 1 until lines.size) {
                val line = lines[i]
                if(line == ""){
                    break
                }
                val lineSplits = line.split(",", ignoreCase = true, limit = 0)
                val record = BpRecord(
                    dateAdded = Date(lineSplits[1].trim().toLong()),
                    sys = lineSplits[2].trim().toInt(),
                    dia = lineSplits[3].trim().toInt(),
                    pulse = lineSplits[4].trim().toInt()
                )
                listOfRecords.add(record)
            }
            return listOfRecords
        }
        fun createCsvString(bpRecords: List<BpRecord>): String {

            val stringBuilder = StringBuilder("_id, date_added, sys, dia, pulse")
            bpRecords.forEach { record ->
                stringBuilder.append("\n")
                stringBuilder.append("${record.id}, ${record.dateAdded.time}, ${record.sys}, ${record.dia}, ${record.pulse}")
            }

            return stringBuilder.toString()
        }
    }
}
