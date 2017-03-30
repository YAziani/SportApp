package com.tud.bp.fitup.UI_Controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tud.bp.fitup.Adapters.FitnessFragebogenViewAdapter;
import com.tud.bp.fitup.Adapters.FragebogenViewAdapter;
import com.tud.bp.fitup.Adapters.FragebogenViewAdapter2;

/**
 * Created by Felix on 19.01.2017.
 */
public class FragebogenListview extends ListView {
    private int index = -1;
    /**
     * The Lst.
     */
    FragebogenListview lst = this;

    /**
     * Instantiates a new Fragebogen listview.
     *
     * @param context the context
     */
    public FragebogenListview(Context context) {super(context);}

    /**
     * Instantiates a new Fragebogen listview.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public FragebogenListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Fragebogen listview.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public FragebogenListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Instantiates a new Fragebogen listview.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes the def style res
     */
    public FragebogenListview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void InitializeFitness() {
        // we do this to disable scrolling the listview
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        // we do this so that the selected color remains after clicking on an item
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                String val = Integer.toString(index);
                ((FitnessFragebogenViewAdapter) lst.getAdapter()).setSelectedIndex(position);
                ((FitnessFragebogenViewAdapter) lst.getAdapter()).notifyDataSetChanged();
                view.setSelected(true);
            }
        });
    }

    /**
     * @return selectedIndex
     */
    public int getIndexFitness() {
        return ((FitnessFragebogenViewAdapter) getAdapter()).getSelectedIndex();
    }

    /**
     * Initiialize ViewAdapter des BSA-Fragebogens
     */
    public void InitializeBSA() {
        // we do this to disable scrolling the listview
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        // we do this so that the selected color remains after clicking on an item
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                String val = Integer.toString(index);
                ((FragebogenViewAdapter2) lst.getAdapter()).setSelectedIndex(position);
                ((FragebogenViewAdapter2) lst.getAdapter()).notifyDataSetChanged();
                view.setSelected(true);
            }
        });
    }

    /**
     * Genau wie Initialize BSA, nur dass das eingegebene Layout verschwindet, wenn der Index des gewählten Listviews
     * > 0 ist
     *
     * @param llayout
     */
    public void visibility(final LinearLayout llayout) {
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });

        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                String val = Integer.toString(index);
                ((FragebogenViewAdapter) lst.getAdapter()).setSelectedIndex(position);
                ((FragebogenViewAdapter) lst.getAdapter()).notifyDataSetChanged();

                if (index > 0) {
                    llayout.setVisibility(LinearLayout.GONE);
                } else
                    llayout.setVisibility(LinearLayout.VISIBLE);

                view.setSelected(true);
            }
        });
    }

    /**
     * @return selectedIndex
     */
    public int getIndexBSA1() {
        return ((FragebogenViewAdapter) getAdapter()).getSelectedIndex();
    }

    /**
     * @return selected Index
     */
    public int getIndexBSA2() {
        return ((FragebogenViewAdapter2) getAdapter()).getSelectedIndex();
    }
}