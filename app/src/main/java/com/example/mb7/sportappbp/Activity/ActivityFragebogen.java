package com.example.mb7.sportappbp.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.Adapters.FragebogenViewAdapter;
import com.example.mb7.sportappbp.Adapters.FragebogenViewAdapter2;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.FragebogenListview;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Felix on 19.01.2017.
 */

public class ActivityFragebogen extends AppCompatActivity {
    private FragebogenViewAdapter adapter;
    private FragebogenViewAdapter2 adapter2;

    //Integer Werte für Scoring des Fragebogens
    long scoringbewegungwert;
    long scoringsportwert;
    long scoringgesamtwert;
    int bewegungsaktivitätberuf;

    //Listviews
    FragebogenListview lstberufstätig;
    FragebogenListview lstsitzendetätigkeiten;
    FragebogenListview lstmäßigebewegung;
    FragebogenListview lstintensivebewegung;
    FragebogenListview lstsportlichaktiv;

    //Zeitaum für Fragen ab Block 4
    static long wochenzeitraum;


    int itemsitzendetätigkeit;
    int itemmäßigebewegung;
    int itemintensivebewegung;

    private Firebase mRootRef;
    private ActivityFragebogen activityFragebogen = this;

    boolean INSERT=true;
    Fragebogen fragebogen=null;


