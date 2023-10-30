package com.donovanwilder.android.bloodpressurehq.ui

import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class RecordChangeViewModelTest {
    @Test
    fun Should_IgnoreCharter_When_ItIsNonnumeric() {
        val input1 = "a"
        val input2 = "."

        val viewModel = RecordChangeViewModel()
        val result1 = viewModel.validateInput(input1)
        val result2 = viewModel.validateInput(input2)

        assertFalse(result1)
        assertFalse(result2)
    }

    @Test
    fun Should_NotValidate_When_NotBetween0And500() {
        val input = arrayOf(-1, 501, 700, -100, -700)

        val viewModel = RecordChangeViewModel()

        input.forEach {
            val result = viewModel.validateInput(it.toString())
            assertFalse( result)
        }
    }
    @Test
    fun Should_UpdateValues(){
        val viewModel = RecordChangeViewModel()
        val record = BpRecord(5, Date(155), 155,66, 44)

        viewModel.updateValues(record)

        assertEquals(record.sys.toString(), viewModel.sys)
        assertEquals(record.dia.toString(), viewModel.dia)
        assertEquals(record.pulse.toString(), viewModel.pulse)


    }
}