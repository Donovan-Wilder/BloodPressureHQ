package com.donovanwilder.android.bloodpressurehq.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.donovanwilder.android.bloodpressurehq.R
import com.github.mikephil.charting.charts.LineChart


@Preview
@Composable
fun BloodPressureHqApp() {
    ScreenLayout()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLayout() {
    Column {
        StatisticsDisplay(Modifier.weight(1f))
        RecordsDisplay(Modifier.weight(1f))
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
            Row {
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
            AndroidView(factory = {
                LineChart(it)
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsDisplay(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Records") })
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(listOf(1, 5, 2, 3, 74, 5, 8, 7, 8, 63, 5, 4, 85)) {
                RecordItem(item = it)
            }
        }

    }
}

@Composable
fun RecordItem(item: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row {
            Text(text = item.toString())
        }
    }
}
