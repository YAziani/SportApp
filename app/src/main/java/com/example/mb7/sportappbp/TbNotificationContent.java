package com.example.mb7.sportappbp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mb7.sportappbp.BusinessLayer.Notification;

import java.util.ArrayList;

/**
 * Created by MB7 on 07.01.2017.
 */

public class TbNotificationContent extends TabFragment {

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
        Notification n3 = new Notification("Fragebogen zur Aktivität", "Messung der Bewegungs- und Sportaktivität");

        ArrayList<Notification> notifList = new ArrayList<Notification>();
        notifList.add(n1);
        notifList.add(n2);
        notifList.add(n3);
        adapter = new NotificationViewAdapter(getActivity(),notifList, android.R.drawable.ic_menu_edit);
        ListView lst = (ListView)view.findViewById( R.id.lstNotifications);
        lst.setAdapter(adapter);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( ((Notification)parent.getAdapter().getItem(position)).getTitle() == "Trainingeintrag") {
                    Intent open = new Intent(getActivity(), DiaryEntryActivity.class);
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
            }
        });
        super.onStart();

    }
}
