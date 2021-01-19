package com.example.myapplication.data.source.local.database.typeConverter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalDateFormat;

import java.text.ParseException;


public class LocalDateMonthYearTypeConverter {

    @TypeConverter
    public static LocalDate fromDateString(String date){
        try {
            return TextUtils.isEmpty(date) ? null : LocalDate.parse(date, LocalDateFormat.SQLITE_MONTH_YEAR);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toDateString(LocalDate date){
        return date == null ? null : date.toString(LocalDateFormat.SQLITE_MONTH_YEAR);
    }
}
