package com.donovanwilder.android.bloodpressurehq.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.util.Date


@Entity(tableName = "bp_record")
data class BpRecord(
    @PrimaryKey(true) @ColumnInfo(name = "_id") val id: Int = 0,
    @ColumnInfo(name = "date_added") val dateAdded: Date,
    @ColumnInfo(name = "sys") val sys: Int,
    @ColumnInfo(name = "dia") val dia: Int,
    @ColumnInfo(name = "pulse") val pulse: Int
)
