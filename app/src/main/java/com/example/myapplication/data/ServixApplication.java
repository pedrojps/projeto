package com.example.myapplication.data;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.example.myapplication.data.preferences.ServixAppPreferencesHelper;
import com.example.myapplication.data.source.local.database.AppDatabase;
import com.example.myapplication.utils.PermissionDeniedException;
import com.example.myapplication.utils.TelephoneUtils;

import java.util.UUID;


public class ServixApplication extends MultiDexApplication {

    private static AppDatabase mDatabase;

    private static String mAppId = null;


    private static ServixAppPreferencesHelper mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        plantTimber();
        mSettings = ServixAppPreferencesHelper.getInstance(getApplicationContext());
        findOrCreateAppInstanceId();
        mDatabase = AppDatabase.getInstance(this);
    }

    private void plantTimber() {
        
    }

    private void findOrCreateAppInstanceId() {
        mAppId = mSettings.getAppInstanceId();

        if (TextUtils.isEmpty(mAppId)) {
            mAppId = generateInstanceId();
            mSettings.setAppInstanceId(mAppId);
        }
    }

    private String generateInstanceId() {
        return UUID.randomUUID().toString();
    }


    private static boolean isAppIdEnabled(@NonNull Context context) {
        return ServixAppPreferencesHelper.getInstance(context)
                .isAppInstanceIdEnabled();
    }

    public synchronized static String appId(@NonNull Context context) throws PermissionDeniedException {
        if (isAppIdEnabled(context)) {
            if (TextUtils.isEmpty(mAppId)) {
                mAppId = ServixAppPreferencesHelper.getInstance(context)
                        .getAppInstanceId();
            }
            return mAppId;
        } else {
            return TelephoneUtils.getTelephoneIMEI(context);
        }
    }

}
