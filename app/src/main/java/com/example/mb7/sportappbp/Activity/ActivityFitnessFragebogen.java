package com.example.mb7.sportappbp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.FitnessFragebogenViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.FitnessFragebogen;
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
    private ActivityFitnessFragebogen activityFitnessFragebogen = this;
    FitnessFragebogen fitnessFragebogen=null;

    boolean INSERT=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitnessquestions);



        // Now read the extra key - val
        Intent iin= getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if(extras!=null )
        {
            // read the datetime as this is the unique value in the db for the notification
            String notificationDate =(String) extras.get("NotificationDate");
            fitnessFragebogen=(FitnessFragebogen) iin.getSerializableExtra(getString(R.string.fitnessfragebogen));
            if (fitnessFragebogen!=null)
            {
                INSERT=false;
            }

            // now we have delete this notification from the db cause it is read if there has been any
            // we could enter this activity without clicking any notification!
            // we delete it from the database, because now the notification is read and it should not be shown in the notification tab cardview
            if (notificationDate!=null)
                removeNofiication(this, notificationDate);
        }

        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        this.InitializeControlls();

        //super.onStart();
    }

    @Override
    protected void onResume(){super.onResume();}


    void removeNofiication(Context context, String notificationDate)
    {
        // get the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        // build the current URL
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + preferences.getString("logedIn","") + "/Notifications/" );
        ref.child(context.getString( R.string.fitnessfragebogen)).child(notificationDate).removeValue();

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
                return super.onOptionsItemSelected(item);
            case R.id.icon_info:
                infoalert();
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                finish();
                return true;
            default:
                finish();
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
                        getString( R.string.Erfolgreich_gespeichert),Toast.LENGTH_SHORT);
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

        //Indexwerte der Listview Elemente
        fitnessfragebogen.vom_Stuhl_aufstehen =lststuhlaufstehen.getIndexFitness();
        fitnessfragebogen.Einkaufskorb_tragen =lsteinkaufskorb.getIndexFitness();
        fitnessfragebogen.Kiste_tragen =lstkistetragen.getIndexFitness();
        fitnessfragebogen.Situp =lstsitup.getIndexFitness();
        fitnessfragebogen.Koffer_hoch_heben =lstkofferheben.getIndexFitness();
        fitnessfragebogen.Koffer_tragen =lstkoffertragen.getIndexFitness();
        fitnessfragebogen.Hantel_stemmen =lsthantelstemmen.getIndexFitness();

        fitnessfragebogen.flott_gehen =lstflottgehen.getIndexFitness();
        fitnessfragebogen.Treppen_gehen =lsttreppengehen.getIndexFitness();
        fitnessfragebogen.Zwei_km_gehen =lst2kmgehen.getIndexFitness();
        fitnessfragebogen.Ein_km_joggen =lst1kmjoggen.getIndexFitness();
        fitnessfragebogen.Dreißig_min_joggen =lst30minjoggen.getIndexFitness();
        fitnessfragebogen.Sechzig_min_joggen =lst60minjoggen.getIndexFitness();
        fitnessfragebogen.Marathon =lstmarathon.getIndexFitness();

        fitnessfragebogen.Socken_anziehen =lstanziehen.getIndexFitness();
        fitnessfragebogen.Boden_im_Sitzen_beruehren =lstsitzendboden.getIndexFitness();
        fitnessfragebogen.Schuhe_binden =lstschuhebinden.getIndexFitness();
        fitnessfragebogen.Ruecken_beruehren =lstrueckenberuehren.getIndexFitness();
        fitnessfragebogen.Im_Stehen_Boden_beruehren =lststehendboden.getIndexFitness();
        fitnessfragebogen.Mit_Kopf_das_Knie_beruehren =lstkopfknie.getIndexFitness();
        fitnessfragebogen.Bruecke =lstbruecke.getIndexFitness();

        fitnessfragebogen.Treppe_runter_gehen =lsttrepperunter.getIndexFitness();
        fitnessfragebogen.Einbeinstand =lsteinbeinstand.getIndexFitness();
        fitnessfragebogen.Purzelbaum =lstpurzelbaum.getIndexFitness();
        fitnessfragebogen.Ball_prellen =lstballprellen.getIndexFitness();
        fitnessfragebogen.Zaunsprung =lstzaunsprung.getIndexFitness();
        fitnessfragebogen.Kurve_fahren_ohne_Hand =lstkurveohnehand.getIndexFitness();
        fitnessfragebogen.Rad_schlagen =lstradschlagen.getIndexFitness();

        //Berechnete Scoringwerte
        fitnessfragebogen.Score_Kraft =kraftscoring();
        fitnessfragebogen.Score_Ausdauer =ausdauerscoring();
        fitnessfragebogen.Score_Beweglichkeit =bewglichkeitsscoring();
        fitnessfragebogen.Score_Koordination =koordinationscoring();
        fitnessfragebogen.Score_Gesamt =scoringwert();

        if(INSERT){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        fitnessfragebogen.Date = sdf.format(new Date());}
        else
            fitnessfragebogen.Date=this.fitnessFragebogen.FirebaseDate;

        return fitnessfragebogen;
    }

    private boolean SaveData() {
        FitnessFragebogen fitnessfragebogen = getData();
        if (INSERT)
        ActivityMain.getMainUser(this).InsertFitnessFragebogen(fitnessfragebogen);
        else
           ActivityMain.getMainUser(this).UpdateFitnessFragebogen(fitnessfragebogen);


        return true;
    }



    /**
     * Initialisiert die Listviews -> Über Funktionen aus Fragebogenlistview
     */
    private void InitializeControlls(){

        // set the listivew

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


        // Adapter setzen
        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.auf_einem_Stuhl_sitzend_ohne_Hilfe_aufstehen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.vom_Stuhl_aufstehen !=null? fitnessFragebogen.vom_Stuhl_aufstehen :-1);
        lststuhlaufstehen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.einen_schweren_Einkaufskorb_ueber_mehrere_Etagen_tragen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Einkaufskorb_tragen !=null? fitnessFragebogen.Einkaufskorb_tragen :-1);
        lsteinkaufskorb.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.eine_volle_Bierkiste_in_den_Keller_tragen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Kiste_tragen !=null? fitnessFragebogen.Kiste_tragen :-1);
        lstkistetragen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.aus_der_Rueckenlage_ohne_Hilfe_der_Arme_den_Oberkoerper_aufrichten));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Situp !=null? fitnessFragebogen.Situp :-1);
        lstsitup.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.einen_schweren_Koffer_ueber_Kopfhoehe_heben));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Koffer_hoch_heben !=null? fitnessFragebogen.Koffer_hoch_heben :-1);
        lstkofferheben.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.zwei_schwere_Koffer_ueber_mehrere_Etagen_tragen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Koffer_tragen !=null? fitnessFragebogen.Koffer_tragen :-1);
        lstkoffertragen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.eine_Hantel_mit_mehr_als_Ihrem_Koerpergewicht_hochstemmen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Hantel_stemmen !=null? fitnessFragebogen.Hantel_stemmen :-1);
        lsthantelstemmen.setAdapter(adapter);


        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.um_mehrere_Blocks_flott_gehen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.flott_gehen !=null? fitnessFragebogen.flott_gehen :-1);
        lstflottgehen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.mehrere_Treppen_hochgehen_ohne_sich_auszuruhen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Treppen_gehen !=null? fitnessFragebogen.Treppen_gehen :-1);
        lsttreppengehen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.zwei_Kilometer_schnell_gehen_ohne_sich_auszuruhen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Zwei_km_gehen !=null? fitnessFragebogen.Zwei_km_gehen :-1);
        lst2kmgehen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.einen_Kilometer_ohne_Pause_joggen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Ein_km_joggen !=null? fitnessFragebogen.Ein_km_joggen :-1);
        lst1kmjoggen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.Minuten_ohne_Pause_joggen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Dreißig_min_joggen !=null? fitnessFragebogen.Dreißig_min_joggen :-1);
        lst30minjoggen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.eine_Stunde_ohne_Pause_joggen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Sechzig_min_joggen !=null? fitnessFragebogen.Sechzig_min_joggen :-1);
        lst60minjoggen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.einen_Marathon_laufen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Marathon !=null? fitnessFragebogen.Marathon :-1);
        lstmarathon.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.einen_engen_Pulli_und_Socken_alleine_aus_und_anziehen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Socken_anziehen !=null? fitnessFragebogen.Socken_anziehen :-1);
        lstanziehen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.auf_einem_Stuhl_sitzend_mit_den_Haenden_den_Boden_erreichen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Boden_im_Sitzen_beruehren !=null? fitnessFragebogen.Boden_im_Sitzen_beruehren :-1);
        lstsitzendboden.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.im_Stehen_Schuhe_binden));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Schuhe_binden !=null? fitnessFragebogen.Schuhe_binden :-1);
        lstschuhebinden.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.mit_der_Hand_von_unten_auf_dem_Ruecken_ein_Schulterblatt_beruehren));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Ruecken_beruehren !=null? fitnessFragebogen.Ruecken_beruehren :-1);
        lstrueckenberuehren.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.aus_dem_Stand_mit_den_Haenden_den_Boden_erreichen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Im_Stehen_Boden_beruehren !=null? fitnessFragebogen.Im_Stehen_Boden_beruehren :-1);
        lststehendboden.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.im_Stehen_mit_dem_Kopf_die_gestreckten_Knie_beruehren));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Mit_Kopf_das_Knie_beruehren !=null? fitnessFragebogen.Mit_Kopf_das_Knie_beruehren :-1);
        lstkopfknie.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.rueckwaerts_bis_in_die_Bruecke_abbeugen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Bruecke !=null? fitnessFragebogen.Bruecke :-1);
        lstbruecke.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.eine_Treppe_hinab_gehen_ohne_sich_festzuhalten));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Treppe_runter_gehen !=null? fitnessFragebogen.Treppe_runter_gehen :-1);
        lsttrepperunter.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.auf_einem_Bein_stehen_ohne_sich_festzuhalten));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Einbeinstand !=null? fitnessFragebogen.Einbeinstand :-1);
        lsteinbeinstand.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.einen_Purzelbaum));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Purzelbaum !=null? fitnessFragebogen.Purzelbaum :-1);
        lstpurzelbaum.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.im_schnellen_Gehen_einen_Ball_prellen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Ball_prellen !=null? fitnessFragebogen.Ball_prellen :-1);
        lstballprellen.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.mit_Abstuetzen_ueber_einen_ein_Meter_hohen_Zaun_springen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Zaunsprung !=null? fitnessFragebogen.Zaunsprung :-1);
        lstzaunsprung.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.freihaendig_mit_dem_Fahrrad_um_eine_Kurve_fahren));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Kurve_fahren_ohne_Hand !=null? fitnessFragebogen.Kurve_fahren_ohne_Hand :-1);
        lstkurveohnehand.setAdapter(adapter);

        adapter = new FitnessFragebogenViewAdapter(this);
        adapter.setFitnessFragebogen(fitnessFragebogen, getString(R.string.ein_Rad_schlagen));
        adapter.setSelectedIndex(fitnessFragebogen!=null && fitnessFragebogen.Rad_schlagen !=null? fitnessFragebogen.Rad_schlagen :-1);
        lstradschlagen.setAdapter(adapter);

        /**
         * Initialsieren
         */

        lststuhlaufstehen.InitializeFitness();
                lsteinkaufskorb.InitializeFitness();
        lstkistetragen.InitializeFitness();
                lstsitup.InitializeFitness();
        lstkofferheben.InitializeFitness();
                lstkoffertragen.InitializeFitness();
        lsthantelstemmen.InitializeFitness();

                lstflottgehen.InitializeFitness();
        lsttreppengehen.InitializeFitness();
                lst2kmgehen.InitializeFitness();
        lst1kmjoggen.InitializeFitness();
                lst30minjoggen.InitializeFitness();
        lst60minjoggen.InitializeFitness();
                lstmarathon.InitializeFitness();

        lstanziehen.InitializeFitness();
                lstsitzendboden.InitializeFitness();
        lstschuhebinden.InitializeFitness();
                lstrueckenberuehren.InitializeFitness();
        lststehendboden.InitializeFitness();
                lstkopfknie.InitializeFitness();
        lstbruecke.InitializeFitness();

                lsttrepperunter.InitializeFitness();
        lsteinbeinstand.InitializeFitness();
                lstpurzelbaum.InitializeFitness();
        lstballprellen.InitializeFitness();
                lstzaunsprung.InitializeFitness();
        lstkurveohnehand.InitializeFitness();
                lstradschlagen.InitializeFitness();
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
    kraftscore=scoringrechnung(lststuhlaufstehen.getIndexFitness())+scoringrechnung(lsteinkaufskorb.getIndexFitness())+scoringrechnung(lstkistetragen.getIndexFitness())+scoringrechnung(lstsitup.getIndexFitness())+scoringrechnung(lstkofferheben.getIndexFitness())+scoringrechnung(lstkoffertragen.getIndexFitness())+scoringrechnung(lsthantelstemmen.getIndexFitness());
    return kraftscore;
}

