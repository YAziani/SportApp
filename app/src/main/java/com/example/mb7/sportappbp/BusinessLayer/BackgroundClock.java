package com.example.mb7.sportappbp.BusinessLayer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;


/**
 * class implements a regular check for time, to determine a fitting moment to start the motivation methods
 * Created by Aziani on 19.01.2017.
 */

public class BackgroundClock{

    String trainingStartTime;
    SharedPreferences preferences;
    Handler handler;
    Date date;
    Calendar calendar;
    Random random;

    /**
     * start the clock to perform regular checks to determine the moment of motivation
     * @param activity the activity calling the method, needed to access shared preferences
     * @param fixMotivationMethods list of the motivation methods that will always be called
     * @param variableMotivationMethods list of the motivation methods from which only one per day will be called
     */
    public void startClock(final Activity activity,
                           final LinkedList<MotivationMethod> fixMotivationMethods,
                           final LinkedList<MotivationMethod> variableMotivationMethods) {
        random = new Random();
        calendar = Calendar.getInstance();
        // get the shared preferences to determine the training time the user handed to the app
        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        // setup handler to schedule regular actions
        handler = new Handler();
        final int delay = 60000;
        handler.postDelayed(new Runnable() {
            public void run() {

                // get current time
                date = new Date();
                calendar.setTime(date);
                int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                // get training times
                String preferenceString = preferences.getString(getCurrentWeekday(), "");
                trainingStartTime = "";
                // find next training start time
                if(preferenceString != "") {
                    for(String s : preferenceString.split(";")) {

                        int trainingMinuteOfDay = Integer.valueOf(s.split(":")[0]) * 60 + Integer.valueOf(s.split(":")[1]);
                        int currentMinuteOfDay = currentHourOfDay * 60 + currentMinute;
                        if(trainingMinuteOfDay >= currentMinuteOfDay + 5) {
                            trainingStartTime = s;
                            break;
                        }
                    }
                }

                // notify every motivation method about the current time
                if(!trainingStartTime.equals("")) {
                    for (MotivationMethod m : fixMotivationMethods) {
                        m.run(trainingStartTime);
                    }

                    // get time until training begins
                    int trainingHourOfDay = Integer.valueOf(trainingStartTime.split(":")[0]);
                    int trainingMinute = Integer.valueOf(trainingStartTime.split(":")[1]);
                    int timeTillTraining = (trainingHourOfDay * 60 + trainingMinute)
                            - (currentHourOfDay * 60 + currentMinute);

                    if (timeTillTraining >= 5 && timeTillTraining < 5 + delay/60000) {
                       variableMotivationMethods.get(random.nextInt(variableMotivationMethods.size())).run(trainingStartTime);
                    }

                }
                // schedule the next iteration
                handler.postDelayed(this,delay);
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