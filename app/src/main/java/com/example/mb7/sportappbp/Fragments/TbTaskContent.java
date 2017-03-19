package com.example.mb7.sportappbp.Fragments;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityFragebogen;
import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.Adapters.NotificationViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbTaskContent extends TabFragment {

    View view;
    ListView listView;
    ArrayList<Notification> notificationList;
    NotificationViewAdapter notificationViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setTitle("Tätigkeiten");
        view = inflater.inflate(R.layout.tbtaskcontent, container, false);
        return view;
    }

    @Override
    public void onStart() {


        final SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity()
                .getApplicationContext());
        notificationList = new ArrayList<>();

        // setup notifications that appear only on specific occasions
        final Notification nPendingTraining = new Notification(
                "nächstes Training",
                "Ihr nächstes Training beginnt um "
                        + preferences.getString("nextTrainingTime", "00:00"), R.drawable.trainingseinheit);
        if(preferences.getBoolean("reminderNotified", false)) {
            notificationList.add(nPendingTraining);
        }

        notificationViewAdapter = new NotificationViewAdapter(getActivity(),
                notificationList,
                android.R.drawable.ic_menu_edit);
        listView = (ListView)view.findViewById(R.id.lstTasks);
        listView.setAdapter(notificationViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position) == nPendingTraining) {
                    Toast.makeText(
                            getContext(),
                            "Ihr Training beginnt in "
                            + MotivationMethod.timeTillTraining(preferences.getString("nextTrainingTime", "00:00"))
                            + " Minuten.",
                            Toast.LENGTH_SHORT
                    ).show();
                    Animation hyperspaceJumpAnimation = AnimationUtils
                            .loadAnimation(ActivityMain.activityMain, R.anim.animation);
                    view.startAnimation(hyperspaceJumpAnimation);
                }
            }
        });
        super.onStart();

    }

}
