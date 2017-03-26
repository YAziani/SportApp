package com.example.mb7.sportappbp.Fragments;

import android.app.ProgressDialog;
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
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
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
    ProgressDialog pd;

    TbNotificationContent tbNotificationContent = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set the title of
        setTitle(getString( R.string.notifikationen));


       view = inflater.inflate(R.layout.tbnotificationcontent, container, false);

        tbNotificationContent = this;
        return view;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible){
            pd = new ProgressDialog(this.getContext());
            pd.setMessage(getString( R.string.wird_geladen));
            pd.show();

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
                                notification = new Notification( title,subtext,R.mipmap.ic_stimmungs_abgabe,date);
                            }
                            else if(title.equals( getString(R.string.tagebucheintrag))){
                                notification = new Notification( title,subtext,R.mipmap.ic_tagebuch_eintrag,date);
                            }
                            else if(title.equals( getString(R.string.fitnessfragebogen))){
                                notification = new Notification( title,subtext,R.mipmap.ic_aktivitaets_fragebogen,date);
                            }
                            else if(title.equals(getString(R.string.aktivitaetsfragebogen))){
                                notification = new Notification( title,subtext,R.mipmap.ic_fittnessfragebogen,date);
                            }


                            if (notification != null)
                                notifications.add(notification);
                        }

                    }

                    // reminder notification
                    boolean containsReminder = false;
                    for(Notification n : notifications) {
                        if(n.getTitle().equals(getString(R.string.trNotiTitle))) {
                            containsReminder = true;
                            break;
                        }
                    }
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String nextTrainingTime = preferences.getString("nextTrainingTime","");
                    if(!containsReminder && !nextTrainingTime.equals("")) {
                        notifications.add(new Notification(
                                getString(R.string.trNotiTitle),
                                getString(R.string.trNotiSmallTitle1)
                                        +" "+ MotivationMethod.timeTillTraining(nextTrainingTime)
                                        +" "+ getString(R.string.trNotiSmallTitle2).split("\n")[0],
                                R.mipmap.ic_fittnessfragebogen,new Date())
                        );
                    }

                    if (notifications != null) {
                        Collections.reverse(notifications);
                        // fill the recycler
                        rv = (RecyclerView) view.findViewById(R.id.recycler);
                        LinearLayoutManager lm = new LinearLayoutManager(getContext());
                        rv.setLayoutManager(lm);
                        // just create a list of tasks
                        rv.setAdapter(new NotificationAdapter(notifications, tbNotificationContent));

                    }

                    // close the progress dialog
                    pd.dismiss();

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
