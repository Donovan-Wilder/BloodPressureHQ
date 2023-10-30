package com.donovanwilder.android.bloodpressurehq

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.donovanwilder.android.bloodpressurehq.ui.SettingsScreen
import com.donovanwilder.android.bloodpressurehq.ui.theme.BloodPressureHQTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsComposeTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun Should_SelectOnExportCsv() {
        var result = -1
        composeTestRule.setContent {

            BloodPressureHQTheme {
                SettingsScreen(
                    onImportCsv = { result = 0 },
                    onExportCsv = { result = 1 },
                    onReportIssue = { result = 2 }) {
                }
            }

        }
        composeTestRule.onNodeWithText(text = "Export to File", ignoreCase = true).performClick()
        assertEquals(1, result)
    }

    @Test
    fun Should_SelectOnImportCsv() {
        var result = -1
        composeTestRule.setContent {

            BloodPressureHQTheme {
                SettingsScreen(
                    onImportCsv = { result = 0 },
                    onExportCsv = { result = 1 },
                    onReportIssue = { result = 2 }) {
                }
            }

        }
        composeTestRule.onNodeWithText(text = "import from file", ignoreCase = true).performClick()
        assertEquals(0, result)
    }

    @Test
    fun Should_SelectOnReportIssue() {
        var result = -1
        composeTestRule.setContent {

            BloodPressureHQTheme {
                SettingsScreen(
                    onImportCsv = { result = 0 },
                    onExportCsv = { result = 1 },
                    onReportIssue = { result = 2 }) {
                }
            }

        }
        composeTestRule.onNodeWithText(text = "report issue", ignoreCase = true).performClick()
        assertEquals(2, result)
    }


}