package com.donovanwilder.android.bloodpressurehq

import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.random.Random

fun main(){
    val calendar = GregorianCalendar.getInstance().apply {
        set(Calendar.YEAR,2020)
        set(Calendar.MONTH,0)
        set(Calendar.DAY_OF_MONTH,1)
    }
    val fromDate = calendar.time
    calendar.apply {
        set(Calendar.YEAR,2023)
        set(Calendar.MONTH,8)
        set(Calendar.DAY_OF_MONTH,1)
    }
    val toDate = calendar.time
    BpRecordGenerator.generate(50,fromDate,toDate)
}
class BpRecordGenerator {
    companion object {
        fun generate(numberOfRecords:Int, fromDate: Date, toDate: Date,  file: File = File("test.txt")) {

            val file =file
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