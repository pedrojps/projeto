package com.example.myapplication.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.myapplication.R;
import com.example.myapplication.generated.callback.OnClickListener;

public class DialogUtils {

    public static AlertDialog showDialog(@NonNull Context context, @StringRes int messageResId){
        return new AlertDialog.Builder(context)
                .setMessage(messageResId)
                .setPositiveButton(R.string.dialog_button_ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public static AlertDialog showDialogCallback(@NonNull Context context, @StringRes int messageResId, @StringRes int titleResId, Listener positiveCall){
        return new AlertDialog.Builder(context)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(R.string.dialog_button_yes, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    positiveCall.callBack();
                })
                .setNegativeButton(R.string.dialog_button_no, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }
    public static void showDialog(@NonNull Context context, String message){
        make(context, null, message).show();
    }

    public static void showDialog(@NonNull Context context, @StringRes int headerResId, @StringRes int messageResId){
        make(context, headerResId, messageResId).show();
    }

    public static void showDialog(@NonNull Context context, String header, String message){
        make(context, header, message).show();
    }

    private static AlertDialog make(@NonNull Context context, @StringRes int headerResId, @StringRes int messageResId){
        return new AlertDialog.Builder(context)
                .setTitle(headerResId)
                .setMessage(messageResId)
                .setPositiveButton(R.string.dialog_button_ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }

    private static AlertDialog make(@NonNull Context context, String header, String message){
        return new AlertDialog.Builder(context)
                .setTitle(header)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_button_ok, (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
    }
    public interface Listener {
        void callBack();
    }
}
