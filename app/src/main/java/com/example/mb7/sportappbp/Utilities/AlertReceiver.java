package com.example.mb7.sportappbp.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.Observe.Observer;
import com.example.mb7.sportappbp.Observe.ObserverStimmungAngabe;
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.util.Date;
import java.util.List;

/**
 * Created by M.Braei on 23.03.2017.
 */

public  class   AlertReceiver extends BroadcastReceiver {
    // Here delcare your observer
    ObserverStimmungAngabe observerStimmungAngabe = null;

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {
        android.os.Debug.waitForDebugger();

        // Initialize your observer and call its update method
        if (observerStimmungAngabe == null)
            observerStimmungAngabe = new ObserverStimmungAngabe();
        observerStimmungAngabe.update(context);

    }
}
