package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by Felix on 03.03.2017.
 */

public class FitnessFragebogenViewAdapter extends BaseAdapter {
    private Activity _context;
    private ArrayList<String > antworten;
    private int ImageId;
    private FitnessFragebogen fitnessFragebogen=null;
    private String subject;
    private Integer selectedIndex=-1;

    public FitnessFragebogenViewAdapter(Activity context)
    {
        antworten = new ArrayList<String>();
        antworten.add(context.getString(R.string.Ich_kann_diese_Tätigkeit_nicht));
        antworten.add(context.getString(R.string.Ich_habe_große_Probleme));
        antworten.add(context.getString(R.string.Ich_habe_mäßige_Probleme));
        antworten.add(context.getString(R.string.Ich_habe_leichte_Probleme));
        antworten.add(context.getString(R.string.Ich_habe_keine_Probleme));

        _context = context;

    }

    public void setFitnessFragebogen(FitnessFragebogen fitnessFragebogen, String subject){
        this.fitnessFragebogen=fitnessFragebogen;
        this.subject=subject;
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

        if (position == selectedIndex) {
            txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
        }
        else {
            txtTitle.setBackgroundColor(Color.parseColor("#4b6df2"));
        }

        return view;
    }

    public void setSelectedIndex(Integer position){
        selectedIndex = position;
    }

    public Integer getSelectedIndex()
    {
        return selectedIndex;
    }

    @Override
    public int getCount() {
        return antworten.size();
    }
}



