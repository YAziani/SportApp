package com.example.mb7.sportappbp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Felix on 19.01.2017.
 */

public class FragebogenListview extends ListView {
    public FragebogenListview(Context context) {super (context);}

    public FragebogenListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragebogenListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public FragebogenListview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context,attrs,defStyleAttr,defStyleRes);
    }

    public void Initialize()
    {
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
                view.setSelected(true);
            }
        });
    }


}