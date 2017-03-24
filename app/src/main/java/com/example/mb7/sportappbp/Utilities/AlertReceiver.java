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
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.util.Date;

/**
 * Created by M.Braei on 23.03.2017.
 */

public class AlertReceiver extends BroadcastReceiver {

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {
        android.os.Debug.waitForDebugger();
        // first save the data in database
        String sDate =  saveNotificationDB(context);
        // now show the notification
        createNotification(context, sDate);


    }

    String saveNotificationDB(Context context)
    {
        try
        {
            // get the current user
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

            // build the current URL
            Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + preferences.getString("logedIn","") + "/Notifications/" );

            // first the main node class of the class and the date as sub node
            String sDate = DAL_Utilities.ConvertDateTimeToFirebaseString( new Date());
            Firebase classRef =  ref.child(context.getString( R.string.stimmungsabfrage)).child(sDate);
            // now enter the the text of the notification as key-value
            Firebase node = classRef.child("subText");
            node.setValue(context.getString(R.string.ntf_stimmungsabfrage));

            return sDate;

        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            return "";
        }
        finally
        {

        }
    }

    public void createNotification(Context context, String NotificationDate){

        Intent contentStimmungsAbgabe = new Intent(context, ActivityStimmungsAbgabe.class);
        contentStimmungsAbgabe.putExtra("NotificationDate",NotificationDate);
        // Used to stack tasks across activites so we go to the proper place when back is clicked
        // create(context): context is the context that will launch the new task stack or a PendingIndent
        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(context);


        // Add all parents of this activity to the stack
        // The parentstck of MoreInfoNotifaction is defined in the Manifest -> <android:parentActivityName=".MainActivity">
        tStackBuilder.addParentStack(ActivityStimmungsAbgabe.class);

        // Add our new Intent to the stack
        tStackBuilder.addNextIntent(contentStimmungsAbgabe);

        PendingIntent notificIntent = tStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

// Define an Intent and an action to perform with it by another application
 /*       PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MoreInfoNotification.class), 0);
*/
        // Builds a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_stimmungsabgabe)
                        .setContentTitle(context.getString(R.string.stimmungsabfrage))                         // The title that is displayed
                        .setContentText(context.getString(R.string.ntf_stimmungsabfrage))              // This text is shown by the notification
                        .setTicker(context.getString(R.string.ntf_stimmungsabfrage))  ;            // This is not shown since Android 5.0 but still usefull to accessibility services

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notificIntent);

        // Set the default notification option
        // DEFAULT_SOUND : Make sound
        // DEFAULT_VIBRATE : Vibrate
        // DEFAULT_LIGHTS : Use the default light notification
        mBuilder.setDefaults(Notification.DEFAULT_ALL);

        // Auto cancels the notification when clicked on in the task bar
        // gets the notif away from the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());

    }
}
