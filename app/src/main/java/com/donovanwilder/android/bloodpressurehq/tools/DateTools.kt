package com.donovanwilder.android.bloodpressurehq.tools

import java.text.SimpleDateFormat
import java.util.Locale

class DateTools {
    companion object {
        fun getDateFormatter(locale: Locale) = SimpleDateFormat("MM/dd/yyy", locale)
        fun getTimeFormatter(locale: Locale) = SimpleDateFormat("hh:mm a", locale)

    }
}