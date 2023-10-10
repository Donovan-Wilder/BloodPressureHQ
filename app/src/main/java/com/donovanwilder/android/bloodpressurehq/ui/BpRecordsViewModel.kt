package com.donovanwilder.android.bloodpressurehq.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.donovanwilder.android.bloodpressurehq.BpRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
            initialValue = emptyList<BpRecord>()
        )

    val bpRecordDailyAvergeList: StateFlow<List<BpRecord>> = getDailyRecordAverageList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList<BpRecord>()
    )

    fun getDailyRecordAverageList() = flow {
        bpRecordsList.collect {
            val outputList = arrayListOf<BpRecord>()
            val calendar = Calendar.getInstance()
            var toDate = calendar.time
            calendar.apply {
                set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
            }
            var fromDate = calendar.time

            val earliestDate = repository.getEarliestDate()
            while (fromDate.after(earliestDate)) {

                val record = repository.getAvgFromDateRange(fromDate, toDate)
                outputList.add(record)

                calendar.add(Calendar.DAY_OF_MONTH, -1)
                calendar.apply {
                    set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
                    set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
                    set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
                    set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
                }
                fromDate = calendar.time
                calendar.apply {
                    set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
                    set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
                    set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND))
                    set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
                }
                toDate = calendar.time

            }
            emit(outputList)
        }
    }


    suspend fun getWeeklyRecordAverageList(): List<BpRecord> {
        val outputList = arrayListOf<BpRecord>()
        val calendar = Calendar.getInstance()
        var toDate = calendar.time
        calendar.apply {
            set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
            set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
            set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
            set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
        }
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        var fromDate = calendar.time

        val earliestDate = repository.getEarliestDate()
        while (fromDate.after(earliestDate)) {
            val record = repository.getAvgFromDateRange(fromDate, toDate)
            outputList.add(record)


            calendar.apply {
                add(Calendar.MILLISECOND, -1)
                set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
            }
            toDate = calendar.time
            calendar.apply {
                add(Calendar.DAY_OF_MONTH, -7)
                set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
            }
            fromDate = calendar.time

        }

        return outputList
    }

    suspend fun getMonthlyRecordAverageList(): List<BpRecord> {
        val outputList = arrayListOf<BpRecord>()
        val calendar = Calendar.getInstance()
        var toDate = calendar.time
        calendar.apply {
            set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
            set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
            set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
            set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
        }
        var fromDate = calendar.time

        val earliestDate = repository.getEarliestDate()
        while (fromDate.after(earliestDate)) {
            val record = repository.getAvgFromDateRange(fromDate, toDate)
            outputList.add(record)


            calendar.apply {
                add(Calendar.MILLISECOND, -1)
                set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
            }
            toDate = calendar.time
            calendar.apply {
                add(Calendar.MONTH, -1)
                set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
            }
            fromDate = calendar.time

        }

        return outputList
    }

    suspend fun getYearlyRecordAverageList(): List<BpRecord> {
        val outputList = arrayListOf<BpRecord>()
        val calendar = Calendar.getInstance()
        var toDate = calendar.time
        calendar.apply {
            set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH))
            set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
            set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
            set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
            set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
        }
        calendar.add(Calendar.MONTH, -1)
        var fromDate = calendar.time

        val earliestDate = repository.getEarliestDate()
        while (fromDate.after(earliestDate)) {
            val record = repository.getAvgFromDateRange(fromDate, toDate)
            outputList.add(record)


            calendar.apply {
                add(Calendar.MILLISECOND, -1)
                set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND))
            }
            toDate = calendar.time
            calendar.apply {
                add(Calendar.MONTH, -1)
                set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE))
                set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY))
                set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND))
                set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND))
            }
            fromDate = calendar.time

        }

        return outputList
    }

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