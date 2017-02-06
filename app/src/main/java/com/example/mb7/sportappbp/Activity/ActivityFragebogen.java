package com.example.mb7.sportappbp.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.BusinessLayer.Fragebogen;
import com.example.mb7.sportappbp.Adapters.FragebogenViewAdapter;
import com.example.mb7.sportappbp.Adapters.FragebogenViewAdapter2;
import com.example.mb7.sportappbp.R;
import com.example.mb7.sportappbp.UI_Controls.FragebogenListview;


/**
 * Created by Felix on 19.01.2017.
 */

public class ActivityFragebogen extends AppCompatActivity{
    private FragebogenViewAdapter adapter;
    private FragebogenViewAdapter2 adapter2;

    int scoringbewegungwert;
    int scoringsportwert;
    int scoringgesamtwert;
    int bewegungsaktivitätberuf;


    FragebogenListview lstberufstätig;
    FragebogenListview lstsitzendetätigkeiten;
    FragebogenListview lstmäßigebewegung;
    FragebogenListview lstintensivebewegung;
    FragebogenListview lstsportlichaktiv;

    LinearLayout beruflayout;

    int wochenzeitraum=4;

    int itemberufstätig;
    int itemsitzendetätigkeit;
    int itemmäßigebewegung;
    int itemintensivebewegung;
    int itemsportlichaktiv;

