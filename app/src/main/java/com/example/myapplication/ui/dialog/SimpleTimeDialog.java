package com.example.myapplication.ui.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class SimpleTimeDialog extends TimePickerDialog {

    public SimpleTimeDialog(@NonNull Context context, @NonNull Calendar calendar, OnTimeSelectListener listener){
        super(context, createDateSetListener(listener), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    public SimpleTimeDialog(Context context, int hourOfDay, int minute, OnTimeSelectListener listener) {
        super(context, createDateSetListener(listener), hourOfDay, minute, true);
    }

    public SimpleTimeDialog(@NonNull Context context, Date initialHour, OnTimeSelectListener listener){
        this(context, initCalendar(initialHour), listener);
    }

    private static Calendar initCalendar(Date time){
        Calendar calendar = Calendar.getInstance();
        if(time != null){
            calendar.setTime(time);
        }
        return calendar;
    }

    private static OnTimeSetListener createDateSetListener(@NonNull final OnTimeSelectListener listener){
        return new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                listener.onTimeChanged(calendar.getTime());
            }
        };
    }

    public interface OnTimeSelectListener{
        void onTimeChanged(Date time);
    }
}
