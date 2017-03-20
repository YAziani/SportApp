package com.example.mb7.sportappbp.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mb7.sportappbp.MotivationMethods.MotivationMessage;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.MotivationMethods.TrainQuestioning;
import com.example.mb7.sportappbp.MotivationMethods.TrainingReminder;
import com.example.mb7.sportappbp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class ActivityPresentationFunctions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_functions);

        ListView listView = (ListView)findViewById(R.id.funcList);
        String[] arr = new String[3];
        arr[0] = "Trainingserinnerung";
        arr[1] = "Motivationsnachricht";
        arr[2] = "Trainingsabfrage";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,0,arr);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // reminder
                if(position == 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE, 30);
                    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    String displayedHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                    String displayedMinute = String.valueOf(calendar.get(Calendar.MINUTE));

                    // insert a 0 if necessary
                    if(hourOfDay < 10) {
                        displayedHour = "0" + String.valueOf(hourOfDay);
                    }
                    if(minute < 10) {
                        displayedMinute = "0" + String.valueOf(minute);
                    }

                    String trainingStartTime = displayedHour+":"+displayedMinute;

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ActivityPresentationFunctions.this.getApplicationContext());
                    preferences.edit().putBoolean("reminderNotified", true).commit();
                    preferences.edit().putString("nextTrainingTime", trainingStartTime).commit();

                    final int notificationId = 4292;

                    // get time until training begins
                    int timeTillTraining = MotivationMethod.timeTillTraining(trainingStartTime);

                    // setup notification builder
                    final NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(ActivityPresentationFunctions.this)
                                    .setStyle(new NotificationCompat.BigTextStyle())
                                    .setSmallIcon(R.drawable.weight_icon)
                                    .setContentTitle("Trainingserinnerung")
                                    .setContentText("Ihr Training beginnt in etwa " + timeTillTraining + " Minuten"
                                            +"\nWerden Sie teilnehmen?");
                    // specify which activity should be started upon clicking on the notification
                    Intent intent = new Intent(ActivityPresentationFunctions.this,ActivityMain.class);
                    intent.putExtra("startTab",1);
                    intent.putExtra("notificationId", notificationId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ActivityPresentationFunctions.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.setContentIntent(pendingIntent);

                    // setting up buttons for question (will you go to training?)
                    Intent intentYes = new Intent(ActivityPresentationFunctions.this,ActivityTrainQuestioning.class);
                    intentYes.setAction("YES_ACTION");
                    intentYes.putExtra("notificationId", notificationId);
                    intentYes.putExtra("praiseOrWarn", 0);
                    intentYes.putExtra("preTrain", true);
                    //intentYes.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    PendingIntent pendingIntentYes = PendingIntent.getActivity(ActivityPresentationFunctions.this,0,intentYes,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.addAction(R.drawable.box,"Ja",pendingIntentYes);

                    Intent intentNo = new Intent(ActivityPresentationFunctions.this,ActivityTrainQuestioning.class);
                    intentNo.setAction("NO_ACTION");
                    intentNo.putExtra("notificationId", notificationId);
                    intentNo.putExtra("praiseOrWarn", 1);
                    intentYes.putExtra("preTrain", true);
                    PendingIntent pendingIntentNo = PendingIntent.getActivity(ActivityPresentationFunctions.this,0,intentNo,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.addAction(R.drawable.box,"Nein",pendingIntentNo);

                    // setup notification manager
                    final NotificationManager notificationManager =
                            (NotificationManager) ActivityPresentationFunctions.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    // send notification
                    notificationManager.notify(notificationId,notificationBuilder.build());
                }


                // reminder
                if(position == 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE, 5);
                    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    String displayedHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                    String displayedMinute = String.valueOf(calendar.get(Calendar.MINUTE));

                    // insert a 0 if necessary
                    if(hourOfDay < 10) {
                        displayedHour = "0" + String.valueOf(hourOfDay);
                    }
                    if(minute < 10) {
                        displayedMinute = "0" + String.valueOf(minute);
                    }

                    String trainingStartTime = displayedHour+":"+displayedMinute;

                    MotivationMessage m = new MotivationMessage(ActivityPresentationFunctions.this);
                    m.run(trainingStartTime);
                }

                // questioning
                if(position == 2) {
                    // setup notification builder
                    final int notificationId = 824243;
                    final NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(ActivityPresentationFunctions.this)
                                    .setStyle(new NotificationCompat.BigTextStyle())
                                    .setSmallIcon(R.drawable.weight_icon)
                                    .setContentTitle("Trainiert?")
                                    .setContentText("Haben Sie Ihren Trainingstermin wahrgenommen?");
                    // specify which activity should be started upon clicking on the notification
                    Intent intent = new Intent(ActivityPresentationFunctions.this,ActivityMain.class);
                    intent.putExtra("startTab",1);
                    intent.putExtra("notificationId", notificationId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ActivityPresentationFunctions.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.setContentIntent(pendingIntent);

                    // setting up buttons for question (will you go to training?)
                    Intent intentYes = new Intent(ActivityPresentationFunctions.this,ActivityTrainQuestioning.class);
                    intentYes.setAction("YES_ACTION");
                    intentYes.putExtra("notificationId", notificationId);
                    intentYes.putExtra("praiseOrWarn", 0);
                    PendingIntent pendingIntentYes = PendingIntent.getActivity(ActivityPresentationFunctions.this,0,intentYes,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.addAction(R.drawable.box,"Ja",pendingIntentYes);

                    Intent intentNo = new Intent(ActivityPresentationFunctions.this,ActivityTrainQuestioning.class);
                    intentNo.setAction("NO_ACTION");
                    intentNo.putExtra("notificationId", notificationId);
                    intentNo.putExtra("praiseOrWarn", 1);
                    PendingIntent pendingIntentNo = PendingIntent.getActivity(ActivityPresentationFunctions.this,0,intentNo,PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.addAction(R.drawable.box,"Nein",pendingIntentNo);

                    // setup notification manager
                    final NotificationManager notificationManager =
                            (NotificationManager) ActivityPresentationFunctions.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    // send notification
                    notificationManager.notify(notificationId,notificationBuilder.build());
                }

            }
        });

    }
}
