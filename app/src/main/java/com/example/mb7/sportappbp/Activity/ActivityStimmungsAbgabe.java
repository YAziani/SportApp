package com.example.mb7.sportappbp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.StimmungsViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.BusinessLayer.StimmungAbfrageScore;
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

    int angespanntwert=0;
    int traurigwert=0;
    int tatkraeftigwert=0;
    int zerstreutwert=0;
    int wuetendwert=0;
    int muedewert=0;
    int selbstsicherwert=0;
    int mitteilsamwert=0;
    int StimmungScore=0;
    float EnergieIndexScore=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("Oncreate enter", "Entered onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimmung);

        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        this.InitializeControlls();
        this.SetControlCaptions();

        // Now read the extra key - val
        Intent iin= getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if(extras!=null )
        {
            // read the datetime as this is the unique value in the db for the notification
            String notificationDate =(String) extras.get("NotificationDate");
            Log.e("Oncreate notifi", notificationDate);

            // now we have delete this notification from the db cause it is read
            // we delete it from the database, because now the notification is read and it should not be shown in the notification tab cardview
            removeNofiication(this,notificationDate);
        }

    }

    void removeNofiication(Context context, String notificationDate)
    {
        // get the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        // build the current URL
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + preferences.getString("logedIn","") + "/Notifications/" );
        ref.child(context.getString( R.string.stimmungsabgabe)).child(notificationDate).removeValue();

    }
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onStart();
    }*/
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
                Toast.makeText(ActivityMain.activityMain,getString( R.string.Erfolgreich_gespeichert),Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private StimmungsAngabe getData()
    {
        StimmungsAngabe stimmungsAngabe = new StimmungsAngabe();
        stimmungsAngabe.Angespannt = lstAngespannt.getIndex();
        stimmungsAngabe.Mitteilsam = lstMittelsam.getIndex();
        stimmungsAngabe.Muede = lstMuede.getIndex();
        stimmungsAngabe.Selbstsicher = lstSelbstsicher.getIndex();
        stimmungsAngabe.Tatkraeftig = lstTatkraeftig.getIndex();
        stimmungsAngabe.Traurig = lstTraurig.getIndex();
        stimmungsAngabe.Wuetend = lstWuetend.getIndex();
        stimmungsAngabe.Zerstreut= lstZerstreut.getIndex();
        stimmungsAngabe.Vor  = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        stimmungsAngabe.Date = sdf.format(new Date());

        return stimmungsAngabe;

    }

    private StimmungAbfrageScore getDataScore(){
        StimmungAbfrageScore stimmungAbfrageScore= new StimmungAbfrageScore();
        stimmungAbfrageScore.AngespanntScore=AngespanntScore();
        stimmungAbfrageScore.TraurigScore=TraurigScore();
        stimmungAbfrageScore.TatkraeftigScore=TatkraeftigScore();
        stimmungAbfrageScore.ZerstreutScore=ZerstreutScore();
        stimmungAbfrageScore.WuetendScore=WuetendScore();
        stimmungAbfrageScore.MuedeScore=MuedeScore();
        stimmungAbfrageScore.SelbstsicherScore=SelbstsicherScore();
        stimmungAbfrageScore.MitteilsamScore=MitteilsamScore();
        stimmungAbfrageScore.StimmungsBarometerScore=StimmungsbarometerScore();
        stimmungAbfrageScore.EnergieIndexScore=EnergieIndex();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        stimmungAbfrageScore.Date = sdf.format(new Date());
        return stimmungAbfrageScore;
    }

    private boolean SaveData()
    {
        StimmungAbfrageScore stimmungAbfrageScore = getDataScore();
        ActivityMain.mainUser.SaveStimmungScore(stimmungAbfrageScore, new Date());
        StimmungsAngabe stimmungAbfrage  = getData();
        ActivityMain.mainUser.SaveStimmung(stimmungAbfrage, new Date());


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
        ((TextView)findViewById(R.id.txtMainQuestion)).setText(getString( R.string.bitte_geben_sie_moment_fuhlen));
        ((TextView)findViewById(R.id.txtAngespannt)).setText(getString( R.string.angespannt));
        ((TextView)findViewById(R.id.txtTraurig)).setText(getString( R.string.traurig));


    }

    public int stimmungsbarometerPositiv(int index) {
        switch (index) {
            case 4:
                return 4;
            case 3:
                return 3;
            case 2:
                return 2;
            case 1:
                return 1;
            case 0:
                return 0;
            default:
                return 0;
        }
    }

    public int stimmungsbarometerNegativ(int index){
        switch(index){
            case 4: return 0;
            case 3: return 1;
            case 2: return 2;
            case 1: return 3;
            case 0: return 4;
            default: return 0;
        }
    }

    public int AngespanntScore(){
        angespanntwert=stimmungsbarometerNegativ(lstAngespannt.getIndex());
        return angespanntwert;
    }

    public int TraurigScore(){
        traurigwert=stimmungsbarometerNegativ(lstTraurig.getIndex());
        return traurigwert;
    }

    public int TatkraeftigScore(){
        tatkraeftigwert=stimmungsbarometerPositiv(lstTatkraeftig.getIndex());
        return tatkraeftigwert;
    }

    public int ZerstreutScore(){
        zerstreutwert=stimmungsbarometerNegativ(lstZerstreut.getIndex());
        return zerstreutwert;
    }

    public int WuetendScore(){
        wuetendwert=stimmungsbarometerNegativ(lstWuetend.getIndex());
        return wuetendwert;
    }

    public int MuedeScore(){
        muedewert=stimmungsbarometerNegativ(lstMuede.getIndex());
        return muedewert;
    }

    public int SelbstsicherScore(){
        selbstsicherwert=stimmungsbarometerPositiv(lstSelbstsicher.getIndex());
        return selbstsicherwert;
    }

    public int MitteilsamScore(){
        mitteilsamwert=stimmungsbarometerPositiv(lstMittelsam.getIndex());
        return mitteilsamwert;
    }

    //Stimmungsbarometer ((Tatkräftig+Selbstsicher+Mitteilsam)-(Angespannt+Traurig+Zerstreut+Wütend+Müde) --> Höhere Werte stehen für ausgeglichenere Stimmung
    public int StimmungsbarometerScore(){
    StimmungScore=(TatkraeftigScore()+SelbstsicherScore()+MitteilsamScore())-(AngespanntScore()+TraurigScore()+ZerstreutScore()+WuetendScore()+MuedeScore());
        return StimmungScore;
    }

    // Ratio aus Tatkräftig und Müde (Höhere Scores indizieren größeres Level von Erholung
    public float EnergieIndex(){
        if (TatkraeftigScore()>0 && MuedeScore()>0 ){
        EnergieIndexScore= ((float)TatkraeftigScore()) / ((float)MuedeScore());
        }
        return EnergieIndexScore;
    }

    //TODO Differenzwert berechnen -> Erst in Berichte/Diagramme: (StimmungsbarometerScore NACH Training - StimmungsbarometerScore VOR Training)

}





