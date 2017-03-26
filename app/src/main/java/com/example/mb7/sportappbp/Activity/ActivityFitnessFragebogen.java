package com.example.mb7.sportappbp.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.FitnessFragebogenViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
import com.example.mb7.sportappbp.MotivationMethods.MotivationMethod;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.FragebogenListview;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Felix on 01.03.2017.
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


    int kraftscore;
    int ausdauerscore;
    int beweglichkeitsscore;
    int koordinationsscore;
    int gesamtscore;

    private Firebase mRootRef;
    ActivityFitnessFragebogen activityFitnessFragebogen = this;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitnessquestions);

        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        this.InitializeControlls();
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
        inflater.inflate(R.menu.menu_info_save, menu);

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
                speicheralert();
                return true;
            case R.id.icon_info:
                infoalert();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void infoalert() {
        AlertDialog.Builder infobuilder=new AlertDialog.Builder(this);
                infobuilder.setTitle(getString( R.string.Information));
                infobuilder.setMessage(getString( R.string.Fitnessfragebogeninfo));
                infobuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                infobuilder.show();
    }

    private void speicheralert() {
        AlertDialog.Builder speicherbuilder=new AlertDialog.Builder(this);
        speicherbuilder.setTitle(getString( R.string.Ergebnis));
        speicherbuilder.setMessage(
                getString( R.string.Kraftscore) +" " + kraftscoring() +" " + getString( R.string.von_Punkten)+"\n" +
                        getString( R.string.Ausdauerscore) +" " + ausdauerscoring() +" "  + getString( R.string.von_Punkten)+ "\n" +
                        getString( R.string.Bewglichkeitsscore) +" " + bewglichkeitsscoring()+" " + getString( R.string.von_Punkten) + "\n" +
                        getString( R.string.Koordinationsscore) +" " + koordinationscoring()+" "  + getString( R.string.von_Punkten)+ "\n" +
                        getString( R.string.Gesamtscore)+" " + scoringwert()+" " + getString( R.string.von_Gesamtpunkten) );
        speicherbuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveData();
                finish();
                Toast ausgabe= Toast.makeText(activityFitnessFragebogen,
                        getString( R.string.Erfolgreich_gespeichert),Toast.LENGTH_LONG);
                ausgabe.show();
            }

        });
        speicherbuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        speicherbuilder.show();
    }

    private FitnessFragebogen getData(){
        FitnessFragebogen fitnessfragebogen=new FitnessFragebogen();

        fitnessfragebogen.scorekraft=kraftscoring();
        fitnessfragebogen.scoreausdauer=ausdauerscoring();
        fitnessfragebogen.scorebeweglichkeit=bewglichkeitsscoring();
        fitnessfragebogen.scorekoordination=koordinationscoring();
        fitnessfragebogen.scoregesamt=scoringwert();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        fitnessfragebogen.Date = sdf.format(new Date());

        return fitnessfragebogen;
    }

    private boolean SaveData() {
        FitnessFragebogen fitnessfragebogen = getData();
        ActivityMain.getMainUser(this).SaveFitnessFragebogen(fitnessfragebogen,new Date());

        return true;
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
}


private int scoringrechnung(int index){
    switch(index){
        case 4: return 5;
        case 3: return 4;
        case 2: return 3;
        case 1: return 2;
        case 0: return 1;
        default: return 0;
    }
}

public int kraftscoring(){
    kraftscore=scoringrechnung(lststuhlaufstehen.getIndex())+scoringrechnung(lsteinkaufskorb.getIndex())+scoringrechnung(lstkistetragen.getIndex())+scoringrechnung(lstsitup.getIndex())+scoringrechnung(lstkofferheben.getIndex())+scoringrechnung(lstkoffertragen.getIndex())+scoringrechnung(lsthantelstemmen.getIndex());
    return kraftscore;
}

private int ausdauerscoring(){
    ausdauerscore=scoringrechnung(lstflottgehen.getIndex())+scoringrechnung(lsttreppengehen.getIndex())+scoringrechnung(lst2kmgehen.getIndex())+scoringrechnung(lst1kmjoggen.getIndex())+scoringrechnung(lst30minjoggen.getIndex())+scoringrechnung(lst60minjoggen.getIndex())+scoringrechnung(lstmarathon.getIndex());
    return ausdauerscore;
    }

    private int bewglichkeitsscoring(){
        beweglichkeitsscore=scoringrechnung(lstanziehen.getIndex())+scoringrechnung(lstsitzendboden.getIndex())+scoringrechnung(lstschuhebinden.getIndex())+scoringrechnung(lstrueckenberuehren.getIndex())+scoringrechnung(lststehendboden.getIndex())+scoringrechnung(lstkopfknie.getIndex())+scoringrechnung(lstbruecke.getIndex());
        return beweglichkeitsscore;
    }

    private int koordinationscoring(){
        koordinationsscore=scoringrechnung(lsttrepperunter.getIndex())+scoringrechnung(lsteinbeinstand.getIndex())+scoringrechnung(lstpurzelbaum.getIndex())+scoringrechnung(lstballprellen.getIndex())+scoringrechnung(lstzaunsprung.getIndex())+scoringrechnung(lstkurveohnehand.getIndex())+scoringrechnung(lstradschlagen.getIndex());
        return koordinationsscore;
    }

    private int scoringwert(){
               gesamtscore=kraftscoring()+ausdauerscoring()+bewglichkeitsscoring()+koordinationscoring();

        return gesamtscore;
    }

}

