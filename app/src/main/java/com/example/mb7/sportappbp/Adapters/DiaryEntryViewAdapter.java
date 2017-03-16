package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by Basti on 14.03.2017.
 */

public class DiaryEntryViewAdapter extends BaseAdapter{

    private Activity _context;
    private ArrayList<String> _categories;
    private DiaryEntry _diaryEntry;
    private ArrayList<Integer> _icons;

    public DiaryEntryViewAdapter(Activity context, ArrayList<String> categories, DiaryEntry diaryEntry,  ArrayList<Integer> icons)
    {
        _categories = categories;
        _context = context;
        _diaryEntry = diaryEntry;
        _icons = icons;
    }

    @Override
    public Object getItem(int position) {
        return _categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        String category = _categories.get(position);
        int iconID = _icons.get(position);

        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_totalactivity_cell, null);

        TextView txtCategory =(TextView) view.findViewById(R.id.category);
        txtCategory.setText(category);

        ImageView icon = (ImageView) view.findViewById(R.id.icon_total);
        icon.setImageResource(iconID);


        TextView txtTime = (TextView) view.findViewById(R.id.time_total);
        TextView txtPoints = (TextView) view.findViewById(R.id.points_total);

        String[] data;
        switch(category){
            case ("Leistungstests"):
                data = _diaryEntry.getTotalTimePointsAsArrayLeistungstests();
                txtTime.setText(data[0]);
                txtPoints.setText(data[1]);
                txtTime.setTextColor(0xFF99CC00); //green
                txtPoints.setTextColor(0xFF99CC00);
                break;
            case ("Training"):
                data = _diaryEntry.getTotalTimePointsAsArrayTraining();
                txtTime.setText(data[0]);
                txtPoints.setText(data[1]);
                txtTime.setTextColor(0xFFFFBB33); //orange
                txtPoints.setTextColor(0xFFFFBB33);
                break;
            case ("Wellness"):
                data = _diaryEntry.getTotalTimePointsAsArrayWellness();
                txtTime.setText(data[0]);
                txtPoints.setText(data[1]);
                txtTime.setTextColor(0xFFFF4444); //red
                txtPoints.setTextColor(0xFFFF4444);
                break;
            case ("Reiner Aufenthalt"):
                data = _diaryEntry.getTotalTimePointsAsArrayReinerAufenthalt();
                txtTime.setText(data[0]);
                txtPoints.setText(data[1]);
                txtTime.setTextColor(0xFF0000FF); //blue
                txtPoints.setTextColor(0xFF0000FF);
                break;
        }

        return view;
    }

    @Override
    public int getCount() {
        return _categories.size();
    }


    private String getTimeAsString(int hours, int min) {
        StringBuilder sb = new StringBuilder();
        String m = String.valueOf(min);
        String h = String.valueOf(hours);

        sb.append(h).append(":").append(m).append(" h");
        String result = sb.toString();
        return result;
    }
}

