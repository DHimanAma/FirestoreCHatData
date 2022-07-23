package com.example.firestorechatjava.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    public static Context context =null;
    public static String token="";
    public PreferenceManager(Context context) {

        if (context != null) {
            sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
            PreferenceManager.context = context;
        }
    }
    public void putBooleaN(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String Key1, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key1, value);
        editor.apply();
    }

    public String getString(String Key1) {
        return sharedPreferences.getString(Key1, null);
    }

    public void clear(){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public static void setLoginCredentials(String Accountno,Context
            context)
    {

      SharedPreferences  sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("amandhiman", Accountno);
        editor.commit();

    }
    public static String getLoginCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("amandhiman", "null");


        if (username.equalsIgnoreCase("null")


        )
            return "";
        else {
            Constants.INSTANCE_NAMEPHONEToKEN = username;

            return Constants.INSTANCE_NAMEPHONEToKEN;
        }
    }}