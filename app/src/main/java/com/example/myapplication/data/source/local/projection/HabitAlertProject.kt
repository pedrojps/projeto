package com.example.myapplication.data.source.local.projection

import androidx.room.ColumnInfo
import com.example.myapplication.common.time.DateTime
import com.example.myapplication.common.time.LocalDate
import com.example.myapplication.common.time.LocalTime
import com.example.myapplication.data.entities.HabitCategoria

class HabitAlertProject: HabitCategoria(){

    @ColumnInfo(name = "alert_id")
    var alertId: Long = 0

    @ColumnInfo(name = "week")
    var week: String? = null

    var isAtive:Boolean = false

    @ColumnInfo(index = true)
    var time: LocalTime? = null

}