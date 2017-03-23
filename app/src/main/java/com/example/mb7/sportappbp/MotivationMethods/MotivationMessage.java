package com.example.mb7.sportappbp.MotivationMethods;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.R;

/**
 * shows the user a motivation message before or after training
 * Created by Aziani on 27.01.2017.
 */
public class MotivationMessage extends MotivationMethod{

    public MotivationMessage(Activity activity) {
        super(activity);
    }

    @Override
    public boolean run(String trainingStartTime) {
        if(timeTillTraining(trainingStartTime) == 5) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            preferences.edit().putBoolean("motivationMessage", true).commit();
            notifyUser();
            return true;
        }
        return false;
    }

    /**
     * notify the user about a new motivation message
     */
    private void notifyUser() {

        final int notificationId = 6818;

        // setup notification builder
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.weight_icon)
                        .setContentTitle("Schon gewusst?")
                        .setContentText("Wissenswertes über Sport");
        // specify which activity should be started upon clicking on the notification
        Intent notificationIntent = new Intent(activity,ActivityMain.class);
        notificationIntent.putExtra("startTab",1);
        notificationIntent.putExtra("notificationId",notificationId);
        PendingIntent intent = PendingIntent.getActivity(activity,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(intent);
        // setup notification manager
        final NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        // send notification
        notificationManager.notify(
                notificationId,
                notificationBuilder.setContentText(
                        "Wissenswertes über Sport").build()
        );
    }
}
