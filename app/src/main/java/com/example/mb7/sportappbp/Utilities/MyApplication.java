package com.example.mb7.sportappbp.Utilities;

import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.firebase.client.Firebase;

/**
 * Created by Aziani on 24.03.2017.
 */
public class MyApplication extends android.app.Application {

    public boolean isInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this); //initializeFireBase(context);
        System.out.println("init");
        isInitialized = true;
    }
}
