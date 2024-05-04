package com.example.myapplication.utils

import com.example.myapplication.common.time.LocalDate
import java.util.*

enum class DayOfWeek(val id:Int) {
    NONE(0),
    SUNDAY (Calendar.SUNDAY),
    MONDAY (Calendar.MONDAY),
    TUESDAY (Calendar.TUESDAY),
    WEDNESDAY (Calendar.WEDNESDAY),
    THURSDAY (Calendar.THURSDAY),
    FRIDAY (Calendar.FRIDAY),
    SATURDAY (Calendar.SATURDAY);
    companion object {
        fun getDayWeekByDate(date: LocalDate): DayOfWeek{
            val gc = GregorianCalendar()
            gc.time = date

            return getById(gc[Calendar.DAY_OF_WEEK])
        }

         fun getById(id: Int): DayOfWeek{
            return when (id) {
                SUNDAY.id -> SUNDAY
                MONDAY.id -> MONDAY
                TUESDAY.id -> TUESDAY
                WEDNESDAY.id -> WEDNESDAY
                THURSDAY.id -> THURSDAY
                FRIDAY.id -> FRIDAY
                SATURDAY.id -> SATURDAY
                else -> NONE
            }
        }
    }
}