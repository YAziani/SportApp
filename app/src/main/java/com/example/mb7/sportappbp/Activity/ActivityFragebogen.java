package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.mb7.sportappbp.Adapters.FragebogenViewAdapter;
import com.example.mb7.sportappbp.Adapters.FragebogenViewAdapter2;
import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.FragebogenListview;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityFragebogen extends AppCompatActivity {
    private FragebogenViewAdapter adapter;
    private FragebogenViewAdapter2 adapter2;
    ProgressDialog pd;
    long scoringbewegungwert;
    long scoringsportwert;
    long scoringgesamtwert;
    int bewegungsaktivitätberuf;
    FragebogenListview lstberufstätig;
    FragebogenListview lstsitzendetätigkeiten;
    FragebogenListview lstmäßigebewegung;
    FragebogenListview lstintensivebewegung;
    FragebogenListview lstsportlichaktiv;
    EditText zufußzurarbeittag;
    EditText zufußzurarbeitminuten;
    EditText zufußeinkaufentag;
    EditText zufußeinkaufenminuten;
    EditText radzurarbeittag;
    EditText readzurarbeitminuten;
    EditText radfahrentag;
    EditText radfahrenminuten;
    EditText spazierentag;
    EditText spazierenminuten;
    EditText gartenarbeittag;
    EditText gartenarbeitminuten;
    EditText hausarbeittag;
    EditText hausarbeitminuten;
    EditText pflgearbeittag;
    EditText pflegearbeitminuten;
    EditText treppentag;
    EditText treppenstockwerke;
    static long wochenzeitraum;
    int itemsitzendetätigkeit;
    int itemmäßigebewegung;
    int itemintensivebewegung;
    private Firebase mRootRef;
    private ActivityFragebogen activityFragebogen = this;
    boolean INSERT = true;
    Fragebogen fragebogen = null;

    public static Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    @Override
    protected void onStart() {
        super.onStart();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();
        getWochenanzahlFromDb();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsaquestions);
        this.SetControlCaptions();

        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {
            String notificationDate = (String) extras.get("NotificationDate");
            fragebogen = (Fragebogen) iin.getSerializableExtra(getString(R.string.aktivitaetsfragebogen));
            if (fragebogen != null) {
                INSERT = false;
            }
            if (notificationDate != null)
                removeNofiication(this, notificationDate);
        }
        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");
        this.InitializeControlls();
    }

    /**
     * Referenzzeitraum für den BSAFragebogen wird aus Firebase geladen
     */
    void getWochenanzahlFromDb() {
        try {
            Firebase root = new Firebase("https://sportapp-cbd6b.firebaseio" + "" +
                    ".com/Administration/bsa/questionaryPeriodweeks");
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    wochenzeitraum = convertToLong(dataSnapshot.getValue());
                    SetControlCaptions();
                    pd.dismiss();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("Fragebogen", firebaseError.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void removeNofiication(Context context, String notificationDate) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Firebase ref = new Firebase("https://sportapp-cbd6b.firebaseio.com/" + "users/" + preferences.getString
                ("logedIn", "") + "/Notifications/");
        ref.child(context.getString(R.string.aktivitaetsfragebogen)).child(notificationDate).removeValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
        inflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    /**
     * Dialog der Nach dem Drücken des Sepicherbuttons erscheint. Speichert dann in Datenbank (SaveData())
     */
    private void speicheralert() {
        AlertDialog.Builder speicherbuilder = new AlertDialog.Builder(this);
        speicherbuilder.setTitle(getString(R.string.Ergebnis));
        speicherbuilder.setMessage(getString(R.string.Bewegungsscore) + " " + scoringbewegung() + "\n" + getString(R
                .string.Sportscore) + " " + scoringsport() + "\n" + getString(R.string.Gesamtscore) + " " +
                scoringgesamt());
        speicherbuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveData();
                finish();
                Toast ausgabe = Toast.makeText(activityFragebogen, getString(R.string.Erfolgreich_gespeichert), Toast
                        .LENGTH_SHORT);
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

        fragebogen.Berufstaetig = lstberufstätig.getIndexBSA1();
        fragebogen.sitzende_Taetigkeiten = lstsitzendetätigkeiten.getIndexBSA2();
        fragebogen.maeßige_Bewegung = lstmäßigebewegung.getIndexBSA2();
        fragebogen.intensive_Bewegung = lstintensivebewegung.getIndexBSA2();
        fragebogen.sportlich_aktiv = lstsportlichaktiv.getIndexBSA1();

        fragebogen.Bewegungsscoring = scoringbewegung();
        fragebogen.Sportscoring = scoringsport();
        fragebogen.Gesamtscoring = scoringgesamt();

        fragebogen.Zu_Fuß_zur_Arbeit = zufußzurarbeit();

        fragebogen.Zu_Fuß_zur_Arbeit_Tag = strtoint((EditText) findViewById(R.id.edittextzufußzurarbeittag));
        fragebogen.Zu_Fuß_zur_Arbeit_Minuten = strtoint((EditText) findViewById(R.id.edittextzufußzurarbeitminuten));
        fragebogen.Zu_Fuß_einkaufen_Tag = strtoint((EditText) findViewById(R.id.edittextzufußzumeinkaufentag));
        fragebogen.Zu_Fuß_einkaufen_Minuten = strtoint((EditText) findViewById(R.id.edittextzufußzumeinkaufenminuten));
        fragebogen.Rad_zur_Arbeit_Tag = strtoint((EditText) findViewById(R.id.edittextradzurarbeittag));
        fragebogen.Rad_zur_Arbeit_Minuten = strtoint((EditText) findViewById(R.id.edittextradzurarbeitminuten));
        fragebogen.Radfahren_Tag = strtoint((EditText) findViewById(R.id.edittextradfahrentag));
        fragebogen.Radfahren_Minuten = strtoint((EditText) findViewById(R.id.edittextradfahrenminuten));
        fragebogen.Spazieren_Tag = strtoint((EditText) findViewById(R.id.edittextspazierentag));
        fragebogen.Spazieren_Minuten = strtoint((EditText) findViewById(R.id.edittextspazierenminuten));
        fragebogen.Gartenarbeit_Tag = strtoint((EditText) findViewById(R.id.edittextgartenarbeittag));
        fragebogen.Gartenarbeit_Minuten = strtoint((EditText) findViewById(R.id.edittextgartenarbeitminuten));
        fragebogen.Hausarbeit_Tag = strtoint((EditText) findViewById(R.id.edittexthausarbeittag));
        fragebogen.Hausarbeit_Minuten = strtoint((EditText) findViewById(R.id.edittexthausarbeitminuten));
        fragebogen.Pflegearbeit_Tag = strtoint((EditText) findViewById(R.id.edittextpflegearbeittag));
        fragebogen.Pflegearbeit_Minuten = strtoint((EditText) findViewById(R.id.edittextpflegearbeitminuten));
        fragebogen.Treppensteigen_Tag = strtoint((EditText) findViewById(R.id.edittexttreppensteigentag));
        fragebogen.Treppensteigen_Stockwerke = strtoint((EditText) findViewById(R.id.edittexttreppensteigenstockwerke));

        fragebogen.Zu_Fuß_einkaufen = zufußeinkaufen();
        fragebogen.Rad_zur_Arbeit = radzurarbeit();
        fragebogen.Radfahren = radfahren();
        fragebogen.Spazieren = spazieren();
        fragebogen.Gartenarbeit = gartenarbeit();
        fragebogen.Hausarbeit = hausarbeit();
        fragebogen.Pflegearbeit = pflegearbeit();
        fragebogen.Treppensteigen = treppensteigen();

        fragebogen.Aktivitaet_A_Name = aktivitätaname();
        fragebogen.Aktivitaet_A_Zeit = aktivitäta();
        fragebogen.Aktivitaet_A_Einheiten = strtoint((EditText) findViewById(R.id.edittextaktivitätaanzahl));
        fragebogen.Aktivitaet_A_Minuten = strtoint((EditText) findViewById(R.id.edittextaktivitätaminuten));
        fragebogen.Aktivitaet_B_Name = aktivitätbname();
        fragebogen.Aktivitaet_B_Zeit = aktivitätb();
        fragebogen.Aktivitaet_B_Einheiten = strtoint((EditText) findViewById(R.id.edittextaktivitätbanzahl));
        fragebogen.Aktivitaet_B_Minuten = strtoint((EditText) findViewById(R.id.edittextaktivitätbminuten));
        fragebogen.Aktivitaet_C_Name = aktivitätcname();
        fragebogen.Aktivitaet_C_Zeit = aktivitätc();
        fragebogen.Aktivitaet_C_Einheiten = strtoint((EditText) findViewById(R.id.edittextaktivitätcanzahl));
        fragebogen.Aktivitaet_C_Minuten = strtoint((EditText) findViewById(R.id.edittextaktivitätcminuten));

        if (INSERT) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            fragebogen.Date = sdf.format(new Date());
        } else
            fragebogen.Date = this.fragebogen.FirebaseDate;

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

    private void InitializeControlls() {

        LinearLayout beruflayout = (LinearLayout) findViewById(R.id.layoutberuf);
        LinearLayout sportlayout = (LinearLayout) findViewById(R.id.layoutsport);

        lstberufstätig = (FragebogenListview) findViewById(R.id.lvberufstätig);
        lstsitzendetätigkeiten = (FragebogenListview) findViewById(R.id.lvsitzendetätigkeiten);
        lstmäßigebewegung = (FragebogenListview) findViewById(R.id.lvmäßigebewegung);
        lstintensivebewegung = (FragebogenListview) findViewById(R.id.lvintensivebewegung);
        lstsportlichaktiv = (FragebogenListview) findViewById(R.id.lvsportlichaktiv);

        zufußzurarbeittag = (EditText) findViewById(R.id.edittextzufußzurarbeittag);
        zufußzurarbeitminuten = (EditText) findViewById(R.id.edittextzufußzurarbeitminuten);
        zufußeinkaufentag = (EditText) findViewById(R.id.edittextzufußzumeinkaufentag);
        zufußeinkaufenminuten = (EditText) findViewById(R.id.edittextzufußzumeinkaufenminuten);
        radzurarbeittag = (EditText) findViewById(R.id.edittextradzurarbeittag);
        readzurarbeitminuten = (EditText) findViewById(R.id.edittextradzurarbeitminuten);
        radfahrentag = (EditText) findViewById(R.id.edittextradfahrentag);
        radfahrenminuten = (EditText) findViewById(R.id.edittextradfahrenminuten);
        spazierentag = (EditText) findViewById(R.id.edittextspazierentag);
        spazierenminuten = (EditText) findViewById(R.id.edittextspazierenminuten);
        gartenarbeittag = (EditText) findViewById(R.id.edittextgartenarbeittag);
        gartenarbeitminuten = (EditText) findViewById(R.id.edittextgartenarbeitminuten);
        hausarbeittag = (EditText) findViewById(R.id.edittexthausarbeittag);
        hausarbeitminuten = (EditText) findViewById(R.id.edittexthausarbeitminuten);
        pflgearbeittag = (EditText) findViewById(R.id.edittextpflegearbeittag);
        pflegearbeitminuten = (EditText) findViewById(R.id.edittextpflegearbeitminuten);
        treppentag = (EditText) findViewById(R.id.edittexttreppensteigentag);
        treppenstockwerke = (EditText) findViewById(R.id.edittexttreppensteigenstockwerke);

        EditText aktaname = (EditText) findViewById(R.id.edittextaktivitäta);
        EditText aktaeinheit = (EditText) findViewById(R.id.edittextaktivitätaanzahl);
        EditText aktaminunten = (EditText) findViewById(R.id.edittextaktivitätaminuten);
        EditText aktbname = (EditText) findViewById(R.id.edittextaktivitätb);
        EditText aktbeinheit = (EditText) findViewById(R.id.edittextaktivitätbanzahl);
        EditText aktbminunten = (EditText) findViewById(R.id.edittextaktivitätbminuten);
        EditText aktcname = (EditText) findViewById(R.id.edittextaktivitätc);
        EditText aktceinheit = (EditText) findViewById(R.id.edittextaktivitätcanzahl);
        EditText aktcminunten = (EditText) findViewById(R.id.edittextaktivitätcminuten);

        adapter = new FragebogenViewAdapter(this);
        adapter.setAntworten(fragebogen, getString(R.string.Sind_Sie_berufstaetig_oder_in_Ausbildung));
        adapter.setSelectedIndex(fragebogen != null && fragebogen.Berufstaetig != null ? fragebogen.Berufstaetig : -1);
        lstberufstätig.setAdapter(adapter);

        adapter2 = new FragebogenViewAdapter2(this);
        adapter2.setAntworten(fragebogen, getString(R.string.Umfasst_Ihre_Berufstaetigkeit_sitzende_Taetigkeiten));
        adapter2.setSelectedIndex(fragebogen != null && fragebogen.Berufstaetig != null && fragebogen
                .sitzende_Taetigkeiten != null ? fragebogen.sitzende_Taetigkeiten : -1);
        lstsitzendetätigkeiten.setAdapter(adapter2);

        adapter2 = new FragebogenViewAdapter2(this);
        adapter2.setAntworten(fragebogen, getString(R.string
                .Umfasst_Ihre_Berufstaetigkeit_Ausbildung_maeßige_Bewegung));
        adapter2.setSelectedIndex(fragebogen != null && fragebogen.Berufstaetig != null && fragebogen
                .maeßige_Bewegung != null ? fragebogen.maeßige_Bewegung : -1);
        lstmäßigebewegung.setAdapter(adapter2);

        adapter2 = new FragebogenViewAdapter2(this);
        adapter2.setAntworten(fragebogen, getString(R.string
                .Umfasst_Ihre_Berufstaetigkeit_Ausbildung_intensive_Bewegung));
        adapter2.setSelectedIndex(fragebogen != null && fragebogen.Berufstaetig != null && fragebogen
                .intensive_Bewegung != null ? fragebogen.intensive_Bewegung : -1);
        lstintensivebewegung.setAdapter(adapter2);

        adapter = new FragebogenViewAdapter(this);
        adapter.setAntworten(fragebogen, getString(R.string.regelmaeßig_Sport_betrieben));
        adapter.setSelectedIndex(fragebogen != null && fragebogen.sportlich_aktiv != null ? fragebogen
                .sportlich_aktiv : -1);
        lstsportlichaktiv.setAdapter(adapter);

        //Wenn Fragebebogen bearbeitet wird. Textfelder aus dem Fragebogen laden und einsetzen.
        if (!INSERT) {
            if (fragebogen.Zu_Fuß_zur_Arbeit_Tag != null)
                zufußzurarbeittag.setText(String.valueOf(fragebogen.Zu_Fuß_zur_Arbeit_Tag));
            if (fragebogen.Zu_Fuß_zur_Arbeit_Minuten != null)
                zufußzurarbeitminuten.setText(String.valueOf(fragebogen.Zu_Fuß_zur_Arbeit_Minuten));
            if (fragebogen.Zu_Fuß_einkaufen_Tag != null)
                zufußeinkaufentag.setText(String.valueOf(fragebogen.Zu_Fuß_einkaufen_Tag));
            if (fragebogen.Zu_Fuß_einkaufen_Minuten != null)
                zufußeinkaufenminuten.setText(String.valueOf(fragebogen.Zu_Fuß_einkaufen_Minuten));
            if (fragebogen.Rad_zur_Arbeit_Tag != null)
                radzurarbeittag.setText(String.valueOf(fragebogen.Rad_zur_Arbeit_Tag));
            if (fragebogen.Rad_zur_Arbeit_Minuten != null)
                readzurarbeitminuten.setText(String.valueOf(fragebogen.Rad_zur_Arbeit_Minuten));
            if (fragebogen.Radfahren_Tag != null)
                radfahrentag.setText(String.valueOf(fragebogen.Radfahren_Tag));
            if (fragebogen.Radfahren_Minuten != null)
                radfahrenminuten.setText(String.valueOf(fragebogen.Radfahren_Minuten));
            if (fragebogen.Spazieren_Tag != null)
                spazierentag.setText(String.valueOf(fragebogen.Spazieren_Tag));
            if (fragebogen.Spazieren_Minuten != null)
                spazierenminuten.setText(String.valueOf(fragebogen.Spazieren_Minuten));
            if (fragebogen.Gartenarbeit_Tag != null)
                gartenarbeittag.setText(String.valueOf(fragebogen.Gartenarbeit_Tag));
            if (fragebogen.Gartenarbeit_Minuten != null)
                gartenarbeitminuten.setText(String.valueOf(fragebogen.Gartenarbeit_Minuten));
            if (fragebogen.Hausarbeit_Tag != null)
                hausarbeittag.setText(String.valueOf(fragebogen.Hausarbeit_Tag));
            if (fragebogen.Hausarbeit_Minuten != null)
                hausarbeitminuten.setText(String.valueOf(fragebogen.Hausarbeit_Minuten));
            if (fragebogen.Pflegearbeit_Tag != null)
                pflgearbeittag.setText(String.valueOf(fragebogen.Pflegearbeit_Tag));
            if (fragebogen.Pflegearbeit_Minuten != null)
                pflegearbeitminuten.setText(String.valueOf(fragebogen.Pflegearbeit_Minuten));
            if (fragebogen.Treppensteigen_Tag != null)
                treppentag.setText(String.valueOf(fragebogen.Treppensteigen_Tag));
            if (fragebogen.Treppensteigen_Stockwerke != null)
                treppenstockwerke.setText(String.valueOf(fragebogen.Treppensteigen_Stockwerke));

            if (fragebogen.Aktivitaet_A_Name != null)
                aktaname.setText(String.valueOf(fragebogen.Aktivitaet_A_Name));
            if (fragebogen.Aktivitaet_A_Einheiten != null)
                aktaeinheit.setText(String.valueOf(fragebogen.Aktivitaet_A_Einheiten));
            if (fragebogen.Aktivitaet_A_Minuten != null)
                aktaminunten.setText(String.valueOf(fragebogen.Aktivitaet_A_Minuten));
            if (fragebogen.Aktivitaet_B_Name != null)
                aktbname.setText(String.valueOf(fragebogen.Aktivitaet_B_Name));
            if (fragebogen.Aktivitaet_B_Einheiten != null)
                aktbeinheit.setText(String.valueOf(fragebogen.Aktivitaet_B_Einheiten));
            if (fragebogen.Aktivitaet_B_Minuten != null)
                aktbminunten.setText(String.valueOf(fragebogen.Aktivitaet_B_Minuten));
            if (fragebogen.Aktivitaet_C_Name != null)
                aktcname.setText(String.valueOf(fragebogen.Aktivitaet_C_Name));
            if (fragebogen.Aktivitaet_C_Einheiten != null)
                aktceinheit.setText(String.valueOf(fragebogen.Aktivitaet_C_Einheiten));
            if (fragebogen.Aktivitaet_C_Minuten != null)
                aktcminunten.setText(String.valueOf(fragebogen.Aktivitaet_C_Minuten));
        }

        lstsitzendetätigkeiten.InitializeBSA();
        lstmäßigebewegung.InitializeBSA();
        lstintensivebewegung.InitializeBSA();

        lstberufstätig.visibility(beruflayout);
        lstsportlichaktiv.visibility(sportlayout);
    }

    private void SetControlCaptions() {
        ((TextView) findViewById(R.id.txtwieoftsport)).setText(getString(R.string
                .An_wie_vielen_Tagen_und_wie_lange_haben_Sie_die_folgenden_Aktivitaeten_in_den_letzten) + " " +
                wochenzeitraum + " " + getString(R.string.Wochen_ausgeuebt));
        ((TextView) findViewById(R.id.txtsportlichaktiv)).setText(getString(R.string.Haben_Sie_in_den_letzten) + " "
                + wochenzeitraum + " " + getString(R.string.regelmaeßig_Sport_betrieben));
        ((TextView) findViewById(R.id.txtzeitraum)).setText(getString(R.string
                .Folgende_Fragen_beziehen_sich_auf_den_Zeitraum_von) + " " + wochenzeitraum + " " + getString(R
                .string.Wochen) + "\n");
        ((TextView) findViewById(R.id.txtaktivitätaanzahl)).setText(getString(R.string
                .Wie_oft_haben_Sie_Aktivitaet_A_in_den_letzten) + " " + wochenzeitraum + " " + getString(R.string
                .Wochen_ausgeuebt));
        ((TextView) findViewById(R.id.txtaktivitätbanzahl)).setText(getString(R.string
                .Wie_oft_haben_Sie_Aktivitaet_B_in_den_letzten) + " " + wochenzeitraum + " " + getString(R.string
                .Wochen_ausgeuebt));
        ((TextView) findViewById(R.id.txtaktivitätcanzahl)).setText(getString(R.string
                .Wie_oft_haben_Sie_Aktivitaet_C_in_den_letzten) + " " + wochenzeitraum + " " + getString(R.string
                .Wochen_ausgeuebt));
    }

    private int strtoint(EditText edittexteingabe) {
        try {
            return Integer.parseInt(edittexteingabe.getText().toString());
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    /**
     * Aus dem Indexwert des gewählten Listviewelement wird der Scoringwert berechnet.
     * (Negativ: Je mehr sitzend -> desto schlechter)
     *
     * @param index
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
     * Aus dem Indexwert des gewählten Listviewelement wird der Scoringwert berechnet.
     * (Positiv: Je mehr bewegung -> desto besser)
     *
     * @param index
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
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int zufußeinkaufen() {
        try {
            EditText zufußzumeinkaufentag = (EditText) findViewById(R.id.edittextzufußzumeinkaufentag);
            EditText zufußzumeinkaufenminuten = (EditText) findViewById(R.id.edittextzufußzumeinkaufenminuten);
            return (strtoint(zufußzumeinkaufentag) * strtoint(zufußzumeinkaufenminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int radzurarbeit() {
        try {
            EditText radzurarbeittag = (EditText) findViewById(R.id.edittextradzurarbeittag);
            EditText radzurarbeitminuten = (EditText) findViewById(R.id.edittextradzurarbeitminuten);
            return (strtoint(radzurarbeittag) * strtoint(radzurarbeitminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int radfahren() {
        try {
            EditText radfahrentag = (EditText) findViewById(R.id.edittextradfahrentag);
            EditText radfahrenminuten = (EditText) findViewById(R.id.edittextradfahrenminuten);
            return (strtoint(radfahrentag) * strtoint(radfahrenminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int spazieren() {
        try {
            EditText spazierentag = (EditText) findViewById(R.id.edittextspazierentag);
            EditText spazierenminuten = (EditText) findViewById(R.id.edittextspazierenminuten);
            return (strtoint(spazierentag) * strtoint(spazierenminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int gartenarbeit() {
        try {
            EditText gartenarbeittag = (EditText) findViewById(R.id.edittextgartenarbeittag);
            EditText gartenarbeitminuten = (EditText) findViewById(R.id.edittextgartenarbeitminuten);
            return (strtoint(gartenarbeittag) * strtoint(gartenarbeitminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int hausarbeit() {
        try {
            EditText hausarbeittag = (EditText) findViewById(R.id.edittexthausarbeittag);
            EditText hausarbeitminuten = (EditText) findViewById(R.id.edittexthausarbeitminuten);
            return (strtoint(hausarbeittag) * strtoint(hausarbeitminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int pflegearbeit() {
        try {
            EditText pflegetag = (EditText) findViewById(R.id.edittextpflegearbeittag);
            EditText pflegeminuten = (EditText) findViewById(R.id.edittextpflegearbeitminuten);
            return (strtoint(pflegetag) * strtoint(pflegeminuten));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private int treppensteigen() {
        try {
            EditText treppensteigentag = (EditText) findViewById(R.id.edittexttreppensteigentag);
            EditText stockwerke = (EditText) findViewById(R.id.edittexttreppensteigenstockwerke);
            return (strtoint(treppensteigentag) * strtoint(stockwerke));
        } catch (NumberFormatException a) {
            return 0;
        }
    }

    private long scoringbewegung() {
        scoringbewegungwert = ((zufußzurarbeit() + zufußeinkaufen() + radzurarbeit() + radfahren() + spazieren() +
                gartenarbeit() + hausarbeit() + pflegearbeit() + treppensteigen()) / wochenzeitraum) + bewegungberuf();

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

    private long scoringsport() {
        if (lstsportlichaktiv.getIndexBSA1() > 0)
            return 0;
        else

            scoringsportwert = (aktivitäta() + aktivitätb() + aktivitätc()) / wochenzeitraum;

        return scoringsportwert;
    }

    private long scoringgesamt() {
        return scoringgesamtwert = scoringbewegung() + scoringsport();
    }
}

