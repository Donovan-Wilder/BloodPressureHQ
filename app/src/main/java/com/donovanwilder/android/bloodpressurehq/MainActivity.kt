package com.donovanwilder.android.bloodpressurehq

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.donovanwilder.android.bloodpressurehq.tools.CsvTools
import com.donovanwilder.android.bloodpressurehq.ui.BpRecordsViewModel
import com.donovanwilder.android.bloodpressurehq.ui.MainScreen
import com.donovanwilder.android.bloodpressurehq.ui.SettingsScreen
import com.donovanwilder.android.bloodpressurehq.ui.theme.BloodPressureHQTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BloodPressureHQTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: BpRecordsViewModel = viewModel()

                    val importCsvLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                            if(result.data == null){
                                return@rememberLauncherForActivityResult
                                if(result.data!!.data == null){
                                    return@rememberLauncherForActivityResult
                                }
                            }
                            val uri = result.data!!.data!!
                            val stringBuilder = StringBuilder()
                            try {
                                val fis = contentResolver.openInputStream(uri)
                                val bufferedReader = BufferedReader(InputStreamReader(fis))
                                var line: String? = bufferedReader.readLine()
                                while (line != null) {
                                    stringBuilder.append(line)
                                    stringBuilder.append('\n')
                                    line = bufferedReader.readLine()
                                }
                                fis!!.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                            val newRecords =
                                CsvTools.createBpRecordList(stringBuilder.toString())
                            newRecords.forEach { record ->
                                viewModel.addRecords(record)
                            }
                        }
                    val toast =
                        Toast.makeText(LocalContext.current, "Records Added", Toast.LENGTH_SHORT)
                    val exportCsvLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                            if(result.data == null){
                                return@rememberLauncherForActivityResult
                                if(result.data!!.data == null){
                                    return@rememberLauncherForActivityResult
                                }
                            }
                            val uri = result.data!!.data!!
                            lifecycleScope.launch {
                                try {
                                    val parcelFileDescriptor =
                                        contentResolver.openFileDescriptor(uri, "w")
                                    val fos =
                                        FileOutputStream(parcelFileDescriptor!!.fileDescriptor)
                                    val bpRecordCsvString =
                                        CsvTools.createCsvString(viewModel.getAllRecords().first())
                                    val writer = OutputStreamWriter(fos)
                                    writer.write(bpRecordCsvString)
                                    writer.flush()
                                    writer.close()
                                    parcelFileDescriptor.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                            toast.show()
                        }
                    val emailReportLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){}
                    NavHost(navController = navController, startDestination = "home_screen") {
                        composable("home_screen") {
                            MainScreen(changeToSettings = {
                                navController.navigate(
                                    "settings_screen"
                                )
                            })
                        }
                        composable("settings_screen") {
                            SettingsScreen(
                                onImportCsv = {
                                    importCsvLauncher.launch(it)
                                },
                                onExportCsv = {
                                    exportCsvLauncher.launch(it)
                                },
                                onReportIssue = {
                                   emailReportLauncher.launch(it)
                                },
                                onBackPressed = {
                                   navController.popBackStack()
                                })
                        }
                    }
                }
            }
        }
    }
}


