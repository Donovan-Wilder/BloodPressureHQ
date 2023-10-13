package com.donovanwilder.android.bloodpressurehq.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.donovanwilder.android.bloodpressurehq.BpRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.List

class BpRecordsViewModel : ViewModel() {
    private val repository = BpRecordRepository.getInstance()

    val bpRecordsList: StateFlow<List<BpRecord>> = repository.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun getAllRecords(): Flow<List<BpRecord>> {

        return repository.getAllRecords()
    }

    fun addRecords(vararg record: BpRecord) {
        viewModelScope.launch { repository.addRecord(*record) }
    }

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