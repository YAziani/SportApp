package com.example.mb7.sportappbp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;



import com.example.mb7.sportappbp.Adapters.FitnessFragebogenViewAdapter;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.FragebogenListview;

/**
 * Created by Felix on 03.03.2017.
 */

public class ActivityFitnessFragebogen extends AppCompatActivity {
    private FitnessFragebogenViewAdapter adapter;

    FragebogenListview lststuhlaufstehen;
    FragebogenListview lsteinkaufskorb;
    FragebogenListview lstkistetragen;
    FragebogenListview lstsitup;
    FragebogenListview lstkofferheben;
    FragebogenListview lstkoffertragen;
    FragebogenListview lsthantelstemmen;

    FragebogenListview lstflottgehen;
    FragebogenListview lsttreppengehen;
    FragebogenListview lst2kmgehen;
    FragebogenListview lst1kmjoggen;
    FragebogenListview lst30minjoggen;
    FragebogenListview lst60minjoggen;
    FragebogenListview lstmarathon;

    FragebogenListview lstanziehen;
    FragebogenListview lstsitzendboden;
    FragebogenListview lstschuhebinden;
    FragebogenListview lstrueckenberuehren;
    FragebogenListview lststehendboden;
    FragebogenListview lstkopfknie;
    FragebogenListview lstbruecke;

    FragebogenListview lsttrepperunter;
    FragebogenListview lsteinbeinstand;
    FragebogenListview lstpurzelbaum;
    FragebogenListview lstballprellen;
    FragebogenListview lstzaunsprung;
    FragebogenListview lstkurveohnehand;
    FragebogenListview lstradschlagen;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitnessquestions);
        this.InitializeControlls();
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
                //SaveData();
                finish();
                Toast.makeText(ActivityMain.activityMain,
                        "Erfolgreich gespeichert" ,Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialisiert die Listviews -> Über Funktionen aus Fragebogenlistview
     */
    private void InitializeControlls(){

        // set the listivew
        // first create the adapters
        adapter = new FitnessFragebogenViewAdapter(this);

        lststuhlaufstehen = (FragebogenListview)findViewById( R.id.lvstuhlaufstehen);
        lsteinkaufskorb = (FragebogenListview)findViewById( R.id.lveinkaufskorb);
        lstkistetragen = (FragebogenListview)findViewById( R.id.lvkistetragen);
        lstsitup=(FragebogenListview)findViewById( R.id.lvsitup);
        lstkofferheben=(FragebogenListview)findViewById( R.id.lvkofferheben);
        lstkoffertragen=(FragebogenListview)findViewById( R.id.lvkoffertragen);
        lsthantelstemmen=(FragebogenListview)findViewById( R.id.lvhantelstemmen);

        lstflottgehen=(FragebogenListview)findViewById( R.id.lvflottgehen);
        lsttreppengehen=(FragebogenListview)findViewById( R.id.lvtreppengehen);
        lst2kmgehen=(FragebogenListview)findViewById( R.id.lv2kmgehen);
        lst1kmjoggen=(FragebogenListview)findViewById( R.id.lv1kmjoggen);
        lst30minjoggen=(FragebogenListview)findViewById( R.id.lv30minjoggen);
        lst60minjoggen=(FragebogenListview)findViewById( R.id.lv60minjoggen);
        lstmarathon=(FragebogenListview)findViewById( R.id.lvmarathon);

        lstanziehen=(FragebogenListview)findViewById( R.id.lvanziehen);
        lstsitzendboden=(FragebogenListview)findViewById( R.id.lvsitzendboden);
        lstschuhebinden=(FragebogenListview)findViewById( R.id.lvschuhebinden);
        lstrueckenberuehren=(FragebogenListview)findViewById( R.id.lvrueckenberuehren);
        lststehendboden=(FragebogenListview)findViewById( R.id.lvstehendboden);
        lstkopfknie=(FragebogenListview)findViewById( R.id.lvkopfknie);
        lstbruecke=(FragebogenListview)findViewById( R.id.lvbruecke);

        lsttrepperunter=(FragebogenListview)findViewById( R.id.lvtrepperunter);
        lsteinbeinstand=(FragebogenListview)findViewById( R.id.lveinbeinstand);
        lstpurzelbaum=(FragebogenListview)findViewById( R.id.lvpurzelbaum);
        lstballprellen=(FragebogenListview)findViewById( R.id.lvballprellen);
        lstzaunsprung=(FragebogenListview)findViewById( R.id.lvzaunsprung);
        lstkurveohnehand=(FragebogenListview)findViewById( R.id.lvkurveohnehand);
        lstradschlagen=(FragebogenListview)findViewById( R.id.lvradschlagen);

        /**
         * Adapter setzen
         */
        lststuhlaufstehen.setAdapter(adapter);
        lsteinkaufskorb.setAdapter(adapter);
        lstkistetragen.setAdapter(adapter);
        lstsitup.setAdapter(adapter);
        lstkofferheben.setAdapter(adapter);
        lstkoffertragen.setAdapter(adapter);
        lsthantelstemmen.setAdapter(adapter);

        lstflottgehen.setAdapter(adapter);
        lsttreppengehen.setAdapter(adapter);
        lst2kmgehen.setAdapter(adapter);
        lst1kmjoggen.setAdapter(adapter);
        lst30minjoggen.setAdapter(adapter);
        lst60minjoggen.setAdapter(adapter);
        lstmarathon.setAdapter(adapter);

        lstanziehen.setAdapter(adapter);
        lstsitzendboden.setAdapter(adapter);
        lstschuhebinden.setAdapter(adapter);
        lstrueckenberuehren.setAdapter(adapter);
        lststehendboden.setAdapter(adapter);
        lstkopfknie.setAdapter(adapter);
        lstbruecke.setAdapter(adapter);

        lsttrepperunter.setAdapter(adapter);
        lsteinbeinstand.setAdapter(adapter);
        lstpurzelbaum.setAdapter(adapter);
        lstballprellen.setAdapter(adapter);
        lstzaunsprung.setAdapter(adapter);
        lstkurveohnehand.setAdapter(adapter);
        lstradschlagen.setAdapter(adapter);

        /**
         * Initialsieren
         */

        lststuhlaufstehen.Initialize();
                lsteinkaufskorb.Initialize();
        lstkistetragen.Initialize();
                lstsitup.Initialize();
        lstkofferheben.Initialize();
                lstkoffertragen.Initialize();
        lsthantelstemmen.Initialize();

                lstflottgehen.Initialize();
        lsttreppengehen.Initialize();
                lst2kmgehen.Initialize();
        lst1kmjoggen.Initialize();
                lst30minjoggen.Initialize();
        lst60minjoggen.Initialize();
                lstmarathon.Initialize();

        lstanziehen.Initialize();
                lstsitzendboden.Initialize();
        lstschuhebinden.Initialize();
                lstrueckenberuehren.Initialize();
        lststehendboden.Initialize();
                lstkopfknie.Initialize();
        lstbruecke.Initialize();

                lsttrepperunter.Initialize();
        lsteinbeinstand.Initialize();
                lstpurzelbaum.Initialize();
        lstballprellen.Initialize();
                lstzaunsprung.Initialize();
        lstkurveohnehand.Initialize();
                lstradschlagen.Initialize();


}}