    public static void getWochenanzahlFromDb() {


            //URL url = new URL(DAL_Utilities.DatabaseURL + "Administration/bsa/questionaryPeriodweeks");
            //Firebase root = new Firebase(url.toString());
            Firebase root= new Firebase("https://sportapp-cbd6b.firebaseio.com/Administration/bsa/questionaryPeriodweeks");
            try{root.addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                       if (dataSnapshot.getValue()!=null)
                                                        //wochenzeitraum=(long)dataSnapshot.getValue();
                                                        ActivityFragebogen.wochenzeitraum=convertToLong(dataSnapshot.getValue());

                                                        else
                                                            ActivityFragebogen.wochenzeitraum=4;

                                                    }


                                                    @Override
                                                    public void onCancelled(FirebaseError firebaseError) {
                                                        Log.d("Fragebogen",firebaseError.getMessage());
                                                    }
                                                }
            );

        } catch (Exception e) {
            e.printStackTrace();

        }

    }



    public static Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWochenanzahlFromDb();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsaquestions);
        this.SetControlCaptions();


        // Now read the extra key - val
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {
            // read the datetime as this is the unique value in the db for the notification
            String notificationDate = (String) extras.get("NotificationDate");
            fragebogen=(Fragebogen) iin.getSerializableExtra(getString(R.string.aktivitaetsfragebogen));

            if (fragebogen!=null){
                INSERT=false;
            }
           // Log.e("Oncreate notifi", notificationDate);

            // now we have delete this notification from the db cause it is read
            // we delete it from the database, because now the notification is read and it should not be shown in the notification tab cardview
            if (notificationDate !=null)
            removeNofiication(this, notificationDate);
        }

        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        //this.getWochenanzahlFromDb();


        this.InitializeControlls();
        //super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    void removeNofiication(Context context, String notificationDate) {
        // get the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        // build the current URL
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + preferences.getString("logedIn", "") + "/Notifications/");
        ref.child(context.getString(R.string.aktivitaetsfragebogen)).child(notificationDate).removeValue();

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
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //check which icon was hidden in the toolbar
        switch (item.getItemId()) {
            case R.id.icon_save:
                speicheralert();

                return super.onOptionsItemSelected(item);

            case android.R.id.home:
                finish();
                return true;
            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }

    private void speicheralert() {
        AlertDialog.Builder speicherbuilder = new AlertDialog.Builder(this);
        speicherbuilder.setTitle(getString(R.string.Ergebnis));
        speicherbuilder.setMessage(
                getString(R.string.Bewegungsscore) + " " + scoringbewegung() + "\n" +
                        getString(R.string.Sportscore) + " " + scoringsport() + "\n" +
                        getString(R.string.Gesamtscore) + " " + scoringgesamt());
        speicherbuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveData();
                finish();
                Toast ausgabe= Toast.makeText(activityFragebogen,
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

    private Fragebogen getData() {
        Fragebogen fragebogen = new Fragebogen();

        //Indexwerte der Listview Elemente
        fragebogen.berufstätig = lstberufstätig.getIndexBSA1();
        fragebogen.sitzendetätigkeiten = lstsitzendetätigkeiten.getIndexBSA2();
        fragebogen.mäßigebewegung = lstmäßigebewegung.getIndexBSA2();
        fragebogen.intensivebewegung = lstintensivebewegung.getIndexBSA2();
        fragebogen.sportlichaktiv = lstsportlichaktiv.getIndexBSA1();

        //Integerwerte des Scorings
        fragebogen.bewegungscoring = scoringbewegung();
        fragebogen.sportscoring = scoringsport();
        fragebogen.gesamtscoring = scoringgesamt();

        //Integerwerte mit Anzahl der Minuten pro Woche
        fragebogen.zufußzurarbeit = zufußzurarbeit();
        fragebogen.zufußeinkaufen = zufußeinkaufen();
        fragebogen.radzurarbeit = radzurarbeit();
        fragebogen.radfahren = radfahren();
        fragebogen.spazieren = spazieren();
        fragebogen.gartenarbeit = gartenarbeit();
        fragebogen.hausarbeit = hausarbeit();
        fragebogen.pflegearbeit = pflegearbeit();
        fragebogen.treppensteigen = treppensteigen();

        //String mit Namen der Aktivität + Integerwert mit Anzahl der Minuten pro Woche
        fragebogen.aktivitätaname = aktivitätaname();
        fragebogen.aktivitäta = aktivitäta();
        fragebogen.aktivitätbname = aktivitätbname();
        fragebogen.aktivitätb = aktivitätb();
        fragebogen.aktivitätcname = aktivitätcname();
        fragebogen.aktivitätc = aktivitätc();

        if (INSERT){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        fragebogen.Date = sdf.format(new Date());}
        else
            fragebogen.Date=this.fragebogen.FirebaseDate;

        return fragebogen;
    }

    private boolean SaveData() {


        Fragebogen fragebogen = getData();
        if (INSERT)
            ActivityMain.getMainUser(this).InsertFragebogen(fragebogen);
        else
        ActivityMain.getMainUser(this).UpdateFragebogen(fragebogen);

        return true;

    }


    /**
     * Initialisiert die Listviews -> Über Funktionen aus Fragebogenlistview
     */
    private void InitializeControlls() {

        LinearLayout beruflayout = (LinearLayout) findViewById(R.id.layoutberuf);
        LinearLayout sportlayout = (LinearLayout) findViewById(R.id.layoutsport);
        // set the listivew
        // first create the adapters


        lstberufstätig = (FragebogenListview) findViewById(R.id.lvberufstätig);
        lstsitzendetätigkeiten = (FragebogenListview) findViewById(R.id.lvsitzendetätigkeiten);
        lstmäßigebewegung = (FragebogenListview) findViewById(R.id.lvmäßigebewegung);
        lstintensivebewegung = (FragebogenListview) findViewById(R.id.lvintensivebewegung);
        lstsportlichaktiv = (FragebogenListview) findViewById(R.id.lvsportlichaktiv);

        adapter = new FragebogenViewAdapter(this);
        adapter.setAntworten(fragebogen,getString(R.string.Sind_Sie_berufstätig_oder_in_Ausbildung));
        adapter.setSelectedIndex(fragebogen!=null && fragebogen.berufstätig!=null? fragebogen.berufstätig:-1);
        lstberufstätig.setAdapter(adapter);

        adapter2 = new FragebogenViewAdapter2(this);
        adapter2.setAntworten(fragebogen,getString(R.string.Umfasst_Ihre_Berufstätigkeit_sitzende_Tätigkeiten));
        adapter2.setSelectedIndex(fragebogen!=null && fragebogen.berufstätig!=null && fragebogen.sitzendetätigkeiten!=null? fragebogen.sitzendetätigkeiten:-1);
        lstsitzendetätigkeiten.setAdapter(adapter2);

        adapter2 = new FragebogenViewAdapter2(this);
        adapter2.setAntworten(fragebogen,getString(R.string.Umfasst_Ihre_Berufstätigkeit_Ausbildung_mäßige_Bewegung));
        adapter2.setSelectedIndex(fragebogen!=null && fragebogen.berufstätig!=null && fragebogen.mäßigebewegung!=null? fragebogen.mäßigebewegung:-1);
        lstmäßigebewegung.setAdapter(adapter2);

        adapter2 = new FragebogenViewAdapter2(this);
        adapter2.setAntworten(fragebogen,getString(R.string.Umfasst_Ihre_Berufstätigkeit_Ausbildung_intensive_Bewegung));
        adapter2.setSelectedIndex(fragebogen!=null && fragebogen.berufstätig!=null && fragebogen.intensivebewegung!=null? fragebogen.intensivebewegung:-1);
        lstintensivebewegung.setAdapter(adapter2);

        adapter = new FragebogenViewAdapter(this);
        adapter.setAntworten(fragebogen,getString(R.string.regelmäßig_Sport_betrieben));
        adapter.setSelectedIndex(fragebogen!=null && fragebogen.sportlichaktiv!=null? fragebogen.sportlichaktiv:-1);
        lstsportlichaktiv.setAdapter(adapter);


        // set the onTouch Event to disable scrolling
        //lstberufstätig.Initialize();
        lstsitzendetätigkeiten.InitializeBSA();
        lstmäßigebewegung.InitializeBSA();
        lstintensivebewegung.InitializeBSA();
        //lstsportlichaktiv.Initialize();

        lstberufstätig.visibility(beruflayout);
        lstsportlichaktiv.visibility(sportlayout);
    }

    public long getwochenzeitraum(){
        getWochenanzahlFromDb();
        return wochenzeitraum;
    }

    /**
     * Inhalt der Textviews mit definiertem Zeitraum.
     */
    private void SetControlCaptions() {
        getwochenzeitraum();
        ((TextView) findViewById(R.id.txtwieoftsport)).setText(getString(R.string.An_wie_vielen_Tagen_und_wie_lange_haben_Sie_die_folgenden_Aktivitäten_in_den_letzten) + " " + getwochenzeitraum() + " " + getString(R.string.Wochen_ausgeübt));
        ((TextView) findViewById(R.id.txtsportlichaktiv)).setText(getString(R.string.Haben_Sie_in_den_letzten) + " " + getwochenzeitraum() + " " + getString(R.string.regelmäßig_Sport_betrieben));
        ((TextView) findViewById(R.id.txtzeitraum)).setText(getString(R.string.Folgende_Fragen_beziehen_sich_auf_den_Zeitraum_von) + " " + getwochenzeitraum() + " " + getString(R.string.Wochen) + "\n");
        ((TextView) findViewById(R.id.txtaktivitätaanzahl)).setText(getString(R.string.Wie_oft_haben_Sie_Aktivität_A_in_den_letzten) + " " + getwochenzeitraum() + " " + getString(R.string.Wochen_ausgeübt));
        ((TextView) findViewById(R.id.txtaktivitätbanzahl)).setText(getString(R.string.Wie_oft_haben_Sie_Aktivität_B_in_den_letzten) + " " + getwochenzeitraum() + " " + getString(R.string.Wochen_ausgeübt));
        ((TextView) findViewById(R.id.txtaktivitätcanzahl)).setText(getString(R.string.Wie_oft_haben_Sie_Aktivität_C_in_den_letzten) + " " + getwochenzeitraum() + " " + getString(R.string.Wochen_ausgeübt));
    }





    /**
     * Macht aus einer Editeingabe einen Integer Wert -> Achtung: In XML Eingabetyp auf Number setzen!!
     * Werfe NumberFormatException, wenn anderes Format und gib 0 zurück
     *
     * @param edittexteingabe
     * @return int
     */
    private int strtoint(EditText edittexteingabe) {
        try {
            return Integer.parseInt(edittexteingabe.getText().toString());
        } catch (NumberFormatException a) {
            return 0;

        }
    }

    /**
     * Scoringwert für Sitzende Tätigkeit: Keine=3 Punkte; Eher wenig=2; Eher mehr=1; viel=0; -> Keine angabe gleich 0;
     *
     * @param index
     * @return int
     */
    private int listscoringsitzend(int index) {
        switch (index) {
            case 3:
                return 0;
            case 2:
                return 1;
            case 1:
                return 2;
            case 0:
                return 3;
            default:
                return 0;
        }
    }

    /**
     * Scoringwert für Bewegung (Mäßige und Intensive Bewegung): Keine=0; Eher wenig=1; Eher mehr=2; Viel=3;
     *
     * @param index
     * @return int
     */
    private int listscoringbewegung(int index) {
        switch (index) {
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

    /**
     * Berechne Werte für die Listviews, indem für bestimmte Index Werte Punkte vergeben werden, Punkte werden in listscoringsitzend + listscoringbewegung vergeben.
     *
     * @return int bewegungsaktivitätberuf
     */
    private int bewegungberuf() {
        if (lstberufstätig.getIndexBSA1() > 0)
            return 0;
        else
        itemsitzendetätigkeit = listscoringsitzend(lstsitzendetätigkeiten.getIndexBSA2());
        itemmäßigebewegung = listscoringbewegung(lstmäßigebewegung.getIndexBSA2());
        itemintensivebewegung = listscoringbewegung(lstintensivebewegung.getIndexBSA2());
        return bewegungsaktivitätberuf = itemsitzendetätigkeit + itemmäßigebewegung + itemintensivebewegung;
    }

    private String aktivitätaname() {
        EditText aktivitätaname = (EditText) findViewById(R.id.edittextaktivitäta);
        return (aktivitätaname.getText().toString());
    }

    private String aktivitätbname() {
        EditText aktivitätbname = (EditText) findViewById(R.id.edittextaktivitätb);
        return (aktivitätbname.getText().toString());
    }

    private String aktivitätcname() {
        EditText aktivitätcname = (EditText) findViewById(R.id.edittextaktivitätc);
        return (aktivitätcname.getText().toString());
    }

    private int zufußzurarbeit() {
        try {
            EditText zufußzurarbeittag = (EditText) findViewById(R.id.edittextzufußzurarbeittag);
            EditText zufußzurarbeitminuten = (EditText) findViewById(R.id.edittextzufußzurarbeitminuten);
            return (strtoint(zufußzurarbeittag) * strtoint(zufußzurarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int zufußeinkaufen() {
        try {
            EditText zufußzumeinkaufentag = (EditText) findViewById(R.id.edittextzufußzumeinkaufentag);
            EditText zufußzumeinkaufenminuten = (EditText) findViewById(R.id.edittextzufußzumeinkaufenminuten);
            return (strtoint(zufußzumeinkaufentag) * strtoint(zufußzumeinkaufenminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int radzurarbeit() {
        try {
            EditText radzurarbeittag = (EditText) findViewById(R.id.edittextradzurarbeittag);
            EditText radzurarbeitminuten = (EditText) findViewById(R.id.edittextradzurarbeitminuten);
            return (strtoint(radzurarbeittag) * strtoint(radzurarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int radfahren() {
        try {
            EditText radfahrentag = (EditText) findViewById(R.id.edittextradfahrentag);
            EditText radfahrenminuten = (EditText) findViewById(R.id.edittextradfahrenminuten);
            return (strtoint(radfahrentag) * strtoint(radfahrenminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int spazieren() {
        try {
            EditText spazierentag = (EditText) findViewById(R.id.edittextspazierentag);
            EditText spazierenminuten = (EditText) findViewById(R.id.edittextspazierenminuten);
            return (strtoint(spazierentag) * strtoint(spazierenminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int gartenarbeit() {
        try {
            EditText gartenarbeittag = (EditText) findViewById(R.id.edittextgartenarbeittag);
            EditText gartenarbeitminuten = (EditText) findViewById(R.id.edittextgartenarbeitminuten);
            return (strtoint(gartenarbeittag) * strtoint(gartenarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int hausarbeit() {
        try {
            EditText hausarbeittag = (EditText) findViewById(R.id.edittexthausarbeittag);
            EditText hausarbeitminuten = (EditText) findViewById(R.id.edittexthausarbeitminuten);
            return (strtoint(hausarbeittag) * strtoint(hausarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int pflegearbeit() {
        try {
            EditText pflegetag = (EditText) findViewById(R.id.edittextpflegearbeittag);
            EditText pflegeminuten = (EditText) findViewById(R.id.edittextpflegearbeitminuten);
            return (strtoint(pflegetag) * strtoint(pflegeminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int treppensteigen() {
        try {
            EditText treppensteigentag = (EditText) findViewById(R.id.edittexttreppensteigentag);
            EditText stockwerke = (EditText) findViewById(R.id.edittexttreppensteigenstockwerke);
            return (strtoint(treppensteigentag) * strtoint(stockwerke));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    /**
     * Berechnet Scoringwert für Bewegung (Block 1-4)
     *
     * @return int scoringbewegungwert
     */
    private long scoringbewegung() {
        scoringbewegungwert = ((zufußzurarbeit() + zufußeinkaufen() + radzurarbeit() + radfahren() + spazieren() + gartenarbeit() + hausarbeit() + pflegearbeit() + treppensteigen()) / wochenzeitraum) + bewegungberuf();

        return scoringbewegungwert;

    }

    private int aktivitäta() {
        try {
            EditText aktaanzahl = (EditText) findViewById(R.id.edittextaktivitätaanzahl);
            EditText aktaminuten = (EditText) findViewById(R.id.edittextaktivitätaminuten);
            return (strtoint(aktaanzahl) * strtoint(aktaminuten));
        } catch (NumberFormatException b) {
            return 0;
        }

    }

    private int aktivitätb() {
        try {
            EditText aktbanzahl = (EditText) findViewById(R.id.edittextaktivitätbanzahl);
            EditText aktbminuten = (EditText) findViewById(R.id.edittextaktivitätbminuten);
            return (strtoint(aktbanzahl) * strtoint(aktbminuten));
        } catch (NumberFormatException b) {
            return 0;
        }
    }

    private int aktivitätc() {
        try {
            EditText aktcanzahl = (EditText) findViewById(R.id.edittextaktivitätcanzahl);
            EditText aktcminuten = (EditText) findViewById(R.id.edittextaktivitätcminuten);
            return (strtoint(aktcanzahl) * strtoint(aktcminuten));
        } catch (NumberFormatException b) {
            return 0;
        }
    }

    /**
     * Berechnung Scoringwert für Sportaktivität (Block 5-6)
     * Falls kein Sport betrieben wurde-> Listview "lstsportlichaktiv" auf "nein" (Index 1) gibt 0 zurück.
     * Sonst Multipliziere die Anzahl der aktiven Tage*die Minuten pro Tag und addiere dies für die 3 Aktivitäten und teile durch die ausgewählte Wochenenzahl-> Ergebnis "Minuten pro Woche"
     *
     * @return (int) scoringsportwert
     */
    private long scoringsport() {
        if (lstsportlichaktiv.getIndexBSA1() > 0)
            return 0;
        else

            scoringsportwert = (aktivitäta() + aktivitätb() + aktivitätc()) / wochenzeitraum;

        return scoringsportwert;
    }


    //Berechnung Gesamtscoring (Block 1-6)= Bewegungsscoring+Aktivitätsscoring

    /**
     * Summe aus scoringbewegungswert und scoringsportwert
     *
     * @return (int) scoringgesamt
     */
    private long scoringgesamt() {
        return scoringgesamtwert = scoringbewegung() + scoringsport();
    }




}

