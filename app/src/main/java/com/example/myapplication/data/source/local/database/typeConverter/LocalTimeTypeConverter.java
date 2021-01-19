package com.example.myapplication.data.source.local.database.typeConverter;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.myapplication.common.time.LocalTime;
import com.example.myapplication.common.time.LocalTimeFormat;

import java.text.ParseException;


public class LocalTimeTypeConverter {

    @TypeConverter
    public static LocalTime fromTimeString(String time) {
        try {
            return TextUtils.isEmpty(time) ? null : LocalTime.parse(time, LocalTimeFormat.FULL_TIME);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toTimeString(LocalTime time) {
        return time == null ? null : time.toString(LocalTimeFormat.FULL_TIME);
    }

}
