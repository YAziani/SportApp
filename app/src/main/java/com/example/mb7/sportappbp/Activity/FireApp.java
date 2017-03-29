package com.example.mb7.sportappbp.Activity;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by M.Braei on 31.01.2017.
 */

public class FireApp extends Application {
    public boolean isInitialized;

    @Override
    public void onCreate() {

        super.onCreate();

        Firebase.setAndroidContext(this);
        isInitialized = true;

    }
}
