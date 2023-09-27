package com.donovanwilder.android.bloodpressurehq.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.util.Date


@Entity(tableName = BpRecord.TABLE_NAME)
data class BpRecord(
    @PrimaryKey(true) @ColumnInfo(name = "_id") val id: Int = 0,
    @ColumnInfo(name = "date_added") val dateAdded: Date,
    @ColumnInfo(name = "sys") val sys: Int,
    @ColumnInfo(name = "dia") val dia: Int,
    @ColumnInfo(name = "pulse") val pulse: Int
) {
    override fun equals(other: Any?): Boolean {
        val otherRecord = other as BpRecord
        return (otherRecord.id == this.id) &&
                (otherRecord.dateAdded == this.dateAdded) &&
                (otherRecord.sys == this.sys) &&
                (otherRecord.dia == this.dia) &&
                (otherRecord.pulse == this.pulse)
    }

    companion object {
        const val TABLE_NAME = "bp_record"
        const val sysMaxValue = 400
        const val sysMinValue = 50
        const val diaMaxValue = 400
        const val diaMinValue = 20
        const val pulseMaxValue = 500
    }
}
