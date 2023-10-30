package com.donovanwilder.android.bloodpressurehq.ui

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donovanwilder.android.bloodpressurehq.R


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen({}, {}, {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onImportCsv: (Intent) -> Unit,
    onExportCsv: (Intent) -> Unit,
    onReportIssue: (Intent) -> Unit,
    onBackPressed: () -> Unit
) {

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Settings") },
            navigationIcon = {
                IconButton(onClick = { onBackPressed.invoke() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
    }) {
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
                Text(text = stringResource(R.string.import_from_file))
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
                Text(text = stringResource(R.string.export_to_file))
            }
            Row(
                Modifier
                    .border(1.dp, Color.Gray)
                    .clickable {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_EMAIL, "info@donovanwilder.com")
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TITLE, "BpRecords")
                        }
                        onReportIssue(intent)

                    }
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.report_issue))
            }
        }

    }
}