private int ausdauerscoring(){
    ausdauerscore=scoringrechnung(lstflottgehen.getIndexFitness())+scoringrechnung(lsttreppengehen.getIndexFitness())+scoringrechnung(lst2kmgehen.getIndexFitness())+scoringrechnung(lst1kmjoggen.getIndexFitness())+scoringrechnung(lst30minjoggen.getIndexFitness())+scoringrechnung(lst60minjoggen.getIndexFitness())+scoringrechnung(lstmarathon.getIndexFitness());
    return ausdauerscore;
    }

    private int bewglichkeitsscoring(){
        beweglichkeitsscore=scoringrechnung(lstanziehen.getIndexFitness())+scoringrechnung(lstsitzendboden.getIndexFitness())+scoringrechnung(lstschuhebinden.getIndexFitness())+scoringrechnung(lstrueckenberuehren.getIndexFitness())+scoringrechnung(lststehendboden.getIndexFitness())+scoringrechnung(lstkopfknie.getIndexFitness())+scoringrechnung(lstbruecke.getIndexFitness());
        return beweglichkeitsscore;
    }

    private int koordinationscoring(){
        koordinationsscore=scoringrechnung(lsttrepperunter.getIndexFitness())+scoringrechnung(lsteinbeinstand.getIndexFitness())+scoringrechnung(lstpurzelbaum.getIndexFitness())+scoringrechnung(lstballprellen.getIndexFitness())+scoringrechnung(lstzaunsprung.getIndexFitness())+scoringrechnung(lstkurveohnehand.getIndexFitness())+scoringrechnung(lstradschlagen.getIndexFitness());
        return koordinationsscore;
    }

    private int scoringwert(){
               gesamtscore=kraftscoring()+ausdauerscoring()+bewglichkeitsscoring()+koordinationscoring();

        return gesamtscore;
    }

}

