package com.example.mb7.sportappbp.Activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.FragebogenListview;
import com.firebase.client.Firebase;


/**
 * Created by Felix on 19.01.2017.
 */

public class ActivityFragebogen extends AppCompatActivity{
    private FragebogenViewAdapter adapter;
    private FragebogenViewAdapter2 adapter2;

    //Integer Werte für Scoring des Fragebogens
    int scoringbewegungwert;
    int scoringsportwert;
    int scoringgesamtwert;
    int bewegungsaktivitätberuf;

    //Listviews
    FragebogenListview lstberufstätig;
    FragebogenListview lstsitzendetätigkeiten;
    FragebogenListview lstmäßigebewegung;
    FragebogenListview lstintensivebewegung;
    FragebogenListview lstsportlichaktiv;

    //Zeitaum für Fragen ab Block 4
    int wochenzeitraum=4;


    int itemsitzendetätigkeit;
    int itemmäßigebewegung;
    int itemintensivebewegung;

    private Firebase mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsaquestions);
        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/players");
        this.SetControlCaptions();
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
                SaveData();
                speicheralert();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void speicheralert() {
        AlertDialog.Builder speicherbuilder=new AlertDialog.Builder(this);
        speicherbuilder.setTitle("Ergebnis");
        speicherbuilder.setMessage(
                "Bewegungsscore: " + scoringbewegung()+"\n" +
                "Sportscore: " + scoringsport() + "\n" +
                "Gesamtscore: " + scoringgesamt());
        speicherbuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Toast ausgabe= Toast.makeText(ActivityMain.activityMain,
                        "Erfolgreich gespeichert",Toast.LENGTH_LONG);
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
        fragebogen.berufstätig = lstberufstätig.getIndex();
        fragebogen.sitzendetätigkeiten = lstsitzendetätigkeiten.getIndex();
        fragebogen.mäßigebewegung = lstmäßigebewegung.getIndex();
        fragebogen.intensivebewegung = lstintensivebewegung.getIndex();
        fragebogen.sportlichaktiv = lstsportlichaktiv.getIndex();

        //Integerwerte des Scorings
        fragebogen.bewegungscoring = scoringbewegung();
        fragebogen.sportscoring = scoringsport();
        fragebogen.gesamtscoring= scoringgesamt();

        //Integerwerte mit Anzahl der Minuten pro Woche
        fragebogen.zufußzurarbeit=zufußzurarbeit();
        fragebogen.zufußeinkaufen=zufußeinkaufen();
        fragebogen.radzurarbeit=radzurarbeit();
        fragebogen.radfahren=radfahren();
        fragebogen.spazieren=spazieren();
        fragebogen.gartenarbeit=gartenarbeit();
        fragebogen.hausarbeit=hausarbeit();
        fragebogen.pflegearbeit=pflegearbeit();
        fragebogen.treppensteigen=treppensteigen();

        //String mit Namen der Aktivität + Integerwert mit Anzahl der Minuten pro Woche
        fragebogen.aktivitätaname=aktivitätaname();
        fragebogen.aktivitäta=aktivitäta();
        fragebogen.aktivitätbname=aktivitätbname();
        fragebogen.aktivitätb=aktivitätb();
        fragebogen.aktivitätcname=aktivitätcname();
        fragebogen.aktivitätc=aktivitätc();

        return fragebogen;
    }

    private boolean SaveData(){
        Fragebogen fragebogen = getData();
        ActivityMain.mainUser.SaveFragebogen(fragebogen);

        return true;

    }


    /**
     * Initialisiert die Listviews -> Über Funktionen aus Fragebogenlistview
     */
    private void InitializeControlls(){

        LinearLayout beruflayout=(LinearLayout)findViewById(R.id.layoutberuf);
        LinearLayout sportlayout=(LinearLayout)findViewById(R.id.layoutsport);
        // set the listivew
        // first create the adapters
        adapter = new FragebogenViewAdapter(this);
        adapter2 = new FragebogenViewAdapter2(this);
        lstberufstätig = (FragebogenListview)findViewById( R.id.lvberufstätig);
        lstsitzendetätigkeiten = (FragebogenListview) findViewById(R.id.lvsitzendetätigkeiten);
        lstmäßigebewegung = (FragebogenListview)findViewById( R.id.lvmäßigebewegung);
        lstintensivebewegung = (FragebogenListview)findViewById( R.id.lvintensivebewegung);
        lstsportlichaktiv = (FragebogenListview)findViewById(R.id.lvsportlichaktiv);

        lstberufstätig.setAdapter(adapter);
        lstsitzendetätigkeiten.setAdapter(adapter2);
        lstmäßigebewegung.setAdapter(adapter2);
        lstintensivebewegung.setAdapter(adapter2);
        lstsportlichaktiv.setAdapter(adapter);


        // set the onTouch Event to disable scrolling
        //lstberufstätig.Initialize();
        lstsitzendetätigkeiten.Initialize();
        lstmäßigebewegung.Initialize();
        lstintensivebewegung.Initialize();
        //lstsportlichaktiv.Initialize();

        lstberufstätig.visibility(beruflayout);
        lstsportlichaktiv.visibility(sportlayout);
    }

    /**
     * Inhalt der Textviews mit definiertem Zeitraum.
     */
    private void SetControlCaptions(){
        ((TextView)findViewById(R.id.txtwieoftsport)).setText("An wie vielen Tagen und wie lange haben Sie die folgenen Aktivitäten in den letzten " + wochenzeitraum +  " Wochen ausgeübt?");
        ((TextView)findViewById(R.id.txtsportlichaktiv)).setText("Haben Sie in den letzten "+ wochenzeitraum + " Wochen regelmäßig sportliche Aktivität betrieben? Hierzu zählen Aktivitäten, die größere Muskelgruppen beanspruchen und zu einer Verbesserung der Ausdauer, Kraft und/oder Beweglichkeit führen, wie z.B. Fahrradfahren, Joggen, Fußball spielen und Reiten. Ausgeschlossen werden dabei Aktivitäten wie z.B. Schach, Billiard und Angeln.");
        ((TextView)findViewById(R.id.txtzeitraum)).setText("Folgende Fragen beziehen sich auf den Zeitraum von " + wochenzeitraum +" Wochen! \n ");
        ((TextView)findViewById(R.id.txtaktivitätaanzahl)).setText("Wie oft haben Sie Aktivität A in den letzten " + wochenzeitraum + " Wochen ausgeübt?");
        ((TextView)findViewById(R.id.txtaktivitätbanzahl)).setText("Wie oft haben Sie Aktivität B in den letzten " + wochenzeitraum + " Wochen ausgeübt?");
        ((TextView)findViewById(R.id.txtaktivitätcanzahl)).setText("Wie oft haben Sie Aktivität C in den letzten " + wochenzeitraum + " Wochen ausgeübt?");
    }





    /**
     * Macht aus einer Editeingabe einen Integer Wert -> Achtung: In XML Eingabetyp auf Number setzen!!
     * Werfe NumberFormatException, wenn anderes Format und gib 0 zurück
     * @param edittexteingabe
     * @return int
     */
     private int strtoint(EditText edittexteingabe){
        try {
            return Integer.parseInt(edittexteingabe.getText().toString());
        }
        catch (NumberFormatException a){
           return 0;

    }}

    /**
     * Scoringwert für Sitzende Tätigkeit: Keine=3 Punkte; Eher wenig=2; Eher mehr=1; viel=0; -> Keine angabe gleich 0;
     * @param index
     * @return int
     */
    private int listscoringsitzend (int index){
        switch (index){
            case 3: return 0;
            case 2: return 1;
            case 1: return 2;
            case 0: return 3;
            default: return 0;
        }
    }

    /**
     * Scoringwert für Bewegung (Mäßige und Intensive Bewegung): Keine=0; Eher wenig=1; Eher mehr=2; Viel=3;
     * @param index
     * @return int
     */
    private int listscoringbewegung (int index){
        switch (index){
            case 3: return 3;
            case 2: return 2;
            case 1: return 1;
            case 0: return 0;
            default: return 0;
        }
    }

    /**
     * Berechne Werte für die Listviews, indem für bestimmte Index Werte Punkte vergeben werden, Punkte werden in listscoringsitzend + listscoringbewegung vergeben.
     * @return int bewegungsaktivitätberuf
     */
    private int bewegungberuf(){
        if (lstberufstätig.getIndex()>0)
            return 0;
        else
        itemsitzendetätigkeit=listscoringsitzend(lstsitzendetätigkeiten.getIndex());
        itemmäßigebewegung=listscoringbewegung(lstmäßigebewegung.getIndex());
        itemintensivebewegung=listscoringbewegung(lstintensivebewegung.getIndex());
        return bewegungsaktivitätberuf=itemsitzendetätigkeit+itemmäßigebewegung+itemintensivebewegung;
    }

    private String aktivitätaname(){
        EditText aktivitätaname = (EditText) findViewById(R.id.edittextaktivitäta);
        return (aktivitätaname.getText().toString());
    }

    private String aktivitätbname(){
        EditText aktivitätbname = (EditText) findViewById(R.id.edittextaktivitätb);
        return (aktivitätbname.getText().toString());
    }

    private String aktivitätcname(){
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

    private int zufußeinkaufen(){
        try{
            EditText zufußzumeinkaufentag=(EditText)findViewById(R.id.edittextzufußzumeinkaufentag);
            EditText zufußzumeinkaufenminuten=(EditText)findViewById(R.id.edittextzufußzumeinkaufenminuten);
            return (strtoint(zufußzumeinkaufentag)*strtoint(zufußzumeinkaufenminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int radzurarbeit(){
        try{
            EditText radzurarbeittag=(EditText)findViewById(R.id.edittextradzurarbeittag);
            EditText radzurarbeitminuten=(EditText)findViewById(R.id.edittextradzurarbeitminuten);
            return(strtoint(radzurarbeittag)*strtoint(radzurarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int radfahren(){
        try{
            EditText radfahrentag=(EditText)findViewById(R.id.edittextradfahrentag);
            EditText radfahrenminuten=(EditText)findViewById(R.id.edittextradfahrenminuten);
            return(strtoint(radfahrentag)*strtoint(radfahrenminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }
    private int spazieren(){
        try{
            EditText spazierentag=(EditText)findViewById(R.id.edittextspazierentag);
            EditText spazierenminuten=(EditText)findViewById(R.id.edittextspazierenminuten);
            return(strtoint(spazierentag)*strtoint(spazierenminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int gartenarbeit(){
        try{
            EditText gartenarbeittag=(EditText)findViewById(R.id.edittextgartenarbeittag);
            EditText gartenarbeitminuten=(EditText)findViewById(R.id.edittextgartenarbeitminuten);
            return(strtoint(gartenarbeittag)*strtoint(gartenarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int hausarbeit(){
        try{
            EditText hausarbeittag=(EditText)findViewById(R.id.edittexthausarbeittag);
            EditText hausarbeitminuten=(EditText)findViewById(R.id.edittexthausarbeitminuten);
            return(strtoint(hausarbeittag)*strtoint(hausarbeitminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int pflegearbeit(){
        try {
            EditText pflegetag=(EditText)findViewById(R.id.edittextpflegearbeittag);
            EditText pflegeminuten=(EditText)findViewById(R.id.edittextpflegearbeitminuten);
            return(strtoint(pflegetag)*strtoint(pflegeminuten));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    private int treppensteigen(){
        try{
            EditText treppensteigentag=(EditText)findViewById(R.id.edittexttreppensteigentag);
            EditText stockwerke=(EditText)findViewById(R.id.edittexttreppensteigenstockwerke);
            return (strtoint(treppensteigentag)*strtoint(stockwerke));
        }
        //Falls Falsche Eingabe (keine Zahl), dann werfe NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a) {
            return 0;
        }
    }

    /**
     * Berechnet Scoringwert für Bewegung (Block 1-4)
     * @return int scoringbewegungwert
     */
    private int scoringbewegung(){
        scoringbewegungwert=((zufußzurarbeit()+zufußeinkaufen()+radzurarbeit()+radfahren()+spazieren()+gartenarbeit()+hausarbeit()+pflegearbeit()+treppensteigen())/wochenzeitraum)+bewegungberuf();

            return scoringbewegungwert;

    }

    private int aktivitäta(){
        try{
            EditText aktaanzahl = (EditText) findViewById(R.id.edittextaktivitätaanzahl);
            EditText aktaminuten = (EditText) findViewById(R.id.edittextaktivitätaminuten);
            return (strtoint(aktaanzahl) * strtoint(aktaminuten));
        }
        catch (NumberFormatException b){
            return 0;
        }

    }

    private int aktivitätb(){
        try{
            EditText aktbanzahl = (EditText) findViewById(R.id.edittextaktivitätbanzahl);
            EditText aktbminuten = (EditText) findViewById(R.id.edittextaktivitätbminuten);
            return(strtoint(aktbanzahl) * strtoint(aktbminuten));
        }
        catch (NumberFormatException b){
            return 0;
        }
    }

    private int aktivitätc(){
        try{
            EditText aktcanzahl = (EditText) findViewById(R.id.edittextaktivitätcanzahl);
            EditText aktcminuten = (EditText) findViewById(R.id.edittextaktivitätcminuten);
            return (strtoint(aktcanzahl) * strtoint(aktcminuten));
        }
        catch (NumberFormatException b){
            return 0;
        }
    }

    /**
     * Berechnung Scoringwert für Sportaktivität (Block 5-6)
     * Falls kein Sport betrieben wurde-> Listview "lstsportlichaktiv" auf "nein" (Index 1) gibt 0 zurück.
     * Sonst Multipliziere die Anzahl der aktiven Tage*die Minuten pro Tag und addiere dies für die 3 Aktivitäten und teile durch die ausgewählte Wochenenzahl-> Ergebnis "Minuten pro Woche"
     * @return (int) scoringsportwert
     */
    private int scoringsport(){
       if (lstsportlichaktiv.getIndex()>0)
           return 0;
        else

            scoringsportwert =(aktivitäta()+aktivitätb()+aktivitätc())/wochenzeitraum;

            return scoringsportwert;
        }


    //Berechnung Gesamtscoring (Block 1-6)= Bewegungsscoring+Aktivitätsscoring

    /**
     * Summe aus scoringbewegungswert und scoringsportwert
     * @return (int) scoringgesamt
     */
    private int scoringgesamt(){
        return scoringgesamtwert=scoringbewegung()+scoringsport();
    }


    }


