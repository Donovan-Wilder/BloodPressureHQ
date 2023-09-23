package com.donovanwilder.android.bloodpressurehq

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.donovanwilder.android.bloodpressurehq.tools.CsvTools
import com.donovanwilder.android.bloodpressurehq.ui.BloodPressureHqApp
import com.donovanwilder.android.bloodpressurehq.ui.BpRecordsViewModel
import com.donovanwilder.android.bloodpressurehq.ui.MainScreen
import com.donovanwilder.android.bloodpressurehq.ui.SettingsScreen
import com.donovanwilder.android.bloodpressurehq.ui.theme.BloodPressureHQTheme
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BloodPressureHQTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: BpRecordsViewModel = viewModel()

                    val activityResultsLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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


                    NavHost(navController = navController, startDestination = "home_screen") {
                        composable("home_screen") {
                            MainScreen(changeToSettings = {
                                navController.navigate(
                                    "settings_screen"
                                )
                            })
                        }
                        composable("settings_screen") {
                            SettingsScreen(onImportCsv = {
                                activityResultsLauncher.launch(it)
                            })
                        }
                    }
                }
            }
        }
    }
}


