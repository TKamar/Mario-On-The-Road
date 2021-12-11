package com.example.hw1;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {

    private final String SP_FILE = "SP_FILE";



    private static com.example.hw1.objects.MSP me;
    private SharedPreferences sharedPreferences;

    public static com.example.hw1.objects.MSP getMe() {
        return me;
    }

    private MSP(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static com.example.hw1.objects.MSP initHelper(Context context) {
        if (me == null) {
            me = new com.example.hw1.objects.MSP(context);
        }
        return me;
    }

    public void putDouble(String KEY, double defValue) {
        putString(KEY, String.valueOf(defValue));
    }

    public double getDouble(String KEY, double defValue) {
        return Double.parseDouble(getString(KEY, String.valueOf(defValue)));
    }

    public int getInt(String KEY, int defValue) {
        return sharedPreferences.getInt(KEY, defValue);
    }

    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }
}

