package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by Basti on 21.03.2017.
 */

public class DiaryViewAdapter extends BaseAdapter {

    private Activity _context;
    private ArrayList<DiaryEntry> _diaryEntries;

    public DiaryViewAdapter(Activity context, ArrayList<DiaryEntry> diaryEntries)
    {
        _diaryEntries = diaryEntries;
        _context = context;
    }

    @Override
    public Object getItem(int position) {
        return _diaryEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        DiaryEntry diaryEntry = _diaryEntries.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);

        //Set the date and time of the diaryEntry into the text field
        TextView txtTitle =(TextView) view.findViewById(android.R.id.text1);
        txtTitle.setText(_context.getResources().getString(R.string.Vom) + diaryEntry.getDate() + ", " + _context.getResources().getString(R.string.um) +
                diaryEntry.getTime() + " " + _context.getResources().getString(R.string.Uhr));
        return view;
    }

    @Override
    public int getCount() {
        return _diaryEntries.size();
    }
}
