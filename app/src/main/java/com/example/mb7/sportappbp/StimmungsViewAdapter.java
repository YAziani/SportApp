package com.example.mb7.sportappbp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MB7 on 17.01.2017.
 */

public class StimmungsViewAdapter extends BaseAdapter {
    private Activity _context;
    private ArrayList<String > stimmungen;
    private int ImageId;

    public StimmungsViewAdapter(Activity context)
    {
        stimmungen = new ArrayList<String>();
        stimmungen.add("Überhaupt nicht");
        stimmungen.add("Wenig");
        stimmungen.add("Mittelmäßig");
        stimmungen.add("Ziemlich");
        stimmungen.add("Extrem");

        _context = context;
    }

    @Override
    public Object getItem(int position) {
        return stimmungen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        String nt = stimmungen.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_stimmnungsabgabe_cell, null);

        TextView txtTitle =(TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(nt);

        return view;
    }

    @Override
    public int getCount() {
        return stimmungen.size();
    }
}
