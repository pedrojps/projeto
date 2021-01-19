package com.example.myapplication.utils;

import android.os.Build;

public class AndroidUtils {

    public static boolean hasLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasKitkat(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
