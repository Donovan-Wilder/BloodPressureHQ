package com.donovanwilder.android.bloodpressurehq.ui

import android.provider.Settings.Global
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.donovanwilder.android.bloodpressurehq.R
import com.donovanwilder.android.bloodpressurehq.data.BpRecord
import com.donovanwilder.android.bloodpressurehq.tools.DateTools
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Date


@Preview
@Composable
fun BloodPressureHqApp() {
    ScreenLayout()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLayout() {
    Column {
        StatisticsDisplay(modifier = Modifier.weight(1f))
        RecordsDisplay(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsDisplay(modifier: Modifier = Modifier) {

    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text("Stats") })
    }) {


        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (verticalAlignment = Alignment.CenterVertically){
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
            AndroidView(modifier=Modifier.fillMaxWidth(), factory = {
                LineChart(it)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsDisplay(bpRecordsViewModel: BpRecordsViewModel = viewModel(),modifier: Modifier = Modifier) {

    var recordList by remember{ mutableStateOf(bpRecordsViewModel.bpRecordList) }
    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Records") })
    }) {
        LazyColumn( contentPadding = it, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(recordList) {
                RecordItem(it, modifier = modifier.padding(16.dp))
            }
        }

    }
}

@Composable
fun RecordItem(record:BpRecord, modifier: Modifier = Modifier) {
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
