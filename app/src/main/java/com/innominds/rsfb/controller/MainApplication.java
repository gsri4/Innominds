package com.innominds.rsfb.controller;


import com.activeandroid.ActiveAndroid;


public class MainApplication extends com.activeandroid.app.Application {


    //Initializing active android
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
