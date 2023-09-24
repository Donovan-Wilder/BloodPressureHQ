package com.donovanwilder.android.bloodpressurehq.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen({},{})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onImportCsv:(Intent)->Unit,onExportCsv:(Intent)->Unit) {

    val toast = Toast.makeText(LocalContext.current, "File Imported", Toast.LENGTH_SHORT)
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = "Settings") }) }) {
        Column(modifier = Modifier.padding(it)) {
            Row(
                Modifier
                    .border(1.dp, Color.Gray)
                    .clickable {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "text/plain"
                        }

                        onImportCsv(intent)
                    }
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Import from File")
            }
            Row(
                Modifier
                    .border(1.dp, Color.Gray)
                    .clickable {
                        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TITLE, "BpRecords")
                        }

                        onExportCsv(intent)
                    }
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Export to File")
            }
        }

    }
}