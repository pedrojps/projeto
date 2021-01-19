package com.example.myapplication.data.source.local.database.typeConverter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.myapplication.common.time.DateTime;
import com.example.myapplication.common.time.DateTimeFormat;

import java.text.ParseException;


public class DatetimeTypeConverter {

    @TypeConverter
    public static DateTime fromDatetimeString(String datetime) {
        try {
            return TextUtils.isEmpty(datetime) ? null : DateTime.parse(datetime, DateTimeFormat.SQLITE_DATETIME);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toDatetimeString(DateTime datetime) {
        return datetime == null ? null : datetime.toString(DateTimeFormat.SQLITE_DATETIME);
    }

}
