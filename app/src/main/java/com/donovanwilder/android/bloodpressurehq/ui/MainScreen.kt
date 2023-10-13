package com.donovanwilder.android.bloodpressurehq.ui

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.donovanwilder.android.bloodpressurehq.R
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import com.donovanwilder.android.bloodpressurehq.tools.DateTools
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Preview
@Composable
fun BloodPressureHqApp() {
    MainScreen({})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(changeToSettings: () -> Unit, viewModel: BpRecordsViewModel = viewModel()) {

    var dialogState by rememberSaveable { mutableStateOf(CurrentDialog.None) }
    var updateBpRecord: BpRecord? = null
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("BloodPressure HQ") }, actions = {
                IconButton(onClick = changeToSettings) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dialogState = CurrentDialog.Add_Record },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_record),
                        contentDescription = null
                    )
                })
        }
    ) {
        Box {

            Column(modifier = Modifier.padding(it)) {

                StatisticsScreen(modifier = Modifier.weight(1f), viewModel)
                RecordsScreen(
                    viewModel = viewModel,
                    updateRecord = {
                        updateBpRecord = it
                        dialogState = CurrentDialog.Update_Record
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp)
                )
            }
            when (dialogState) {
                CurrentDialog.Add_Record -> {
                    Dialog(
                        onDismissRequest = { dialogState = CurrentDialog.None },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true
                        ),
                        content = {
                            AddRecordDialog(
                                onAddButtonClicked = {
                                    viewModel.addRecords(it)
                                    dialogState = CurrentDialog.None
                                },
                                onCancelButtonClicked = { dialogState = CurrentDialog.None })
                        }
                    )
                }

                CurrentDialog.Update_Record -> {
                    Dialog(onDismissRequest = { dialogState = CurrentDialog.None },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true
                        ),
                        content = {
                            UpdateRecordDialog(
                                bpRecord = updateBpRecord!!,
                                onUpdateButtonClicked = {
                                    viewModel.updateRecord(it)
                                    dialogState = CurrentDialog.None
                                },
                                onCancelButtonClicked = { dialogState = CurrentDialog.None },
                                onDeleteButtonClicked = {
                                    dialogState = CurrentDialog.Delete_Record
                                })
                        })

                }

                CurrentDialog.Delete_Record -> {
                    Dialog(
                        onDismissRequest = { dialogState = CurrentDialog.Update_Record },
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        ),
                    ) {
                        ConfirmationDialog(
                            onConfirmation = {
                                viewModel.deleteRecord(updateBpRecord!!)
                                dialogState = CurrentDialog.None

                            },
                            onNegation = {
                                dialogState = CurrentDialog.Update_Record
                            }
                        )
                    }
                }

                else -> {}
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(modifier: Modifier = Modifier, viewModel: BpRecordsViewModel) {


    val recordList = viewModel.bpRecordsList.collectAsState()
    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text("Stats") })
    }) {


        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {context->
                LineChart(context)

            }) {
                val locale = it.context.resources.configuration.locales
                val xAxis = it.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = if(recordList.value.size >0) XAxisValueFormatter(recordList.value[0].dateAdded.time, locale[0]) else null
                val sysData = arrayListOf<Entry>()
                val diaData = arrayListOf<Entry>()
                val pulseData = arrayListOf<Entry>()
                recordList.value.toList().forEach { record ->
                    if (record.sys != 0) { // Todo: Change>> This implementation is sloppy along with the valueFormatter get rid of the subtraction
                        sysData.add(Entry((record.dateAdded.time - recordList.value.first().dateAdded.time ).toFloat(), record.sys.toFloat()))
                        diaData.add(Entry((record.dateAdded.time - recordList.value.first().dateAdded.time ).toFloat(), record.dia.toFloat()))
                        pulseData.add(
                            Entry(
                                (record.dateAdded.time - recordList.value.first().dateAdded.time  ).toFloat(),
                                record.pulse.toFloat()
                            )
                        )
                    }
                }

                val sysLineDataSet = LineDataSet(sysData, "sys").apply {
                    color = Color.BLUE
                }
                val diaLineDataSet = LineDataSet(diaData, "dia").apply {
                    color = Color.GREEN
                }
                val pulseLineDataSet = LineDataSet(pulseData, "pulse").apply {
                    color = Color.MAGENTA
                }

                val dataSet = listOf(sysLineDataSet, diaLineDataSet, pulseLineDataSet)
                it.description = null
                it.axisRight.isEnabled = false
                it.data = LineData(dataSet)

                it.invalidate()

            }
        }
    }
}

private class XAxisValueFormatter(val timeOffset: Long, val locale: Locale) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val date = Date(value.toLong() + timeOffset)
        val dateFormatter = SimpleDateFormat("MM/dd",locale )
        return dateFormatter.format(date)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    modifier: Modifier = Modifier,
    updateRecord: (bpRecord: BpRecord) -> Unit,
    viewModel: BpRecordsViewModel,

    ) {


    val recordList by viewModel.bpRecordsList.collectAsState()



    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Records") })
        },
    ) {
        LazyColumn(contentPadding = it, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(recordList.reversed()) {
                RecordItem(
                    it,
                    onClick = { updateRecord(it) }
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordItem(record: BpRecord, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            val locale = LocalContext.current.resources.configuration.locales[0]
            Column {
                val dateString = DateTools.getDateFormatter(locale).format(record.dateAdded)
                val timeString = DateTools.getTimeFormatter(locale).format(record.dateAdded)
                Text(text = dateString)
                Text(text = timeString)
            }
            Column {
                Text("sys")
                Text(record.sys.toString())
            }
            Column {
                Text("dia")
                Text(record.dia.toString())
            }
            Column {
                Text("pulse")
                Text(record.pulse.toString())
            }
        }
    }
}

enum class CurrentDialog {
    None,
    Add_Record,
    Update_Record,
    Delete_Record,
}