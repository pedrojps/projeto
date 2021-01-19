package com.example.myapplication.data.source.local.database.typeConverter;

import androidx.room.TypeConverter;

public class BooleanTypeConverter {

    @TypeConverter
    public static int toInteger(boolean bool){
        return bool ? 1 : 0;
    }

    @TypeConverter
    public static boolean fromInteger(int value){
        return value > 0;
    }
}
