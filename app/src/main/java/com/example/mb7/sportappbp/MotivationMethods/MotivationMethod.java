package com.example.mb7.sportappbp.MotivationMethods;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aziani on 23.12.2016.
 *
 * abstract class for methods with which the user gets motivational help
 */

public abstract class MotivationMethod {

    /**
     * runs the motivation method
     * has to be individually implemented by each concrete motivation method
     */
    public abstract void run(String trainingStartTime);

    /**
     * initiates the rating of a motivation method
     * collects data about the efficiency of the used method
     */
    public abstract void rate();

    /**
     * determine the time in minutes needed until the training begins
     * @param trainingStartTime string, representing the training start time
     * @return time in minutes until training starts
     */
    public int timeTillTraining(String trainingStartTime) {
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
