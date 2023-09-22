package com.donovanwilder.android.bloodpressurehq.ui

import java.util.Date

data class RecordChangeState (
    val Date: Date,
    val sys: Int,
    val dia: Int,
    val pulse: Int,
)