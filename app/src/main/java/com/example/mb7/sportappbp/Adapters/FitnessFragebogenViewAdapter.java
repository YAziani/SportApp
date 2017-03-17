package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by Felix on 03.03.2017.
 */

public class FitnessFragebogenViewAdapter extends BaseAdapter {
    private Activity _context;
    private ArrayList<String > antworten;

    public FitnessFragebogenViewAdapter(Activity context)
    {
        antworten = new ArrayList<String>();
        antworten.add("Ich kann diese Tätigkeit nicht");
        antworten.add("Ich habe große Probleme");
        antworten.add("Ich habe mäßige Probleme");
        antworten.add("Ich habe leichte Probleme");
        antworten.add("Ich habe keine Probleme");

        _context = context;

    }

    @Override
    public Object getItem(int position)
    {return antworten.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        String nt = antworten.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_stimmnungsabgabe_cell, null);

        TextView txtTitle =(TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(nt);

        return view;
    }

    @Override
    public int getCount() {
        return antworten.size();
    }
}



