package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by M.Braei on 24.03.2017.
 */

public class ObserverStimmungAngabe extends Observer{

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
            return (Integer.parseInt(abstand)) *10;
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
                        R.mipmap.ic_stimmungs_abgabe );
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

        Integer lastTrDateInt = getLastTrainingTime(context);
        int abstand = getAbstand();
        if (getMinutesofDate(new Date()) >=lastTrDateInt + abstand &&  lastTrDateInt !=  0)
        {
            // we are in the interval where we should raise a notification
            // just check if the user hasn't got a notification before
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String date =  android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
            Boolean sendNotif = preferences.getBoolean("N:" +  date + " " + lastTrDateInt.toString(), false   );
            if (!sendNotif)
            {
                // insert in the preferences that notification has been sent
                preferences.edit().putBoolean("N:" + date + " " + lastTrDateInt.toString(),true).commit(); ;
                // send notification
                sendNotification(
                        context,
                        context.getString( R.string.stimmungsabgabe),
                        ActivityStimmungsAbgabe.class,context.getString(R.string.stimmungsabgabe),
                        context.getString(R.string.ntf_stimmungsabgabe_nach),
                        R.mipmap.ic_stimmungs_abgabe );
            }

        }
        return false;
    }



}
