package com.example.myapplication.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.myapplication.data.repository.HabitCategoriRepository;
import com.example.myapplication.data.source.local.database.AppDatabase;

public class Injection {

    public static AppDatabase provideDatabase(@NonNull Context context) {
        return AppDatabase.getInstance(context.getApplicationContext());
    }

    public static HabitCategoriRepository HabitCategoriRepository(@NonNull Context context) {
        AppDatabase appDatabase = provideDatabase(context);
        return HabitCategoriRepository.getInstance(appDatabase.habitCategoriDao());
    }



}
