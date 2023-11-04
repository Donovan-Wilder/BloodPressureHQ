package com.donovanwilder.android.bloodpressurehq.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.donovanwilder.android.bloodpressurehq.model.BpRecord

class RecordChangeViewModel : ViewModel() {
    var sys by mutableStateOf("120")
    var dia by mutableStateOf("70")
    var pulse by mutableStateOf("60")

    fun updateValues(bpRecord: BpRecord){
        sys = bpRecord.sys.toString()
        dia = bpRecord.dia.toString()
        pulse = bpRecord.pulse.toString()
    }
    fun validateInput(value: String):Boolean {
        for(character in value){
            if(!character.isDigit()){
                return false
            }
        }
        if(value == ""){
            return true
        }
        val valueInt = value.toInt()
        if(valueInt>500||valueInt<0){
            return false
        }
        return true
    }

}