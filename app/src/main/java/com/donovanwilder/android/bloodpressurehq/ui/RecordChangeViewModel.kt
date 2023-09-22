package com.donovanwilder.android.bloodpressurehq.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
class RecordChangeViewModel : ViewModel() {
    var sys by mutableStateOf("120")
    var dia by mutableStateOf("70")
    var pulse by mutableStateOf("60")

}