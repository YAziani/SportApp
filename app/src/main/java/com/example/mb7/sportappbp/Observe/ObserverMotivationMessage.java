package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMessage;
import com.example.mb7.sportappbp.R;

/**
 * Created by Intirion on 25.03.2017.
 */

public class ObserverMotivationMessage extends Observer {

    short timeOutCounter = 0;

    @Override
    public void update(Context context) {

        // check if method allocated
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!preferences.getString("allocatedMethods","").contains("motivationimages")) {
            return;
        }

        if(timeOutCounter > 0) {
            timeOutCounter--;
        }else {
            if(!getNextTrainingTimeString(context).equals("")
                    && MotivationMessage.timeTillTraining(getNextTrainingTimeString(context)) == 5) {
                sendNotification(
                        context,
                        "Motivationsbilder",
                        ActivityMotivationMessage.class,
                        context.getString(R.string.mmNotiTitle),
                        context.getString(R.string.mmNotiSmallTitle),
                        R.mipmap.ic_trainingseinheit);
                timeOutCounter = 10;
            }
        }
    }



}
