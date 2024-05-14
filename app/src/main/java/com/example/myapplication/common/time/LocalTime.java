package com.example.myapplication.common.time;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalTime extends Date {

    @NonNull
    private String pattern;

    public LocalTime() {
        super();
        this.pattern = LocalTimeFormat.FULL_TIME;
    }

    public LocalTime(long time) {
        super(time);
        this.pattern = LocalTimeFormat.FULL_TIME;
    }

    public LocalTime(@NonNull Date time) {
        this(time.getTime());
        this.pattern = LocalTimeFormat.FULL_TIME;
    }

    public LocalTime(@NonNull Date time, @NonNull @LocalTimeFormat String pattern) {
        this(time.getTime());
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public String toString(@NonNull @LocalTimeFormat String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public static LocalTime parse(
            @NonNull String dateString, @NonNull @LocalTimeFormat String pattern
    ) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return new LocalTime(formatter.parse(dateString), pattern);
    }

    public static LocalTime now(){
        LocalTime lt = new LocalTime();
        try {
            return LocalTime.parse(lt.toString(LocalTimeFormat.FULL_TIME),LocalTimeFormat.FULL_TIME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lt;
    }

}
