package com.tud.bp.fitup.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tud.bp.fitup.BusinessLayer.Fragebogen;
import com.tud.bp.fitup.R;

import java.util.ArrayList;

/**
 * Created by Felix on 19.01.2017.
 */
public class FragebogenViewAdapter extends BaseAdapter {
    private Activity _context;
    private ArrayList<String> antworten;
    private int ImageId;
    private Fragebogen fragebogen = null;
    private String subject;
    private Integer selectedIndex = -1;

    /**
     * Instantiates a new Fragebogen view adapter.
     * @param context
     */
    public FragebogenViewAdapter(Activity context) {
        antworten = new ArrayList<String>();
        antworten.add(context.getString(R.string.Ja));
        antworten.add(context.getString(R.string.Nein));

        _context = context;
    }

    /**
     * @param fragebogen
     * @param subject
     */
    public void setAntworten(Fragebogen fragebogen, String subject) {
        this.fragebogen = fragebogen;
        this.subject = subject;
    }

    @Override
    public Object getItem(int position) {return antworten.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Setze Selektiertes Listfeld von blau auf grün
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        String nt = antworten.get(position);
        if (view == null)
            view = _context.getLayoutInflater().inflate(R.layout.lst_stimmnungsabgabe_cell, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(nt);

        if (position == selectedIndex) {
            txtTitle.setBackgroundColor(Color.parseColor("#037f23"));
        } else {
            txtTitle.setBackgroundColor(Color.parseColor("#4b6df2"));
        }

        return view;
    }

    /**
     * @param position
     */
    public void setSelectedIndex(Integer position) {
        selectedIndex = position;
    }

    /**
     * @return the selected index
     */
    public Integer getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public int getCount() {
        return antworten.size();
    }
}
