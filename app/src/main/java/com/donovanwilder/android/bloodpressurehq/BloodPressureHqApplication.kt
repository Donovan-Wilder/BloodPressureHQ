package com.donovanwilder.android.bloodpressurehq

import android.app.Application
import com.donovanwilder.android.bloodpressurehq.data.BpRecordRepository

class BloodPressureApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        BpRecordRepository.initialize(this)
    }
}