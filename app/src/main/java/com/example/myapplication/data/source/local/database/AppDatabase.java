package com.example.myapplication.data.source.local.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.data.entities.AlertCategori;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.data.entities.ItemEnty;
import com.example.myapplication.data.source.local.database.dao.AlertDao;
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
        ItemEnty.class,
        AlertCategori.class
}, version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "habit_db";

    public static final int DATABASE_VERSION = 6;

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                     AppDatabase.class, DATABASE_NAME)
                    .addMigrations(MIGRATION_5_6)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }


    private static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE \"ALERT_CATEGORI\" (" +
                            "\"id\" INTEGER NOT NULL,\"day_of_week\" TEXT, \"categori_h\"INTEGER NOT NULL,\"isAtive\"INTEGER NOT NULL,\"time\"TEXT," +
                            "FOREIGN KEY(\"categori_h\") REFERENCES \"CATEGORIA_H\"(\"id\") ON UPDATE NO ACTION ON DELETE CASCADE," +
                            "PRIMARY KEY(\"id\" AUTOINCREMENT)" +
                            ");"
            );

            database.execSQL("INSERT INTO ALERT_CATEGORI (categori_h,day_of_week,isAtive) SELECT id, day_of_week, false FROM CATEGORIA_H\n");

            database.execSQL("CREATE INDEX \"index_ALERT_CATEGORI_time\" ON \"ALERT_CATEGORI\" (\n" +
                    "\t\"time\"\n" +
                    ");");
        }
    };
    public void clearInstance() {
        INSTANCE = null;
    }

    public abstract HabitCategoriDao habitCategoriDao();

    public abstract VariavelCategoriDao variavelCategoriDao();

    public abstract HabitEntyDao habitEntyDao();

    public abstract VariavelEntyDao variavelEntyDao();

    public abstract AlertDao AlertDao();

}
