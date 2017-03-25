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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Basti on 14.03.2017.
 */

public class DiaryEntryViewAdapter extends BaseAdapter{

    private Activity _context;
    private ArrayList<Integer> _categories;
    private DiaryEntry _diaryEntry;
    private ArrayList<Integer> _icons;

    public DiaryEntryViewAdapter(Activity context, ArrayList<Integer> categories, DiaryEntry diaryEntry,  ArrayList<Integer> icons)
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
        int category = _categories.get(position);
        int iconID = _icons.get(position);

        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_totalactivity_cell, null);

        //Name the category name in the grid
        TextView txtCategory =(TextView) view.findViewById(R.id.category);
        txtCategory.setText(category);

        //Place the icon for the category in the grid
        ImageView icon = (ImageView) view.findViewById(R.id.icon_total);
        icon.setImageResource(iconID);

        //get the txtViews for time and points
        TextView txtTime = (TextView) view.findViewById(R.id.time_total);
        TextView txtPoints = (TextView) view.findViewById(R.id.points_total);

        //for each grid/category set the total time, total point and the color
        DecimalFormat df = new DecimalFormat("00");
        int[] data;
        switch(category){
            case (R.string.Leistungstests):
                //get the total time (hours, minutes) and total points as an array
                data = _diaryEntry.getTotalTimePointsAsArrayLeistungstests();
                txtTime.setText(df.format(data[0]).toString() + ":" + df.format(data[1]).toString() + " h");
                txtPoints.setText(data[2] + " " + _context.getResources().getString(R.string.Pkt));
                txtTime.setTextColor(0xFF99CC00); //green
                txtPoints.setTextColor(0xFF99CC00);
                break;
            case (R.string.Training):
                data = _diaryEntry.getTotalTimePointsAsArrayTraining();
                txtTime.setText(df.format(data[0]).toString() + ":" + df.format(data[1]).toString() + " h");
                txtPoints.setText(data[2] + " " + _context.getResources().getString(R.string.Pkt));
                txtTime.setTextColor(0xFFFFBB33); //orange
                txtPoints.setTextColor(0xFFFFBB33);
                break;
            case (R.string.Wellness):
                data = _diaryEntry.getTotalTimePointsAsArrayWellness();
                txtTime.setText(df.format(data[0]).toString() + ":" + df.format(data[1]).toString() + " h");
                txtPoints.setText(data[2] + " " + _context.getResources().getString(R.string.Pkt));
                txtTime.setTextColor(0xFFFF4444); //red
                txtPoints.setTextColor(0xFFFF4444);
                break;
            case (R.string.ReinerAufenthalt):
                data = _diaryEntry.getTotalTimePointsAsArrayReinerAufenthalt();
                txtTime.setText(df.format(data[0]).toString() + ":" + df.format(data[1]).toString() + " h");
                txtPoints.setText(data[2] + " " + _context.getResources().getString(R.string.Pkt));
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

    public String buildString(int points){
        StringBuilder sb = new StringBuilder();
        sb.append(points).append(" ");

        return sb.toString();
    }

}

