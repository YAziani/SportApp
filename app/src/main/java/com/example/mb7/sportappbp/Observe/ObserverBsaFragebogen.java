package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Felix on 25.03.2017.
 */

public class ObserverBsaFragebogen extends Observer {
    /**
     * then main update method that is called from the Observable
     * Here you check your condition and do call then notify process
     * @param context
     */
    @Override
    public void update(Context context) {
        this.context=context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // check if method allocated
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!preferences.getString("allocatedMethods","").contains("bsaQuestionary")) {
            return;
        }

        shouldNotify();
    }


      /**
     * show Notification 
     * if it is show notification and save that you have showed it in the preferences to not repeat it for the same event
     * @return
     */
    private  void shouldNotify(){

             // we are in the interval where we should raise a notification
            // just check if the user hasn't got a notification before
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String date =  android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
            Boolean sendNotif = preferences.getBoolean(date + " " ,false   );
            if (!sendNotif)
            {
                // insert in the preferences that notification has been sent
                preferences.edit().putBoolean(date + " ",true    ).commit();
                // send notification
                sendNotification(
                        context,
                        context.getString( R.string.aktivitaetsfragebogen),
                        ActivityFitnessFragebogen.class,context.getString(R.string.aktivitaetsfragebogen),
                        context.getString(R.string.ntf_aktivitaetsfragebogen),
                        R.mipmap.ic_aktivitaets_fragebogen);
            }


    }

}