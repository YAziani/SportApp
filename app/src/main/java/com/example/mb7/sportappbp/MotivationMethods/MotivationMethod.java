package com.example.mb7.sportappbp.MotivationMethods;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aziani on 23.12.2016.
 *
 * abstract class for methods with which the user gets motivational help
 */

public abstract class MotivationMethod {
    Activity activity;

    public MotivationMethod(Activity activity){
        this.activity = activity;
    }

    /**
     * runs the motivation method
     * has to be individually implemented by each concrete motivation method
     * @param trainingStartTime string representing the next training time
     * @return true if method will fire, false else
     */
    public abstract boolean run(String trainingStartTime);

    /**
     * initiates the rating of a motivation method
     * collects data about the efficiency of the used method
     * @param didTrain boolean representing if user trained or not
     * @return string representing the amount of successful motivations and total motivations
     */
    public String rate(boolean didTrain) {
        // get rating from preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String rating = preferences.getString(this.getClass().getSimpleName() + "Rating", "0:0");
        int successCount = Integer.valueOf(rating.split(":")[0]);
        int totalCount = Integer.valueOf(rating.split(":")[1]);

        // accumulate count
        if(didTrain) {
            successCount++;
        }
        totalCount++;

        // save rating into preferences
        rating = String.valueOf(successCount) + ":" + String.valueOf(totalCount);
        preferences.edit().putString(this.getClass().getSimpleName() + "Rating", rating).apply();

        return rating;
    }

    /**
     * determine the time in minutes needed until the training begins
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

    /**
     * evaluate the results of possible permissionRequests, leave empty if motivation method doesn't request permissions
     * @param requestCode code of the requested permission
     * @param permissions array of all requested permissions
     * @param grantResults permissions which have been granted
     */
    public void evaluatePermissionResults(int requestCode, String permissions[], int[] grantResults) {
    }
}
