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
import com.example.mb7.sportappbp.Observe.ObserverMotivationMessage;
import com.example.mb7.sportappbp.Observe.ObserverStimmungAngabe;
import com.example.mb7.sportappbp.Observe.ObserverTrainQuestioning;
import com.example.mb7.sportappbp.Observe.ObserverTrainingReminder;
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
    ObserverTrainingReminder observerTrainingReminder = null;
    ObserverMotivationMessage observerMotivationMessage = null;
    ObserverTrainQuestioning observerTrainQuestioning = null;

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {
       //android.os.Debug.waitForDebugger();

        // Initialize your observer and call its update method
        if (observerStimmungAngabe == null)
            observerStimmungAngabe = new ObserverStimmungAngabe();
        observerStimmungAngabe.update(context);

        if (observerTrainingReminder == null)
            observerTrainingReminder = new ObserverTrainingReminder(context);
        observerTrainingReminder.update(context);

        if (observerMotivationMessage == null)
            observerMotivationMessage = new ObserverMotivationMessage();
        observerMotivationMessage.update(context);

        if (observerTrainQuestioning == null)
            observerTrainQuestioning = new ObserverTrainQuestioning();
        observerTrainQuestioning.update(context);

        insertdb(context);
    }

    void insertdb(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + preferences.getString("logedIn","") + "/" );
        ref.child("AlertReceiver").setValue(new Date().toString());

    }
}
