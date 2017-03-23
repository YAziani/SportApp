package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.R;

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
        stimmungen.add(context.getString(R.string.ueberhaupt_nicht));
        stimmungen.add(context.getString(R.string.wenig));
        stimmungen.add(context.getString(R.string.mittelmaessig));
        stimmungen.add(context.getString(R.string.ziemlich));
        stimmungen.add(context.getString(R.string.extrem));

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
