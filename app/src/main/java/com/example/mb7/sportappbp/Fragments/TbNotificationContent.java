package com.example.mb7.sportappbp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mb7.sportappbp.Activity.ActivityDiaryEntry;
import com.example.mb7.sportappbp.Activity.ActivityMotivationMessage;
import com.example.mb7.sportappbp.Activity.ActivityStimmungsAbgabe;
import com.example.mb7.sportappbp.Activity.ActivityFragebogen;
import com.example.mb7.sportappbp.Adapters.NotificationViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbNotificationContent extends TabFragment {

    ArrayList<Notification> notifList;
    ListView lst;
    NotificationViewAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setTitle("Notifikationen");
        view = inflater.inflate(R.layout.tbnotificationcontent, container, false);

        return view;
    }

    @Override
    public void onStart() {
        Notification n1 = new Notification("Trainingeintrag","Nun ist es soweit. Haben Sie heute trainert?");
        Notification n2 = new Notification("Stimmungsabfrage", "Wie fühlen Sie sich in dem Moment?");
        Notification n5 = new Notification("Fragebogen zur Aktivität", "Wie aktiv sind Sie?");

        notifList = new ArrayList<>();
        notifList.add(n1);
        notifList.add(n2);
        notifList.add(n5);

        final Notification nMotivationMessage = new Notification(
                "Schon gewusst?",
                "Wissenswertes über Sport");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(preferences.getBoolean("motivationMessage",false)) {
            notifList.add(nMotivationMessage);
            preferences.edit().putBoolean("motivationMessage",false).commit();
        }
        adapter = new NotificationViewAdapter(getActivity(),notifList, android.R.drawable.ic_menu_edit);
        lst = (ListView)view.findViewById(R.id.lstNotifications);
        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( ((Notification)parent.getAdapter().getItem(position)).getTitle() == "Trainingeintrag") {
                    Intent open = new Intent(getActivity(), ActivityDiaryEntry.class);
                    startActivity(open);
                }
                else if (((Notification)parent.getAdapter().getItem(position)).getTitle() == "Stimmungsabfrage")
                {
                    Intent open = new Intent(getActivity(), ActivityStimmungsAbgabe.class);
                    startActivity(open);
                }
                else if (((Notification)parent.getAdapter().getItem(position)).getTitle() == "Fragebogen zur Aktivität")
                {
                    Intent open = new Intent(getActivity(), ActivityFragebogen.class);
                    startActivity(open);
                }
                else if (((Notification)parent.getAdapter().getItem(position)).getTitle() == "Schon gewusst?")
                {
                    Intent open = new Intent(getActivity(), ActivityMotivationMessage.class);
                    startActivity(open);
                    notifList.remove(nMotivationMessage);
                    lst.setAdapter(adapter);
                }

            }
        });
        super.onStart();

    }
}
