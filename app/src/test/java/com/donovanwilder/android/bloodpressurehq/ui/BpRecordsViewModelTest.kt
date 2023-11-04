package com.donovanwilder.android.bloodpressurehq.ui

import com.donovanwilder.android.bloodpressurehq.BpRecordRepository
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.Date


class BpRecordsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: BpRecordRepository
    private lateinit var viewModel: BpRecordsViewModel

    @Before
    fun setup() {
        repository = mockk()
        every { repository.getAllRecords() } returns flow { emit(emptyList()) }
        coEvery { repository.deleteRecord(any()) }
        coEvery { repository.updateRecord(any(), any(), any(), any(), any()) }
        viewModel = BpRecordsViewModel(repository)

    }

    @Test
    fun Should_BeAbleToDeleteRecord() {
        val record = BpRecord(0, Date(1), 145, 78, 48)
        viewModel.deleteRecord(record)
        coVerify { repository.deleteRecord(any()) }
    }

    @Test
    fun Should_BeAbleToAddRecord() {
        val record = BpRecord(0, Date(1), 145, 78, 48)
        viewModel.addRecords(record)
        coVerify { repository.addRecord(any()) }
    }

    @Test
    fun Should_BeAbleToUpdateRecord() {
        val record = BpRecord(0, Date(1), 145, 78, 48)
        viewModel.updateRecord(record)
        coVerify { repository.updateRecord(any(), any(), any(), any(), any()) }
    }

    @Test
    fun Should_BeAbleToGetRecord() {
        viewModel.getAllRecords()
        //This method is also called at an instantiation of the BpRecordView model to initialize an member variable
        coVerify { repository.getAllRecords() }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}