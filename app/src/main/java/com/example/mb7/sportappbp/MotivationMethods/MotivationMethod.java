package com.example.mb7.sportappbp.MotivationMethods;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aziani on 23.12.2016.
 * <p>
 * abstract class for methods with which the user gets motivational help
 */

public abstract class MotivationMethod {
    Activity activity;

    public MotivationMethod(Activity activity) {
        this.activity = activity;
    }

    /**
     * determine the time in minutes needed until the training begins
     *
     * @param trainingStartTime string, representing the training start time
     * @return time in minutes until training starts
     */
    public static int timeTillTraining(String trainingStartTime) {
        // start of the training
        int trainingHour = Integer.valueOf(trainingStartTime.split(":")[0]);
        int trainingMinute = Integer.valueOf(trainingStartTime.split(":")[1]);
        int trainingMinuteOfDay = trainingHour * 60 + trainingMinute;

        // current time
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentMinuteOfDay = currentHour * 60 + currentMinute;

        return trainingMinuteOfDay - currentMinuteOfDay;
    }
}
