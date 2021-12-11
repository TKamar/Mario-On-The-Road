package com.example.hw1.Activities;

import android.app.Application;

import com.example.hw1.objects.MSP;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MSP.initHelper(this);
    }
}
