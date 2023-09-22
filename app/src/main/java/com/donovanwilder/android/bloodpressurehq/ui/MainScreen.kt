package com.donovanwilder.android.bloodpressurehq.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
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


@Preview
@Composable
fun BloodPressureHqApp() {
    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: BpRecordsViewModel = viewModel()) {
    var dialogState by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("BloodPressure HQ") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dialogState = true },
                content = {})
        }
    ) {
        Box {

            Column(modifier = Modifier.padding(it)) {

                StatisticsScreen(modifier = Modifier.weight(1f))
                RecordsScreen(modifier = Modifier.weight(1f))
            }
            if (dialogState == true) {
                Dialog(
                    onDismissRequest = { dialogState = false },
                    properties = DialogProperties(true, true),
                    content = {
                        NewRecordDialog(
                            onAddButttonClicked = {
                                viewModel.addRecords(it)
                                dialogState = false
                            },
                            onCancelButtonClicked = { dialogState = false })
                    }
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {

    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text("Stats") })
    }) {


        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_left_arrow),
                        contentDescription = null
                    )
                }
                Text(text = "This Week")
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right_arrow),
                        contentDescription = null
                    )
                }
            }
            AndroidView(modifier = Modifier.fillMaxWidth(), factory = {
                LineChart(it)
            })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    bpRecordsViewModel: BpRecordsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {


    val recordList by bpRecordsViewModel.bpRecordsList.collectAsState()



    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Records") })
        },
    ) {
        LazyColumn(contentPadding = it, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(recordList) {
                RecordItem(it, modifier = modifier.padding(16.dp))
            }
        }

    }

}

@Composable
fun RecordItem(record: BpRecord, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
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
