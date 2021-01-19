package com.example.myapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class AppPreferencesHelper {

    public static final int EMPTY_NUMBER_ENTRY = -1;

    private final String SETTINGS_NAME;

    private final SharedPreferences mPreferences;

    public AppPreferencesHelper(Context context, String settingsName){
        SETTINGS_NAME = settingsName;
        mPreferences = context.getSharedPreferences(settingsName, 0);
    }

    public SharedPreferences getPreferences(){
        return mPreferences;
    }

    public String getSettingsName(){
        return SETTINGS_NAME;
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Retorna String vazia ("") se não houver valor.
     * @param key Chave de identificação do valor
     * @return Valor gravado ou ""
     */
    public String getString(String key){
        return mPreferences.getString(key, "");
    }

    public String getString(String key, String defaultValue){
        return mPreferences.getString(key, defaultValue);
    }

    public void putLong(String key, long value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Retorna -1 se não houver valor inserido.
     * @param key Chave de identificação do valor
     * @return Valor gravado na chave ou -1
     */
    public long getLong(String key){
        return mPreferences.getLong(key, EMPTY_NUMBER_ENTRY);
    }

    public void putInteger(String key, int value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Retorna -1 se não houver valor inserido.
     * @param key Chave de identificação do valor
     * @return Valor gravado na chave ou -1
     */
    public int getInteger(String key){
        return mPreferences.getInt(key, EMPTY_NUMBER_ENTRY);
    }

    public void putStringSet(String key, Set<String> set){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Retorna false se não houver valor inserido.
     * @param key Chave de identificação do valor.
     * @return Valor gravado na chave ou false se não houver valor.
     */
    public boolean getBoolean(String key){
        return mPreferences.getBoolean(key, false);
    }

    /**
     * Retorna um HashSet vazio se não houver valor inserido.
     * @param key Chave de identificação do valor
     * @return Valor gravado na chave ou HashSet vazio
     */
    public Set<String> getStringSet(String key){
        return mPreferences.getStringSet(key, new HashSet<>());
    }

    public void remove(String key){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

}
