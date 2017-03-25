package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by M.Braei on 24.03.2017.
 */

public class ObserverStimmungAngabe extends Observer{
    private SharedPreferences preferences;
    private Context context;

    /**
     * then main update method that is called from the Observable
     * Here you check your condition and do call then notify process
     * @param context
     */
    @Override
    public void update(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // User has to fill Stimmungsabgabe if ->
        // show Notification if we are in the interval [trainingtime - abstand, trainingtime]
        shouldNotifyBefore();
        // show Notification if we are in the interval [trainingtime, trainingtime + abstand]
        shouldNotifyAfter();
    }

    // get the set Abstand in the Einstellungen
    private  int getAbstand()
    {
        try{
            String abstand = preferences.getString("lstStmabfrageAbstand","0");
            return Integer.parseInt(abstand) *10;
        }
        catch ( Exception ex)
        {
            Log.e("Error",ex.getMessage());
            return 0;
        }

    }


    // get the current time in minutes
    private int getMinutesofDate(Date date  )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentMinuteOfDay = currentHourOfDay * 60 + currentMinute;
        return currentMinuteOfDay;
    }

    /**
     * show Notification if we are in the interval [trainingtime - abstand, trainingtime]
     * if it is show notification and save that you have showed it in the preferences to not repeat it for the same event
     * @return
     */
    private  boolean shouldNotifyBefore(){

        Integer nextTrDateInt = getNextTrainingTimeInteger(context);
        int abstand = getAbstand();
        if (getMinutesofDate(new Date()) >=nextTrDateInt - abstand && getMinutesofDate(new Date()) < nextTrDateInt)
        {
            // we are in the interval where we should raise a notification
            // just check if the user hasn't got a notification before
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String date =  android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
            Boolean sendNotif = preferences.getBoolean("V:" + date + " " + nextTrDateInt.toString(), false   );
            if (!sendNotif)
            {
                // insert in the preferences that notification has been sent
                preferences.edit().putBoolean("V:" + date + " " + nextTrDateInt.toString(),true    ).commit(); ;
                // send notification
                sendNotification(
                        context,
                        context.getString( R.string.stimmungsabgabe),
                        ActivityStimmungsAbgabe.class,context.getString(R.string.stimmungsabgabe),
                        context.getString(R.string.ntf_stimmungsabgabe),
                        R.mipmap.ic_stimmungsabgabe );
            }

        }
        return false;
    }

    /**
     * show Notification if we are in the interval [trainingtime, trainingtime + abstand]
     * if it is show notification and save that you have showed it in the preferences to not repeat it for the same event
     * @return
     */
    private  boolean shouldNotifyAfter(){

        Integer nextTrDateInt = getLastTrainingTime(context);
        int abstand = getAbstand();
        if (getMinutesofDate(new Date()) >=nextTrDateInt + abstand &&  nextTrDateInt !=  0)
        {
            // we are in the interval where we should raise a notification
            // just check if the user hasn't got a notification before
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String date =  android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
            Boolean sendNotif = preferences.getBoolean("N:" +  date + " " + nextTrDateInt.toString(), false   );
            if (!sendNotif)
            {
                // insert in the preferences that notification has been sent
                preferences.edit().putBoolean("N:" + date + " " + nextTrDateInt.toString(),true    ).commit(); ;
                // send notification
                sendNotification(
                        context,
                        context.getString( R.string.stimmungsabgabe),
                        ActivityStimmungsAbgabe.class,context.getString(R.string.stimmungsabgabe),
                        context.getString(R.string.ntf_stimmungsabgabe),
                        R.mipmap.ic_stimmungsabgabe );
            }

        }
        return false;
    }

    /**
     * get the time of the next training time
     * @param context
     * @return
     */
    public Integer getNextTrainingTimeInteger(Context context){
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
                    return trainingMinuteOfDay;
                }
            }
        }
        return 0;
    }

    /**
     * get the time of the last training time
     * @param context
     * @return
     */
    public Integer getLastTrainingTime(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
                        return Integer.valueOf(lastTraining.split(":")[0]) * 60
                                + Integer.valueOf(lastTraining.split(":")[1]);
                    }else {
                        break;
                    }
                }else {
                    lastTraining = s;
                }
            }
        }
        if(lastTraining.equals("")) {
            return -1;
        }else {
            return Integer.valueOf(lastTraining.split(":")[0]) * 60 + Integer.valueOf(lastTraining.split(":")[1]);
        }
    }

}
