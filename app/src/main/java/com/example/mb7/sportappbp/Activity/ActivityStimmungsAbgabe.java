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
                Toast.makeText(ActivityMain.activityMain,getString( R.string.erfolgreichgespeichert),Toast.LENGTH_SHORT);

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

    private boolean SaveData()
    {
        StimmungsAngabe stimmungsAngabe = getData();
        ActivityMain.mainUser.SaveStimmung(stimmungsAngabe, new Date());

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
}
