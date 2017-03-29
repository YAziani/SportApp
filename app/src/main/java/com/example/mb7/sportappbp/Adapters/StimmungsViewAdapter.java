package com.example.mb7.sportappbp.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

/**
 * Created by MB7 on 17.01.2017.
 */

public class StimmungsViewAdapter extends BaseAdapter {
    private Activity _context;
    private ArrayList<String> stimmungen;
    private int ImageId;
    private StimmungsAngabe stimmungsAngabe = null;
    private String subject;
    private Integer selectedIndex = -1;

    public StimmungsViewAdapter(Activity context) {
        stimmungen = new ArrayList<String>();
        stimmungen.add(context.getString(R.string.ueberhaupt_nicht));
        stimmungen.add(context.getString(R.string.wenig));
        stimmungen.add(context.getString(R.string.mittelmaessig));
        stimmungen.add(context.getString(R.string.ziemlich));
        stimmungen.add(context.getString(R.string.extrem));

        _context = context;
    }

    public void setStimmung(StimmungsAngabe stimmungsAngabe, String subject) {
        this.stimmungsAngabe = stimmungsAngabe;
        this.subject = subject;
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

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(nt);

        if (position == selectedIndex) {
            txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
        } else {
            txtTitle.setBackgroundColor(Color.parseColor("#4b6df2"));
        }
 /*       if (stimmungsAngabe != null) {
            if (stimmungsAngabe.Angespannt != null && subject.equals( _context.getString(R.string.angespannt)) &&
            stimmungsAngabe.Angespannt == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Mitteilsam != null && subject.equals( _context.getString(R.string.mitteilsam)) &&
            stimmungsAngabe.Mitteilsam == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Muede != null && subject.equals( _context.getString(R.string.muede)) &&
            stimmungsAngabe.Muede == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Selbstsicher != null && subject.equals( _context.getString(R.string.selbstsicher)) &&
             stimmungsAngabe.Selbstsicher == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Tatkraeftig != null && subject.equals( _context.getString(R.string.tatkraeftig)) &&
            stimmungsAngabe.Tatkraeftig == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Traurig != null && subject.equals( _context.getString(R.string.traurig)) &&
            stimmungsAngabe.Traurig == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Wuetend != null && subject.equals( _context.getString(R.string.wuetend)) &&
            stimmungsAngabe.Wuetend == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
            if (stimmungsAngabe.Zerstreut != null && subject.equals( _context.getString(R.string.zerstreut)) &&
            stimmungsAngabe.Zerstreut == position )
                txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
        }*/

        return view;
    }

    public void setSelectedIndex(Integer position) {
        selectedIndex = position;
    }

    public Integer getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public int getCount() {
        return stimmungen.size();
    }
}
