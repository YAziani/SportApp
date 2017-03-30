package com.tud.bp.fitup.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tud.bp.fitup.BusinessLayer.StimmungsAngabe;
import com.tud.bp.fitup.R;

import java.util.ArrayList;

/**
 * Created by M.Braei on 17.01.2017.
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

        // control and show the selected one as green while others as blue
        if (position == selectedIndex) {
            txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
        } else {
            txtTitle.setBackgroundColor(Color.parseColor("#4b6df2"));
        }

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
