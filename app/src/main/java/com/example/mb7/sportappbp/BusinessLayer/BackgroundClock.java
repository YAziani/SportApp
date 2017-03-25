package com.example.mb7.sportappbp.BusinessLayer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;


/**
 * class implements a regular check for time, to determine a fitting moment to start the motivation methods
 * Created by Aziani on 19.01.2017.
 */

public class BackgroundClock{

    private String trainingStartTime = "";
    private SharedPreferences preferences;
    private Handler handler;
    private Date date;
    private Calendar calendar;
    private Random random;
    private MotivationMethod nextRandomMethod = null;
    private static LinkedList<MotivationMethod> runningMethods;

    /**
     * start the clock to perform regular checks to determine the moment of motivation
     * @param activity the activity calling the method, needed to access shared preferences
     * @param fixMotivationMethods list of the motivation methods that will always be called
     * @param variableMotivationMethods list of the motivation methods from which only one per day will be called
     */
    public void startClock(final Activity activity,
                           final LinkedList<MotivationMethod> fixMotivationMethods,
                           final LinkedList<MotivationMethod> variableMotivationMethods) {
        runningMethods = new LinkedList<>();
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
                //TODO
                //System.out.println("CLOCK TICK " + currentHourOfDay +":"+ currentMinute);
                // get training times
                String preferenceString = preferences.getString(getCurrentWeekday(), "");
                preferences.edit().remove("nextTrainingTime").commit();
                // find next training start time
                if(!preferenceString.equals("")) {
                    for(final String s : preferenceString.split(";")) {
                        int trainingMinuteOfDay = Integer.valueOf(s.split(":")[0]) * 60
                                + Integer.valueOf(s.split(":")[1]);
                        int currentMinuteOfDay = currentHourOfDay * 60 + currentMinute;

                        // check if training is still noteworthy
                        if(currentMinuteOfDay <= trainingMinuteOfDay) {
                            preferences.edit().putString("nextTrainingTime", s).apply();
                            if(!trainingStartTime.equals(s)) {
                                // save next training start time
                                trainingStartTime = s;
                                preferences.edit().putString("nextTrainingTime", trainingStartTime).apply();
                                // save next random motivation method
                                if(variableMotivationMethods.size() > 0) {
                                    nextRandomMethod = variableMotivationMethods
                                            .get(random.nextInt(variableMotivationMethods.size()));
                                }
                            }
                            break;
                        }
                    }
                }

                // notify every motivation method about the current time and save it if it fires
                if(!trainingStartTime.equals("")) {
                    for (MotivationMethod m : fixMotivationMethods) {
                        if(m.run(trainingStartTime)){
                            runningMethods.add(m);
                        }
                    }
                    if(nextRandomMethod != null) {
                        if(nextRandomMethod.run(trainingStartTime)){
                            runningMethods.add(nextRandomMethod);
                        }
                    }
                }
                // schedule the next iteration
                handler.postDelayed(this,delay);
            }
        },delay);

    }

    /**
     * let methods rate themself and hand the rating over to be saved into the data base
     * @param didTrain boolean representing if user did train or not
     */
    public static void startRating(boolean didTrain) {

        LinkedList<String> listMethod = new LinkedList<>();
        LinkedList<String> listRating = new LinkedList<>();
        // let every running method rate themself
       if(runningMethods != null) {
           for(MotivationMethod m : runningMethods) {
               listMethod.add(m.getClass().getSimpleName());
               listRating.add(m.rate(didTrain));
           }
           runningMethods.clear();
       }

        // hand the ratings to the user object
        ActivityMain.mainUser.saveRating(listMethod,listRating);

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
        int currentWeekday = calendar.get(Calendar.DAY_OF_WEEK);
        if(currentWeekday == Calendar.MONDAY){
            dayOfWeek = "Montag";
        }
        else if(currentWeekday == Calendar.TUESDAY){
            dayOfWeek = "Dienstag";
        }
        else if(currentWeekday == Calendar.WEDNESDAY){
            dayOfWeek = "Mittwoch";
        }
        else if(currentWeekday == Calendar.THURSDAY){
            dayOfWeek = "Donnerstag";
        }
        else if(currentWeekday == Calendar.FRIDAY){
            dayOfWeek = "Freitag";
        }
        else if(currentWeekday == Calendar.SATURDAY){
            dayOfWeek = "Samstag";
        }
        else{
            dayOfWeek = "Sonntag";
        }
        return dayOfWeek;
    }

    public String getNextTrainingTime(Activity activity){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String preferenceString = preferences.getString(getCurrentWeekday(), "");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        // find next training start time
        if(!preferenceString.equals("")) {
            for(final String s : preferenceString.split(";")) {
                int trainingMinuteOfDay = Integer.valueOf(s.split(":")[0]) * 60
                        + Integer.valueOf(s.split(":")[1]);
                int currentMinuteOfDay = currentHourOfDay * 60 + currentMinute;
                // check if training is still noteworthy
                if(currentMinuteOfDay <= trainingMinuteOfDay) {
                    return s;
                }
            }
        }
        return "";
    }

    public String getLastTrainingTime(Activity activity){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String preferenceString = preferences.getString(getCurrentWeekday(), "");
        String lastTraining = "";
        Calendar calendar = Calendar.getInstance();
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        // find next training start time
        if(!preferenceString.equals("")) {
            for(final String s : preferenceString.split(";")) {
                int trainingMinuteOfDay = Integer.valueOf(s.split(":")[0]) * 60
                        + Integer.valueOf(s.split(":")[1]);
                int currentMinuteOfDay = currentHourOfDay * 60 + currentMinute;
                // check if training is still noteworthy
                if(currentMinuteOfDay <= trainingMinuteOfDay) {
                    if(!lastTraining.equals("")) {
                        return lastTraining;
                    }else {
                        break;
                    }
                }else {
                    lastTraining = s;
                }
            }
        }
        return lastTraining;
    }
}