package com.example.myapplication.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

public class TelephoneUtils {

    public static String getTelephoneIMEI(@NonNull final Context context) throws PermissionDeniedException{
        String IMEI;

        try{
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

//            String IMEI = telephonyManager.getDeviceId();
            if(Build.VERSION.SDK_INT < 26){
                IMEI = telephonyManager.getDeviceId();
            }else{
                IMEI = telephonyManager.getImei();
            }

            if(TextUtils.isEmpty(IMEI)){
                throw new PermissionDeniedException("Permissão para acessar o seguinte recurso negada: " + Context.TELEPHONY_SERVICE);
            }
            return IMEI;
        }catch (NullPointerException e){
            throw new PermissionDeniedException("Não foi possível verificar as permissões do dispositivo.");
        }
    }
}
