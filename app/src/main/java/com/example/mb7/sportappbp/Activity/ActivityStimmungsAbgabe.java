package com.example.mb7.sportappbp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.StimmungsViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrage;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.StimmungListview;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityStimmungsAbgabe extends AppCompatActivity {
    StimmungsViewAdapter adapter;
    StimmungListview lstTraurig;
    StimmungListview lstTatkraeftig ;
    StimmungListview  lstZerstreut;
    StimmungListview lstWuetend ;
    StimmungListview lstAngespannt ;
    StimmungListview lstMuede ;
    StimmungListview lstSelbstsicher ;
    StimmungListview lstMittelsam ;

    private Firebase mRootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimmung);

        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/players");
        this.InitializeControlls();
        this.SetControlCaptions();

         super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
        inflater.inflate(R.menu.menu_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_save:
                SaveData();
                finish();
                Toast.makeText(ActivityMain.activityMain,"Erfolgreich gespeichert",Toast.LENGTH_SHORT);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private StimmungAbfrage getData()
    {
        StimmungAbfrage stimmungAbfrage = new StimmungAbfrage();
        stimmungAbfrage.Angespannt = lstAngespannt.getIndex();
        stimmungAbfrage.Mitteilsam = lstMittelsam.getIndex();
        stimmungAbfrage.Muede = lstMuede.getIndex();
        stimmungAbfrage.Selbstsicher = lstSelbstsicher.getIndex();
        stimmungAbfrage.Tatkraeftig = lstTatkraeftig.getIndex();
        stimmungAbfrage.Traurig = lstTraurig.getIndex();
        stimmungAbfrage.Wuetend = lstWuetend.getIndex();
        stimmungAbfrage.Zerstreut= lstZerstreut.getIndex();
        stimmungAbfrage.Vor  = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        stimmungAbfrage.Date = sdf.format(new Date());

        return stimmungAbfrage;
    }

    private boolean SaveData()
    {
        StimmungAbfrage stimmungAbfrage  = getData();
        ActivityMain.mainUser.SaveStimmung(stimmungAbfrage);

        return true;
    }

    private void InitializeControlls(){

        // set the listivew
        // first create the adapter
        adapter = new StimmungsViewAdapter(this);
        lstTraurig = (StimmungListview)findViewById( R.id.lvTraurig);
        lstTatkraeftig = (StimmungListview)findViewById( R.id.lvTatkraeftig);
        lstZerstreut= (StimmungListview)findViewById( R.id.lvZerstreut);
        lstWuetend = (StimmungListview)findViewById( R.id.lvWuetend);
        lstAngespannt = (StimmungListview)findViewById( R.id.lvAngespannt);
        lstMuede = (StimmungListview)findViewById( R.id.lvMuede);
        lstSelbstsicher = (StimmungListview)findViewById( R.id.lvSelbstsicher);
        lstMittelsam = (StimmungListview)findViewById( R.id.lvMitteilsam);

        lstAngespannt.setAdapter(adapter);
        lstTraurig.setAdapter(adapter);
        lstTatkraeftig.setAdapter(adapter);
        lstZerstreut.setAdapter(adapter);
        lstWuetend.setAdapter(adapter);
        lstMuede.setAdapter(adapter);
        lstSelbstsicher.setAdapter(adapter);
        lstMittelsam.setAdapter(adapter);

        // set the onTouch Event to disable scrolling
        lstAngespannt.Initialize();
        lstTraurig.Initialize();
        lstTatkraeftig.Initialize();
        lstZerstreut.Initialize();
        lstWuetend.Initialize();
        lstMuede.Initialize();
        lstSelbstsicher.Initialize();
        lstMittelsam.Initialize();
    }

    private void SetControlCaptions(){
        ((TextView)findViewById(R.id.txtMainQuestion)).setText("Bitte geben Sie an, wie Sie sich jetzt d.h. in diesem Moment fühlen");
        ((TextView)findViewById(R.id.txtAngespannt)).setText("Angespannt");
        ((TextView)findViewById(R.id.txtTraurig)).setText("Traurig");


    }
}
