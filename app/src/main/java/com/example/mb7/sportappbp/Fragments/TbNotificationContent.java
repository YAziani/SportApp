package com.example.mb7.sportappbp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mb7.sportappbp.Adapters.NotificationAdapter;
import com.example.mb7.sportappbp.Adapters.NotificationViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.R;

import java.util.Arrays;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set the title of
        setTitle(getString( R.string.notifikationen));


        view = inflater.inflate(R.layout.tbnotificationcontent, container, false);

        // first create some notifications
        Notification n1 = new Notification(getString( R.string.tagebucheintrag),getString(R.string.nun_ist_es_soweit), R.mipmap.ic_trainingseinheit);
        Notification n2 = new Notification(getString( R.string.stimmungsabfrage), getString( R.string.wie_fuhlen_sie_moment), R.mipmap.ic_stimmungsabgabe);
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
        rv.setAdapter(new NotificationAdapter(notifications, this));
        return view;
    }



    @Override
    public void onStart() {
        if(rv != null && notifications != null) {
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
        }

//        NotificationAdapter temp;
/*
        adapter = new NotificationViewAdapter(getActivity(), notifications, android.R.drawable.ic_menu_edit);

        lst = (ListView)view.findViewById(R.id.lstNotifications);
        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( ((Notification)parent.getAdapter().getItem(position)).getTitle().equals("Trainingeintrag")) {
                    Intent open = new Intent(getActivity(), ActivityDiaryEntry.class);
                    startActivity(open);
                }
                else if (((Notification)parent.getAdapter().getItem(position)).getTitle().equals("Stimmungsabfrage"))
                {
                    Intent open = new Intent(getActivity(), ActivityStimmungsAbgabe.class);
                    startActivity(open);
                }
                else if (((Notification)parent.getAdapter().getItem(position)).getTitle().equals("Fitnessfragebogen"))
                {
                    Intent open = new Intent(getActivity(), ActivityFitnessFragebogen.class);
                    startActivity(open);
                }
                else if (((Notification)parent.getAdapter().getItem(position)).getTitle().equals("Fragebogen zur Aktivität"))
                {
                    Intent open = new Intent(getActivity(), ActivityFragebogen.class);
                    startActivity(open);
                }

                else if (((Notification)parent.getAdapter().getItem(position)).getTitle().equals("Schon gewusst?"))
                {
                    Intent open = new Intent(getActivity(), ActivityMotivationMessage.class);
                    startActivity(open);
                    notifications.remove(nMotivationMessage);
                    lst.setAdapter(adapter);
                }
            }
        });*/
        super.onStart();

    }
}
