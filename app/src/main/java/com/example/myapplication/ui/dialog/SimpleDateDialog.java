package com.example.myapplication.ui.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class SimpleDateDialog
        extends DatePickerDialog {

    public interface OnDateSelectListener{
        void onDateChanged(Date date);
    }

    private OnDateSelectListener mListener;

    public SimpleDateDialog(@NonNull Context context, @NonNull Calendar calendar, @NonNull OnDateSelectListener listener){
        super(context, createDateSetListener(listener), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public SimpleDateDialog(@NonNull Context context, @NonNull Calendar calendar){
        super(context, null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public SimpleDateDialog(@NonNull Context context, Date initialDate, @NonNull OnDateSelectListener listener){
        this(context, initCalendar(initialDate), listener);
    }

    public SimpleDateDialog(@NonNull Context context, Date initialDate){
        this(context, initCalendar(initialDate));

    }

    private static Calendar initCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        if(date != null){
            calendar.setTime(date);
        }
        return calendar;
    }

    private static OnDateSetListener createDateSetListener(@NonNull final OnDateSelectListener listener){
        return new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                listener.onDateChanged(calendar.getTime());
            }
        };
    }

    @Override
    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
        super.onDateChanged(view, year, month, dayOfMonth);
        if(mListener != null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            mListener.onDateChanged(calendar.getTime());
        }
    }

    public void setDateSelectListener(@NonNull OnDateSelectListener listener){
        mListener = listener;
    }

}
