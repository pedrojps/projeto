package com.example.myapplication.ui.factory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

public class DialogFactory {

    public static AlertDialog.Builder createDialog(@NonNull Context context, int title, String message){
        return createDialog(context, context.getResources().getString(title), message);
    }

    public static AlertDialog.Builder createDialog(@NonNull Context context, String message){
        return createDialog(context, null, message);
    }

    public static AlertDialog.Builder createDialog(@NonNull Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!TextUtils.isEmpty(title)) builder.setTitle(title);
        if(!TextUtils.isEmpty(message)) builder.setMessage(message);
        return builder;
    }

    public static ProgressDialog createInfinityProgressDialog(@NonNull Context context, int message){
        return createInfinityProgressDialog(context, context.getString(message));
    }

    public static ProgressDialog createInfinityProgressDialog(@NonNull Context context, String message){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setMessage(message);
        return dialog;
    }
}
