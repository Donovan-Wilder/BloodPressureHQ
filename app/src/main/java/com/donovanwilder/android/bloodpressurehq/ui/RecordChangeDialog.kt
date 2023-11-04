package com.donovanwilder.android.bloodpressurehq.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.donovanwilder.android.bloodpressurehq.tools.DateTools
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

@Preview
@Composable
fun AddRecordDialogPreview() {
    AddRecordDialog(onAddButtonClicked = {}, onCancelButtonClicked = {})
}

@Preview
@Composable
fun UpdateRecordDialogPreview() {
    UpdateRecordDialog(
        bpRecord = BpRecord(0, Date(), 120, 70, 60),
        onUpdateButtonClicked = {},
        onCancelButtonClicked = {},
        onDeleteButtonClicked = {})
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    ConfirmationDialog(onConfirmation = {}, onNegation = {})
}

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    onConfirmation: () -> Unit,
    onNegation: () -> Unit
) {

    Card {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Are you sure you want to delete this record?")
            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = onConfirmation) {
                    Text(text = "Yes")
                }
                Spacer(Modifier.width(16.dp))
                Button(onClick = onNegation) {
                    Text(text = "No")
                }
            }
        }
    }
}

@Composable
fun AddRecordDialog(
    modifier: Modifier = Modifier,
    onAddButtonClicked: (bpRecord: BpRecord) -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    ChangeRecordDialog(
        modifier = modifier,
        title = "New Record",
        actionButtonTitle = "Add",
        onActionButtonClicked = onAddButtonClicked,
        onCancelButtonClicked = onCancelButtonClicked,
        onDeleteButtonClicked = {}
    )
}

@Composable
fun UpdateRecordDialog(
    modifier: Modifier = Modifier,
    bpRecord: BpRecord,
    onUpdateButtonClicked: (updatedBpRecord: BpRecord) -> Unit,
    onCancelButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    ChangeRecordDialog(
        modifier = modifier,
        bpRecord = bpRecord,
        title = "Update Record",
        canChangeDate = true,
        actionButtonTitle = "Update",
        onActionButtonClicked = onUpdateButtonClicked,
        onCancelButtonClicked = onCancelButtonClicked,
        onDeleteButtonClicked = onDeleteButtonClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeRecordDialog(
    modifier: Modifier = Modifier,
    viewModel: RecordChangeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    bpRecord: BpRecord = BpRecord(dateAdded = Date(), sys = 120, dia = 70, pulse = 60),
    title: String,
    actionButtonTitle: String,
    canChangeDate: Boolean = false,
    onActionButtonClicked: (updatedBpRecord: BpRecord) -> Unit,
    onCancelButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    val calendar = GregorianCalendar.getInstance()
    calendar.time = bpRecord.dateAdded
    var currentDate by remember { mutableStateOf(bpRecord.dateAdded) }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            currentDate = calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour, min ->
            currentDate = calendar.apply {
                set(Calendar.HOUR, hour)
                set(Calendar.MINUTE, min)
            }.time
        },
        calendar.get(Calendar.HOUR),
        calendar.get(Calendar.MINUTE),
        false
    )

    viewModel.updateValues(bpRecord)
    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Max), verticalArrangement = Arrangement.Center
        ) {
            Text(text = title)
            val locale = LocalContext.current.resources.configuration.locales[0]
            if (canChangeDate) {
                Row {
                    Button(onClick = {
                        datePickerDialog.show()
                    }) {
                        Text(DateTools.getDateFormatter(locale).format(currentDate))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        timePickerDialog.show()
                    }) {
                        Text(DateTools.getTimeFormatter(locale).format(currentDate))
                    }
                }
            } else {
                Text(text = DateTools.getDateFormatter(locale).format(currentDate))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Sys", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = viewModel.sys,
                    onValueChange = {
                        if (viewModel.validateInput(it)) {
                            viewModel.sys = it
                        }
                    },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dia", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = viewModel.dia,
                    onValueChange = {
                        if(viewModel.validateInput(it)){
                            viewModel.dia = it
                        }
                                                       },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Pulse", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = viewModel.pulse,
                    onValueChange = {
                        if(viewModel.validateInput(it)){
                            viewModel.pulse = it
                        }
                                                       },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {

                        if(viewModel.sys.isBlank()||viewModel.dia.isBlank()||viewModel.pulse.isBlank()){
                            viewModel.sys = "0"
                            viewModel.dia = "0"
                            viewModel.pulse = "0"
                        }
                        val record = BpRecord(
                            id = bpRecord.id,
                            dateAdded = currentDate,
                            sys = viewModel.sys.toInt(),
                            dia = viewModel.dia.toInt(),
                            pulse = viewModel.pulse.toInt()
                        )
                        viewModel.updateValues(record)
                        onActionButtonClicked(record)
                    }
                ) {
                    Text(text = actionButtonTitle)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onCancelButtonClicked) {
                    Text(text = "Cancel")
                }
                if (canChangeDate) {
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick =
                        onDeleteButtonClicked
                    ) {
                        Text(text = "Delete")
                    }
                }

            }
        }
    }
}