package com.example.mb7.sportappbp.Observe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.pm.ActivityInfoCompat;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityTrainQuestioning;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMessage;
import com.example.mb7.sportappbp.R;

/**
 * Created by Intirion on 25.03.2017.
 */

public class ObserverTrainQuestioning extends Observer{

    short timeOutCounter = 0;

    @Override
    public void update(Context context) {
        if(timeOutCounter > 0) {
            timeOutCounter--;
        }else {
            if(!getLastTrainingTimeString(context).equals("")
                    && MotivationMessage.timeTillTraining(getLastTrainingTimeString(context)) == -59) {
                sendNotification(
                        context,
                        "Trainingsabfrage",
                        ActivityMain.class,
                        context.getString(R.string.tqNotiTitle),
                        context.getString(R.string.tqNotiSmallTitle),
                        R.mipmap.ic_trainingseinheit);
                timeOutCounter = 5;
            }
        }
    }

    @Override
    public void createNotification(Context context, String NotificationDate, Class<?> cls,String title, String text, Integer icon ){

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Intent contentClass = new Intent(context, cls);
        contentClass.putExtra("NotificationDate",NotificationDate);
        // Used to stack tasks across activites so we go to the proper place when back is clicked
        // create(context): context is the context that will launch the new task stack or a PendingIndent
        TaskStackBuilder tStackBuilder = TaskStackBuilder.create(context);

        // Add all parents of this activity to the stack
        // The parentstck of MoreInfoNotifaction is defined in the Manifest -> <android:parentActivityName=".MainActivity">
        tStackBuilder.addParentStack(cls);

        // Add our new Intent to the stack
        tStackBuilder.addNextIntent(contentClass);

        if(preferences.getBoolean("willTrain",true)) {
            final NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context)
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setSmallIcon(icon)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setTicker(text);
            // specify which activity should be started upon clicking on the notification
            Intent intent = new Intent(context,ActivityMain.class);
            intent.putExtra("startTab",1);
            intent.putExtra("notificationId", 331);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);

            // setting up buttons for question (will you go to training?)
            Intent intentYes = new Intent(context,ActivityTrainQuestioning.class);
            intentYes.setAction("YES_ACTION");
            intentYes.putExtra("notificationId", 331);
            intentYes.putExtra("praiseOrWarn", 0);
            PendingIntent pendingIntentYes = PendingIntent.getActivity(context,0,intentYes,PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.addAction(R.drawable.box,"Ja",pendingIntentYes);

            Intent intentNo = new Intent(context,ActivityTrainQuestioning.class);
            intentNo.setAction("NO_ACTION");
            intentNo.putExtra("notificationId", 331);
            intentNo.putExtra("praiseOrWarn", 1);
            PendingIntent pendingIntentNo = PendingIntent.getActivity(context,0,intentNo,PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.addAction(R.drawable.box,"Nein",pendingIntentNo);

            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);

            // setup notification manager
            final NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // send notification
            notificationManager.notify(331,notificationBuilder.build());
        }else {
            preferences.edit().remove("willTrain").apply();
        }

    }
}
