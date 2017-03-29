package com.example.mb7.sportappbp.Observe;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.R;

/**
 * Created by Felix on 25.03.2017.
 */
public class ObserverFitnessFragebogen extends Observer {
    /**
     * then main update method that is called from the Observable
     * Here you check your condition and do call then notify process
     *
     * @param context
     */
    @Override
    public void update(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.getString("allocatedMethods", "").contains("fitnessQuestionary")) {
            return;
        }
        shouldNotify();
    }

    /**
     * show Notification
     * if it is show notification and save that you have showed it in the preferences to not repeat it for the same
     * event
     */
    private void shouldNotify() {

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String date = android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
        Boolean sendNotif = preferences.getBoolean(date + " ", false);
        if (!sendNotif) {
            preferences.edit().putBoolean(date + " ", true).commit();
            sendNotification(context, context.getString(R.string.fitnessfragebogen), ActivityFitnessFragebogen.class,
                    context.getString(R.string.fitnessfragebogen), context.getString(R.string.ntf_fitnessfragebogen),
                    R.mipmap.ic_fittnessfragebogen);
        }
    }
}
