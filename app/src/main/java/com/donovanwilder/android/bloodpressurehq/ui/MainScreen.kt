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


@Preview
@Composable
fun BloodPressureHqApp() {
    MainScreen({})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(changeToSettings:()->Unit, viewModel: BpRecordsViewModel = viewModel()) {

    var dialogState by rememberSaveable { mutableStateOf(CurrentDialog.None) }
    var updateBpRecord: BpRecord? = null
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("BloodPressure HQ") }, actions = {
            IconButton(onClick = changeToSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dialogState = CurrentDialog.Add_Record },
                content = {
                    Icon(painter = painterResource(id = R.drawable.ic_add_record), contentDescription = null )
                })
        }
    ) {
        Box {

            Column(modifier = Modifier.padding(it)) {

                StatisticsScreen(modifier = Modifier.weight(1f),viewModel)
                RecordsScreen(
                    viewModel = viewModel,
                    updateRecord = {
                    updateBpRecord = it
                    dialogState = CurrentDialog.Update_Record
                }, modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 16.dp))
            }
            when (dialogState) {
                CurrentDialog.Add_Record -> {
                    Dialog(
                        onDismissRequest = { dialogState = CurrentDialog.None },
                        properties = DialogProperties(true, true),
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
                        properties = DialogProperties(true, true),
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
                        properties = DialogProperties(false, false),
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
fun StatisticsScreen(modifier: Modifier = Modifier,viewModel: BpRecordsViewModel) {


    val dailyAverages = viewModel.bpRecordDailyAvergeList.collectAsState()
    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text("Stats") })
    }) {


        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                LineChart(it)

            }){
                val xAxis = it.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = XAxisValueFormmater()
                val sysData = arrayListOf<Entry>()
                val diaData = arrayListOf<Entry>()
                val pulseData = arrayListOf<Entry>()
                dailyAverages.value.toList().reversed().forEach { record ->
                    if(record.sys!=0) {
                        sysData.add(Entry(record.dateAdded.time.toFloat(), record.sys.toFloat()))
                        diaData.add(Entry(record.dateAdded.time.toFloat(), record.dia.toFloat()))
                        pulseData.add(
                            Entry(
                                record.dateAdded.time.toFloat(),
                                record.pulse.toFloat()
                            )
                        )
                    }
                    }

                val sysLineDataSet = LineDataSet(sysData,"sys").apply {
                    color = Color.BLUE
                }
                val diaLineDataSet = LineDataSet(diaData, "dia").apply {
                    color = Color.GREEN
                }
                val pulseLineDataSet = LineDataSet(pulseData, "pulse").apply {
                    color = Color.LTGRAY
                }

                val dataSet = listOf(sysLineDataSet,diaLineDataSet, pulseLineDataSet)
                it.description = null
                it.axisRight.isEnabled = false
                it.data = LineData(dataSet)
                it.invalidate()
            }
        }
    }
}
 private class XAxisValueFormmater: ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        val date = Date(value.toLong())
        val dateFormatter = SimpleDateFormat("MM/dd")
        val display = dateFormatter.format(date)
        return display
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
        modifier = Modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column {
                val dateString = DateTools.getDateFormatter().format(record.dateAdded)
                val timeString = DateTools.getTimeFormatter().format(record.dateAdded)
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

enum class CurrentDialog() {
    None,
    Add_Record,
    Update_Record,
    Delete_Record,
}