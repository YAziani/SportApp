package com.example.mb7.sportappbp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mb7.sportappbp.Activity.ActivityMain;
import com.example.mb7.sportappbp.Adapters.NotificationAdapter;
import com.example.mb7.sportappbp.Adapters.NotificationViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbNotificationContent extends TabFragment {

    List<Notification> notifications;
    ListView lst;
    NotificationViewAdapter adapter;
    RecyclerView rv;
    View view;

    TbNotificationContent tbNotificationContent = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set the title of
        setTitle(getString( R.string.notifikationen));


       view = inflater.inflate(R.layout.tbnotificationcontent, container, false);
/*
        // first create some notifications
        Notification n1 = new Notification(getString( R.string.tagebucheintrag),getString(R.string.nun_ist_es_soweit), R.mipmap.ic_trainingseinheit);
        Notification n2 = new Notification(getString( R.string.stimmungsabgabe), getString( R.string.wie_fuhlen_sie_moment), R.mipmap.ic_stimmungsabgabe);
        Notification n3 = new Notification(getString( R.string.aktivitaetsfragebogen), getString( R.string.wie_aktiv),R.mipmap.ic_aktivitaetfragebogen);
        Notification n4 = new Notification(getString( R.string.fitnessfragebogen), getString( R.string.wie_ist_fitnesstand),R.mipmap.ic_fitness_fragebogen);
        notifications =new LinkedList<Notification>(Arrays.asList(n1,n2,n3,n4));


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        // setup notifications that appear only on specific occasions
        final Notification nMotivationMessage = new Notification(
                getString( R.string.bewegen_sie_sich),
                getString( R.string.ihr_korper_ihnen_danken),R.drawable.trainingseinheit);
        if(preferences.getBoolean("motivationMessage",false)) {
            notifications.add(nMotivationMessage);
            preferences.edit().remove("motivationMessage").commit();
        }

        String nextTrainingTime = preferences.getString("nextTrainingTime", "");
        final Notification nPendingTraining = new Notification(
                getString( R.string.naechstes_training),
                getString( R.string.ihr_naechstes_train_begin_um)
                        + nextTrainingTime, R.drawable.trainingseinheit);
        if(preferences.getBoolean("reminderNotified", false) && !nextTrainingTime.equals("")){
            boolean notify = true;
            for(Notification n : notifications) {
                if(n.getTitle().equals(getString( R.string.naechstes_training))) {
                    notify = false;
                    break;
                }
            }
            if(notify) {
                notifications.add(nPendingTraining);
            }
        }

        // fill the recycler
        rv = (RecyclerView)view.findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        // just create a list of tasks
        rv.setAdapter(new NotificationAdapter(notifications, this));*/
        tbNotificationContent = this;
        return view;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible){
            readNotifications();

        }
    }

    // when the fragment becomes visible to the user
    @Override
    public void onStart() {
      /*  if(rv != null && notifications != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            // setup notifications that appear only on specific occasions
            final Notification nMotivationMessage = new Notification(
                    getString( R.string.bewegen_sie_sich),
                    getString( R.string.ihr_korper_ihnen_danken),R.drawable.trainingseinheit);
            if(preferences.getBoolean("motivationMessage",false)) {
                notifications.add(nMotivationMessage);
                preferences.edit().remove("motivationMessage").commit();
            }

            String nextTrainingTime = preferences.getString("nextTrainingTime", "");
            final Notification nPendingTraining = new Notification(
                    getString( R.string.naechstes_training),
                    getString( R.string.ihr_naechstes_train_begin_um)
                            + nextTrainingTime, R.drawable.trainingseinheit);
            if(preferences.getBoolean("reminderNotified", false) && !nextTrainingTime.equals("")){
                boolean notify = true;
                for(Notification n : notifications) {
                    if(n.getTitle().equals(getString( R.string.naechstes_training))) {
                        notify = false;
                        break;
                    }
                }
                if(notify) {
                    notifications.add(nPendingTraining);
                }
            }

            rv.setAdapter(new NotificationAdapter(notifications, this));
        }*/
        super.onStart();

    }

    void readNotifications(){
        try
        {
            URL url = new URL(DAL_Utilities.DatabaseURL + "users/" + ActivityMain.mainUser.getName()+ "/Notifications/");
            final Firebase root = new Firebase(url.toString());

            root.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String  strKey = "";
                    // dataSnapshot.getKey() declares which strategy the notification belongs to (Stimmungsabgabe....)
                    notifications = new LinkedList<Notification>();
                    // the child.key of dataSnapshop declare the unique datetime of the notification
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String title = child.getKey();
                        for (DataSnapshot dates : child.getChildren())
                        {
                            Date date = DAL_Utilities.ConvertFirebaseStringToDateTime(dates.getKey());

                            // Now we can read for each unique datetime the text
                            String subtext = ((DataSnapshot) dates).child("subText").getValue().toString();

                            Notification notification = null;
                            // Now create our Notifications
                            if (title.equals( getString( R.string.stimmungsabgabe))){
                                notification = new Notification( title,subtext,R.mipmap.ic_stimmungsabgabe,date);
                            }
                            else if(title.equals( getString(R.string.tagebucheintrag))){
                                notification = new Notification( title,subtext,R.mipmap.ic_trainingseinheit,date);
                            }
                            else if(title.equals( getString(R.string.fitnessfragebogen))){
                                notification = new Notification( title,subtext,R.mipmap.ic_fitness_fragebogen,date);
                            }
                            else if(title.equals(getString(R.string.aktivitaetsfragebogen))){
                                notification = new Notification( title,subtext,R.mipmap.ic_aktivitaetfragebogen,date);
                            }


                            if (notification != null)
                                notifications.add(notification);
                        }

                    }
                    if (notifications.size() != 0) {
                        // fill the recycler
                        rv = (RecyclerView) view.findViewById(R.id.recycler);
                        LinearLayoutManager lm = new LinearLayoutManager(getContext());
                        rv.setLayoutManager(lm);
                        // just create a list of tasks
                        rv.setAdapter(new NotificationAdapter(notifications, tbNotificationContent));
                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return ;

        }
        catch (Exception e)
        {
            Log.d("ERROR", e.getMessage());
            return ;
        }
    }

}
