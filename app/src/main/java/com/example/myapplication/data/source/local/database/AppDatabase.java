package com.example.myapplication.data.source.local.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;
import com.example.myapplication.data.source.local.database.dao.HabitCategoriDao;
import com.example.myapplication.data.source.local.database.dao.HabitEntyDao;
import com.example.myapplication.data.source.local.database.dao.VariavelCategoriDao;
import com.example.myapplication.data.source.local.database.dao.VariavelEntyDao;
import com.example.myapplication.data.source.local.database.typeConverter.BitmapTypeConverter;
import com.example.myapplication.data.source.local.database.typeConverter.DatetimeTypeConverter;
import com.example.myapplication.data.source.local.database.typeConverter.LocalDateTypeConverter;
import com.example.myapplication.data.source.local.database.typeConverter.LocalTimeTypeConverter;

@TypeConverters({
        LocalDateTypeConverter.class,
        LocalTimeTypeConverter.class,
        DatetimeTypeConverter.class,
        BitmapTypeConverter.class
})
@Database(entities = {
        HabitCategoria.class,
        HabitEnty.class,
        ItemCategoria.class,
        ItemEnty.class
}, version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "habit_db";

    public static final int DATABASE_VERSION = 5;

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                     AppDatabase.class, DATABASE_NAME)
                    //.addMigrations( MIGRATION_1_2)
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public void clearInstance() {
        INSTANCE = null;
    }

    public abstract HabitCategoriDao habitCategoriDao();

    public abstract VariavelCategoriDao variavelCategoriDao();

    public abstract HabitEntyDao habitEntyDao();

    public abstract VariavelEntyDao variavelEntyDao();


}
