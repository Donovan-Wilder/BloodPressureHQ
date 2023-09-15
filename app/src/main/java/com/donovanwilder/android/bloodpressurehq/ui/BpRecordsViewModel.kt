package com.donovanwilder.android.bloodpressurehq.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donovanwilder.android.bloodpressurehq.data.BpRecord
import com.donovanwilder.android.bloodpressurehq.data.BpRecordRepository
import kotlinx.coroutines.launch
import java.util.*

class BpRecordsViewModel : ViewModel() {
    private val repository = BpRecordRepository.getInstance()


    fun getAllRecords() = repository.getAllRecords()
    fun updateRecord(id: Int, sysValue: Int, diaValue: Int, pulseValue: Int, dateAdded: Date) {
        viewModelScope.launch {
            repository.updateRecord(id, sysValue, diaValue, pulseValue, dateAdded)
        }
    }

    fun updateRecord(bpRecord: BpRecord) {
        updateRecord(bpRecord.id, bpRecord.sys, bpRecord.dia, bpRecord.pulse, bpRecord.dateAdded)
    }

    fun deleteRecord(bpRecord: BpRecord) {
        viewModelScope.launch {
            repository.deleteRecord(bpRecord)
        }
    }


}