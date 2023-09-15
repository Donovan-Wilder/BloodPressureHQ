package com.donovanwilder.android.bloodpressurehq.tools

import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter

class DateTools {
    companion object {
        fun getDateFormatter() = SimpleDateFormat("MM/dd/yyy")
        fun getTimeFormatter() = SimpleDateFormat("hh:mm a")

    }
}