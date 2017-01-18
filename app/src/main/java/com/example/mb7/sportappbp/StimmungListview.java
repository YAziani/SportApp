package com.example.mb7.sportappbp;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by MB7 on 18.01.2017.
 */

public class StimmungListview extends ListView  {
    public StimmungListview(Context context) {
    super(context);
}

    public void Initialize()
    {
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
            view.setSelected(true);
        }
    });
    }


}
