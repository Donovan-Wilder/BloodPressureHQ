package com.donovanwilder.android.bloodpressurehq

import android.app.Application

class BloodPressureApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        BpRecordRepository.initialize(this)
    }
}