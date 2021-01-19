package com.example.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public class ToastUtils {

    public static void showToastShort(@NonNull Context context, @StringRes int text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(@NonNull Context context, CharSequence text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(@NonNull Context context, @StringRes int text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showToastLong(@NonNull Context context, CharSequence text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
