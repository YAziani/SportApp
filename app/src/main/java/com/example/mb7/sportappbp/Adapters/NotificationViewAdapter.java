package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.Notification;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by MB7 on 17.01.2017.
 */

public class NotificationViewAdapter extends BaseAdapter {
    private Activity _context;
    private ArrayList<Notification> _notifications;
    private int ImageId;

    public NotificationViewAdapter(Activity context, ArrayList<Notification> notifications, int imageID) {
        _notifications = notifications;
        _context = context;
        ImageId = imageID;
    }

    @Override
    public Object getItem(int position) {
        return _notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Notification nt = _notifications.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lstnotificationcell, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(nt.getTitle());

        TextView txtText = (TextView) view.findViewById(R.id.txtText);
        txtText.setText(nt.getSubText());

        return view;
    }

    @Override
    public int getCount() {
        return _notifications.size();
    }
}
