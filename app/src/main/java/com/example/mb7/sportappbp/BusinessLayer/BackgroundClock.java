package com.example.mb7.sportappbp.BusinessLayer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;


/**
 * class implements a regular check for time, to determine a fitting moment to start the motivation methods
 * Created by Aziani on 19.01.2017.
 */

public class BackgroundClock{

    /**
     * start the clock to perform regular checks to determine the moment of motivation
     * @param activity the activity calling the method, needed to access shared preferences
     * @param motivationMethods list of the motivation methods the app uses
     */
    public void startClock(final Activity activity, final LinkedList<MotivationMethod> motivationMethods) {
        // get the shared preferences to determine the training time the user handed to the app
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

        // setup handler to schedule regular actions
        final Handler h = new Handler();
        final int delay = 5000;
        h.postDelayed(new Runnable() {
            public void run() {
                // notify every motivation method about the current time
                if(motivationMethods != null && !preferences.getString(getCurrentWeekday(), "").equals("")) {
                    for (MotivationMethod m : motivationMethods) {
                        m.run(preferences.getString(getCurrentWeekday(), ""));
                    }
                }
                // schedule the next iteration
                h.postDelayed(this,delay);
            }
        },delay);

    }

    /**
     * determines the current day of the week
     * @return string representing the day of the week
     */
    private String getCurrentWeekday() {
        String dayOfWeek;

        // setup calendar to get day of week
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // determine the day of the week
        switch(calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                dayOfWeek = "Montag";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Dienstag";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Mittwoch";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Donnerstag";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Freitag";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Samstag";
                break;
            case Calendar.SUNDAY:
                dayOfWeek = "Sonntag";
                break;
            default:
                dayOfWeek = "Montag";
        }
        return dayOfWeek;
    }
}