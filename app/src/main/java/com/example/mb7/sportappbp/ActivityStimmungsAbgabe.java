package com.example.mb7.sportappbp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityStimmungsAbgabe extends AppCompatActivity {
    StimmungsViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimmung);

        this.InitializeControlls();
        this.SetControlCaptions();

         super.onStart();
    }

    private void InitializeControlls(){

        // set the listivew
        // first create the adapter
        adapter = new StimmungsViewAdapter(this);
        ListView lst = (ListView)findViewById( R.id.lvAngespannt);
        ListView lstTraurig = (ListView)findViewById( R.id.lvTraurig);
        ListView lstTatkraeftig = (ListView)findViewById( R.id.lvTatkraeftig);
        ListView  lstZerstreut= (ListView)findViewById( R.id.lvZerstreut);
        ListView lstWuetend = (ListView)findViewById( R.id.lvWuetend);
        ListView lstMuede = (ListView)findViewById( R.id.lvMuede);
        ListView lstSelbstsicher = (ListView)findViewById( R.id.lvSelbstsicher);
        ListView lstMittelsam = (ListView)findViewById( R.id.lvMitteilsam);

        lst.setAdapter(adapter);
        lstTraurig.setAdapter(adapter);
        lstTatkraeftig.setAdapter(adapter);
        lstZerstreut.setAdapter(adapter);
        lstWuetend.setAdapter(adapter);
        lstMuede.setAdapter(adapter);
        lstSelbstsicher.setAdapter(adapter);
        lstMittelsam.setAdapter(adapter);

        // set the onTouch Event to disable scrolling
        lst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }

        });
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        // set the onTouch Event to disable scrolling
        lstTraurig.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });

        lstTraurig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        lstTatkraeftig.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        lstTatkraeftig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        lstZerstreut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        lstZerstreut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        lstWuetend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        lstWuetend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        lstMuede.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        lstMuede.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        lstSelbstsicher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        lstSelbstsicher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
        lstMittelsam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });
        lstMittelsam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });
    }

    private void SetControlCaptions(){
        ((TextView)findViewById(R.id.txtMainQuestion)).setText("Bitte geben Sie an, wie Sie sich jetzt d.h. in diesem Moment fühlen");
        ((TextView)findViewById(R.id.txtAngespannt)).setText("Angespannt");
        ((TextView)findViewById(R.id.txtTraurig)).setText("Traurig");


    }
}
