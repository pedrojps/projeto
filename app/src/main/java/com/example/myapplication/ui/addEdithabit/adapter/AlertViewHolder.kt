package com.example.myapplication.ui.addEdithabit.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.common.time.LocalTime
import com.example.myapplication.data.entities.AlertCategori
import com.example.myapplication.databinding.ItemAlertBinding
import com.example.myapplication.ui.dialog.SimpleTimeDialog
import com.example.myapplication.utils.DayOfWeek
import com.example.myapplication.utils.Globals
import java.util.*

class AlertViewHolder(val binding: ItemAlertBinding): RecyclerView.ViewHolder(binding.root) {

    var mItem: AlertCategori? = null
    private var mDayOfWeek: ArrayList<Int?> = ArrayList()

    fun onBind(item: AlertCategori, listenerDeleter: View.OnClickListener) {
        mItem = item
        mDayOfWeek= ArrayList()
        val isNotShowAlert :Boolean? = Globals[Globals.c.SHOW_ALERT_OPISION]

        binding.apply {

            checkboxD.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.SUNDAY, check)
            }
            checkboxQ1.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.WEDNESDAY, check)
            }
            checkboxQ2.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.THURSDAY, check)
            }
            checkboxS.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.MONDAY, check)
            }
            checkboxS2.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.FRIDAY, check)
            }
            checkboxS3.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.SATURDAY, check)
            }
            checkboxT.setOnCheckedChangeListener { _, check ->
                checkAll()
                setDayOfWeek(DayOfWeek.TUESDAY, check)
            }
            allDay.setOnClickListener { _ ->
                val check: Boolean = allDay.isChecked
                checkboxD.isChecked = check
                checkboxQ1.isChecked = check
                checkboxQ2.isChecked = check
                checkboxS.isChecked = check
                checkboxS2.isChecked = check
                checkboxS3.isChecked = check
                checkboxT.isChecked = check
            }
            setCheck()
            val timeL = item.time ?: LocalTime()
            time.text = itemView.context.getString(R.string.format_time,  timeL)
            time.setOnClickListener {
                SimpleTimeDialog(itemView.context, item.time ?: LocalTime()) { newDate: Date ->
                    val localTime = LocalTime(newDate)
                    time.text = itemView.context.getString(R.string.format_time, localTime)
                    item.time = localTime
                }.show()
            }

            if (isNotShowAlert==true) alertaAtiveDay.isVisible = false
            alertaAtiveDay.isChecked = mItem?.isAtive?: false
            alertaAtiveDay.setOnClickListener {
                mItem?.isAtive = alertaAtiveDay.isChecked
            }
            deleteItem.setOnClickListener(listenerDeleter)

            mItem?.time = timeL
            mItem?.dayOfWeek = mDayOfWeek.toString()
            mItem?.isAtive = alertaAtiveDay.isChecked
        }

    }

    private fun checkAll() {
        binding.apply {
            allDay.isChecked = (
                    checkboxD.isChecked
                    && checkboxQ1.isChecked
                    && checkboxQ2.isChecked
                    && checkboxS.isChecked
                    && checkboxS2.isChecked
                    && checkboxS3.isChecked
                    && checkboxT.isChecked)
        }
    }

    private fun setCheck() {
        mDayOfWeek = mItem?.dayOfWeekList as ArrayList<Int?>

        binding.apply {
            checkboxD.isChecked = containsDayOfWeek(DayOfWeek.SUNDAY)
            checkboxQ1.isChecked = containsDayOfWeek(DayOfWeek.WEDNESDAY)
            checkboxQ2.isChecked = containsDayOfWeek(DayOfWeek.THURSDAY)
            checkboxS.isChecked = containsDayOfWeek(DayOfWeek.MONDAY)
            checkboxS2.isChecked = containsDayOfWeek(DayOfWeek.FRIDAY)
            checkboxS3.isChecked = containsDayOfWeek(DayOfWeek.SATURDAY)
            checkboxT.isChecked = containsDayOfWeek(DayOfWeek.TUESDAY)
        }
    }

    private fun containsDayOfWeek(day: DayOfWeek): Boolean {
        return mDayOfWeek.contains(day.id)
    }

    private fun setDayOfWeek(day: DayOfWeek, check: Boolean) {
        if (check){
            if (!mDayOfWeek.contains(day.id))
                mDayOfWeek.add(day.id)
        } else
            mDayOfWeek.removeIf{
                it == day.id
            }

        mItem?.dayOfWeek = mDayOfWeek.toString()
    }
}