package com.example.myapplication.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {

    public static void showSnackbarShort(@NonNull View view, @StringRes int text){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbarShort(@NonNull View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbarLong(@NonNull View view, @StringRes int text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackbarLong(@NonNull View view, String text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
