package com.example.mb7.sportappbp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Activity.ActivityFitnessFragebogen;
import com.example.mb7.sportappbp.R;

/**
 * Created by Felix on 21.03.2017.
 */

public class ActivityFragebogenScore extends AppCompatActivity{




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragebogenscores);
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

    /**
     * Spericherbutton
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_save:
                finish();
                Toast.makeText(this,
                        "Erfolgreich gespeichert" + "\n"
                        ,Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void SetControlCaptions(){
        ((TextView)findViewById(R.id.txtbsascore)).setText("");
        ((TextView)findViewById(R.id.txtfitnesscore)).setText("");

    }


    /**
     * Werte aus Datenbank lesen, speichern und in Textviews ausgeben.
     */
    public void getFitnessScores(){

    }

    public void getBsaScores(){



    }

}