    Button ptest;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsaquestions);
        this.InitializeControlls();
        this.SetControlCaptions();
        super.onStart();
        this.scoringbewegung();
        this.toastscoringtest();
        //this.SetLayoutVisibility();

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
                //SaveData();
                finish();
                Toast.makeText(ActivityMain.activityMain,"Erfolgreich gespeichert",Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private Fragebogen getData() {
        Fragebogen fragebogen = new Fragebogen();
        fragebogen.berufstätig = lstberufstätig.getIndex();
        fragebogen.sitzendetätigkeiten = lstsitzendetätigkeiten.getIndex();
        fragebogen.mäßigebewegung = lstmäßigebewegung.getIndex();
        fragebogen.intensivebewegung = lstintensivebewegung.getIndex();
        fragebogen.sportlichaktiv = lstsportlichaktiv.getIndex();

        return fragebogen;
    }

    private boolean SaveData(){
        Fragebogen fragebogen = getData();
        //ActivityMain.mainUser.SaveFragebogen(fragebogen);

        return true;

    }



    private void InitializeControlls(){

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
        lstberufstätig.Initialize();
        lstsitzendetätigkeiten.Initialize();
        lstmäßigebewegung.Initialize();
        lstintensivebewegung.Initialize();
        lstsportlichaktiv.Initialize();
    }

    private void SetControlCaptions(){
        /*((TextView)findViewById(R.id.txtFragebogenausfüllen)).setText("Bitte füllen Sie den Fragebogen aus!");
        ((TextView)findViewById(R.id.txtberufstätig)).setText("Sind Sie berufstätig oder in Ausbildung?");
        ((TextView)findViewById(R.id.txtsitzendetätigkeiten)).setText("Ihre berufstätigkeit bzw. Ausbildung umfasst sitzende Tätigkeiten");
        ((TextView)findViewById(R.id.txtmäßigebewegung)).setText("Ihre berufstätigkeit bzw. Ausbildung umfasst mäßige Bewegung");
        ((TextView)findViewById(R.id.txtintensivebewegung)).setText("Ihre berufstätigkeit bzw. Ausbildung umfasst intensive Bewegung");
        ((TextView)findViewById(R.id.txtsportlicheaktivitäten)).setText("Um welche sportliche(n) Aktivitäte(en) handelt es sich dabei?");  */

        ((TextView)findViewById(R.id.txtwieoftsport)).setText("An wie vielen Tagen und wie lange haben Sie die folgenen Aktivitäten in den letzten " + wochenzeitraum +  " Wochen ausgeübt?");
        ((TextView)findViewById(R.id.txtsportlichaktiv)).setText("Haben Sie in den letzten "+ wochenzeitraum + " Wochen regelmäßig sportliche Aktivität betrieben? Hierzu zählen Aktivitäten, die größere Muskelgruppen beanspruchen und zur Verbesserung der Ausdauer, Kraft und/oder Beweglichkeit führen, wie z.B. Fahrradfahren, Joggen, Fußball spielen und Reiten. Ausgeschlossen werden dabei Aktivitäten wie z.B. Schach, Billiard und Angeln.");
        ((TextView)findViewById(R.id.txtzeitraum)).setText("Folgende Fragen beziehen sich auf den Zeitraum von " + wochenzeitraum +" Wochen! \n ");
        ((TextView)findViewById(R.id.txtaktivitätaanzahl)).setText("Wie oft haben Sie Aktivität A in den letzten " + wochenzeitraum + " Wochen ausgeübt");
        ((TextView)findViewById(R.id.txtaktivitätbanzahl)).setText("Wie oft haben Sie Aktivität A in den letzten " + wochenzeitraum + " Wochen ausgeübt");
        ((TextView)findViewById(R.id.txtaktivitätcanzahl)).setText("Wie oft haben Sie Aktivität A in den letzten " + wochenzeitraum + " Wochen ausgeübt");
    }

    /**
     * Setzt Linearlayout, wenn Index größer 0 auf unsichtbar
     * @param linearLayout
     * @param indexwert
     */
    private void SetLayoutVisibility(LinearLayout linearLayout, int indexwert){

        if (indexwert<=0) {
            linearLayout.setVisibility(LinearLayout.VISIBLE);}
        else{
            linearLayout.setVisibility(LinearLayout.GONE);

        }
           }


    /**
     * Macht aus einer Editeingabe einen Integer Wert -> Achtung: In XML Eingabetyp auf Number setzen!!
     * @param edittexteingabe
     * @return int
     */
     private int strtoint(EditText edittexteingabe){
        try {
            return Integer.parseInt(edittexteingabe.getText().toString());

            //Integer.parseInt(edittexteingabe.getText().toString());
            //Integer.valueOf(edittexteingabe.getText().toString());
        }
        catch (NumberFormatException a){
            //Toast.makeText(getApplicationContext(),"Falsches Format. Bitte nur Zahlen eingeben!",Toast.LENGTH_LONG).show();
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

    //Berechnet Scoringwert für Bewegung (Block 1-4)
    private int scoringbewegung(){

        itemsitzendetätigkeit=listscoringsitzend(lstsitzendetätigkeiten.getIndex());
        itemmäßigebewegung=listscoringbewegung(lstmäßigebewegung.getIndex());
        itemintensivebewegung=listscoringbewegung(lstintensivebewegung.getIndex());
        bewegungsaktivitätberuf=itemsitzendetätigkeit+itemmäßigebewegung+itemintensivebewegung;


        try {
            EditText zufußzurarbeittag= (EditText)findViewById(R.id.edittextzufußzurarbeittag);
            EditText zufußzurarbeitminuten=(EditText)findViewById(R.id.edittextzufußzurarbeitminuten);
            EditText zufußzumeinkaufentag=(EditText)findViewById(R.id.edittextzufußzumeinkaufentag);
            EditText zufußzumeinkaufenminuten=(EditText)findViewById(R.id.edittextzufußzumeinkaufenminuten);
            EditText radzurarbeittag=(EditText)findViewById(R.id.edittextradzurarbeittag);
            EditText radzurarbeitminuten=(EditText)findViewById(R.id.edittextradzurarbeitminuten);
            EditText radfahrentag=(EditText)findViewById(R.id.edittextradfahrentag);
            EditText radfahrenminuten=(EditText)findViewById(R.id.edittextradfahrenminuten);
            EditText spazierentag=(EditText)findViewById(R.id.edittextspazierentag);
            EditText spazierenminuten=(EditText)findViewById(R.id.edittextspazierenminuten);
            EditText gartenarbeittag=(EditText)findViewById(R.id.edittextgartenarbeittag);
            EditText gartenarbeitminuten=(EditText)findViewById(R.id.edittextgartenarbeitminuten);
            EditText hausarbeittag=(EditText)findViewById(R.id.edittexthausarbeittag);
            EditText hausarbeitminuten=(EditText)findViewById(R.id.edittexthausarbeitminuten);
            EditText pflegetag=(EditText)findViewById(R.id.edittextpflegearbeittag);
            EditText pflegeminuten=(EditText)findViewById(R.id.edittextpflegearbeitminuten);

            EditText treppensteigentag=(EditText)findViewById(R.id.edittexttreppensteigentag);
            EditText stockwerke=(EditText)findViewById(R.id.edittexttreppensteigenstockwerke);


            //Integerwerte als Produkt der Einzelnen Angaben
            int zufußzurarbeit=(strtoint(zufußzurarbeittag)*strtoint(zufußzurarbeitminuten));
            int zufußeinkaufen=(strtoint(zufußzumeinkaufentag)*strtoint(zufußzumeinkaufenminuten));
            int radzurarbeit=(strtoint(radzurarbeittag)*strtoint(radzurarbeitminuten));
            int radfahren=(strtoint(radfahrentag)*strtoint(radfahrenminuten));
            int spazieren=(strtoint(spazierentag)*strtoint(spazierenminuten));
            int gartenarbeit=(strtoint(gartenarbeittag)*strtoint(gartenarbeitminuten));
            int hausarbeit=(strtoint(hausarbeittag)*strtoint(hausarbeitminuten));
            int pflegearbeit=(strtoint(pflegetag)*strtoint(pflegeminuten));

            int treppensteigen=(strtoint(treppensteigentag)*strtoint(stockwerke));


            itemberufstätig=lstberufstätig.getIndex();
            //SetLayoutVisibility(beruflayout,itemberufstätig);





            //Scoringwertsumme
            scoringbewegungwert=(zufußzurarbeit+zufußeinkaufen+radzurarbeit+radfahren+spazieren+gartenarbeit+hausarbeit+pflegearbeit+treppensteigen)/wochenzeitraum;


            return scoringbewegungwert;
        }
        //Falls Falsche Eingabe (keine Zahl), dann NumberFormatException -> sollte eigentlich aufgrund Einschränkung in XML nicht passieren
        catch (NumberFormatException a){
            //Toast.makeText(getApplicationContext(),"Falsches Format. Bitte nur Zahlen eingeben!",Toast.LENGTH_SHORT).show();
            return scoringbewegungwert;
        }
    }



    //Berechnung Scoringwert für Sportaktivität (Block 5-6)
    private int scoringsport(){
        try {
            EditText aktaanzahl = (EditText) findViewById(R.id.edittextaktivitätaanzahl);
            EditText aktaminuten = (EditText) findViewById(R.id.edittextaktivitätaminuten);
            EditText aktbanzahl = (EditText) findViewById(R.id.edittextaktivitätbanzahl);
            EditText aktbminuten = (EditText) findViewById(R.id.edittextaktivitätbminuten);
            EditText aktcanzahl = (EditText) findViewById(R.id.edittextaktivitätcanzahl);
            EditText aktcminuten = (EditText) findViewById(R.id.edittextaktivitätcminuten);

            int aktivitäta=(strtoint(aktaanzahl) * strtoint(aktaminuten));
            int aktivitätb=(strtoint(aktbanzahl) * strtoint(aktbminuten));
            int aktivitätc=(strtoint(aktcanzahl) * strtoint(aktcminuten));

            scoringsportwert =(aktivitäta+aktivitätb+aktivitätc)/wochenzeitraum;

            return scoringsportwert;
        }

        catch (NumberFormatException b){
            return scoringsportwert;

        }
    }

    //Berechnung Gesamtscoring (Block 1-6)= Bewegungsscoring+Aktivitätsscoring
    private int scoringgesamt(){
        return scoringgesamtwert=scoringbewegung()+scoringsport()+bewegungsaktivitätberuf;
    }


    //Testbutton für Scoringwerte
    //TODO später löschen
    public void toastscoringtest(){
        ptest = (Button) findViewById(R.id.punktetest);
        ptest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                                "Bewegungsaktivität im Beruf: " + bewegungsaktivitätberuf + "\n" +
                                "Bewegungsaktivität Freizeit: " + scoringbewegung() + " Minuten pro Woche" + "\n" +
                                "Bewegungsaktivität: " + (scoringbewegung()+bewegungsaktivitätberuf) + "\n" +
                                "Sportaktivität: " + scoringsport()+ " Minuten pro Woche  " + "\n" +
                                "Scoring Gesamt: " + scoringgesamt()
                        ,Toast.LENGTH_LONG).show();

            }}

        );}

    }


