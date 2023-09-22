package com.donovanwilder.android.bloodpressurehq.ui

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
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donovanwilder.android.bloodpressurehq.model.BpRecord
import java.util.Date

@Preview
@Composable
fun DialogPreview() {
    NewRecordDialog(onAddButttonClicked = {}, onCancelButtonClicked = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecordDialog(
    modifier: Modifier = Modifier,
    viewModel: RecordChangeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onAddButttonClicked: (bpRecord: BpRecord) -> Unit,
    onCancelButtonClicked: () -> Unit
) {

    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Max), verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Add New Record")
            Text(text = "")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Sys", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = viewModel.sys,
                    onValueChange = { viewModel.sys = it },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dia", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = viewModel.dia,
                    onValueChange = { viewModel.dia = it },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Pulse", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = viewModel.pulse,
                    onValueChange = { viewModel.pulse = it },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row() {
                Button(
                    onClick = {
                        onAddButttonClicked( BpRecord(
                                dateAdded = Date(),
                                sys = viewModel.sys.toInt(),
                                dia = viewModel.dia.toInt(),
                                pulse = viewModel.pulse.toInt()
                            )
                        )
                    }
                ) {
                    Text(text = "Add")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onCancelButtonClicked) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}