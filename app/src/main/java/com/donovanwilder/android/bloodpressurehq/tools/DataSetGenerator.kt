package com.donovanwilder.android.bloodpressurehq.tools

import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.github.mikephil.charting.data.Entry
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.abs

class DataSetGenerator {
    companion object {
        fun generateDailyRecordAverageList(recordList: List<BpRecord>): List<BpRecord> {
            if (recordList.size == 0) {
                return listOf()
            }
            val calendar = Calendar.getInstance()

            var timeMark = calendar.time

            var (sysValue, diaValue, pulseValue, numberofRecords) = arrayOf(0, 0, 0, 0)
            val newRecordList = arrayListOf<BpRecord>()

            for (record in recordList) {

                if (record.dateAdded.before(timeMark)) {
                    // Wrap up
                    if (numberofRecords > 0) {
                        val newRecord = BpRecord(
                            0,
                            timeMark,
                            sysValue / numberofRecords,
                            diaValue / numberofRecords,
                            pulseValue / numberofRecords
                        )
                        newRecordList.add(newRecord)
                    }
                    // Reset the values
                    sysValue = 0; diaValue = 0; pulseValue = 0; numberofRecords = 0

                    //Set TimeMark
                    calendar.time = record.dateAdded
                    calendar.apply {
                        set(Calendar.MINUTE, 0)
                        set(Calendar.HOUR_OF_DAY, 0)
                    }
                    timeMark = calendar.time

                }
                // Collect
                sysValue += record.sys
                diaValue += record.dia
                pulseValue += record.pulse
                numberofRecords++

            }
            // Wrap up
            if (numberofRecords > 0) {
                val newRecord = BpRecord(
                    0,
                    timeMark,
                    sysValue / numberofRecords,
                    diaValue / numberofRecords,
                    pulseValue / numberofRecords
                )
                newRecordList.add(newRecord)
            }
            return newRecordList
        }
    }
}


enum class TimeFrame() {
    Daily,
    Monthly,
    Weekly,
    Yearly
}