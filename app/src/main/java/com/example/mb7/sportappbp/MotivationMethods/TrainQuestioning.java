package com.example.mb7.sportappbp.MotivationMethods;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityTrainQuestioning;
import com.example.mb7.sportappbp.R;

/**
 * class that handles the questioning of the user whether he trained or not
 * Created by Aziani on 11.03.2017.
 */

public class TrainQuestioning extends MotivationMethod {

    public TrainQuestioning(Activity activity) {
        super(activity);
    }

    @Override
    public boolean run(final String trainingStartTime) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

        if(timeTillTraining(trainingStartTime) == 0) {
            if(preferences.getBoolean("willTrain",true)) {
                Handler handler = new Handler();
                // save last training and plan the questioning of the user
                handler.postDelayed(new Runnable() {
                    String trainingTime = trainingStartTime;
                    @Override
                    public void run() {
                        // setup notification builder
                        final int notificationId = 824243;
                        final NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(activity)
                                        .setStyle(new NotificationCompat.BigTextStyle())
                                        .setSmallIcon(R.drawable.weight_icon)
                                        .setContentTitle("Trainiert?")
                                        .setContentText("Haben Sie Ihren Trainingstermin wahrgenommen?");
                        // specify which activity should be started upon clicking on the notification
                        Intent intent = new Intent(activity,ActivityMain.class);
                        intent.putExtra("startTab",1);
                        intent.putExtra("notificationId", notificationId);
                        PendingIntent pendingIntent = PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationBuilder.setContentIntent(pendingIntent);

                        // setting up buttons for question (will you go to training?)
                        Intent intentYes = new Intent(activity,ActivityTrainQuestioning.class);
                        intentYes.setAction("YES_ACTION");
                        intentYes.putExtra("notificationId", notificationId);
                        intentYes.putExtra("praiseOrWarn", 0);
                        PendingIntent pendingIntentYes = PendingIntent.getActivity(activity,0,intentYes,PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationBuilder.addAction(R.drawable.box,"Ja",pendingIntentYes);

                        Intent intentNo = new Intent(activity,ActivityTrainQuestioning.class);
                        intentNo.setAction("NO_ACTION");
                        intentNo.putExtra("notificationId", notificationId);
                        intentNo.putExtra("praiseOrWarn", 1);
                        PendingIntent pendingIntentNo = PendingIntent.getActivity(activity,0,intentNo,PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationBuilder.addAction(R.drawable.box,"Nein",pendingIntentNo);

                        // setup notification manager
                        final NotificationManager notificationManager =
                                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                        // send notification
                        notificationManager.notify(notificationId,notificationBuilder.build());

                    }
                }, 10800000);
                return true;
            }else {
                preferences.edit().remove("willTrain").apply();
            }
        }
        return false;
    }
}
