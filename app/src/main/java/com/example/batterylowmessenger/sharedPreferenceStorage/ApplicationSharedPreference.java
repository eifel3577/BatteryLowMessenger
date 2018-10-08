package com.example.batterylowmessenger.sharedPreferenceStorage;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.preference.PreferenceManager;



public class ApplicationSharedPreference {

    private static final String SAVED_MESSAGE = "saved_message";
    private static final String SAVED_BATTERY_LEVEL= "saved_battery_level";
    private static final String SAVED_CHECKED_LIST= "saved_checked_list";

    public static String getStoredMessage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SAVED_MESSAGE,null);
    }


    public static void setStoredMessage(Context context, String message) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SAVED_MESSAGE,message)
                .apply();
    }

    public static String getStoredBatteryLevel(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SAVED_BATTERY_LEVEL,null);
    }


    public static void setStoredBatteryLevel(Context context, String batteryLevel) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SAVED_BATTERY_LEVEL,batteryLevel)
                .apply();
    }


}
