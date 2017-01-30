package com.example.mb7.sportappbp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Felix on 19.01.2017.
 */

public class ActivityFragebogen extends AppCompatActivity{
    private FragebogenViewAdapter adapter;
    private FragebogenViewAdapter2 adapter2;
    private Button savebutton;
    private int scoringbewegung;
    private int scorungsport;
    private int scoringgesamt;

    private FragebogenListview berufstätig;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsaquestions);

        this.InitializeControlls();
        this.SetControlCaptions();

        super.onStart();

        savebutton = (Button) findViewById(R.id.savebuttonfragebogen);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityFragebogen.this, "Antworten wurden gespeichert!", Toast.LENGTH_SHORT).show();
                //TODO In Datenbank speichern



            }
        });


    }

    private void InitializeControlls(){

        // set the listivew
        // first create the adapter
        adapter = new FragebogenViewAdapter(this);
        adapter2 = new FragebogenViewAdapter2(this);
        FragebogenListview lstberufstätig = (FragebogenListview)findViewById( R.id.lvberufstätig);
        FragebogenListview lstsitzendetätigkeiten = (FragebogenListview) findViewById(R.id.lvsitzendetätigkeiten);
        FragebogenListview lstmäßigebewegung = (FragebogenListview)findViewById( R.id.lvmäßigebewegung);
        FragebogenListview lstintensivebewegung = (FragebogenListview)findViewById( R.id.lvintensivebewegung);
        FragebogenListview lstsportlichaktiv = (FragebogenListview)findViewById(R.id.lvsportlichaktiv);

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
        ((TextView)findViewById(R.id.txtFragebogenausfüllen)).setText("Bitte füllen Sie den Fragebogen aus!");
        ((TextView)findViewById(R.id.txtberufstätig)).setText("Sind Sie berufstätig oder in Ausbildung?");
        ((TextView)findViewById(R.id.txtsitzendetätigkeiten)).setText("Ihre berufstätigkeit bzw. Ausbildung umfasst sitzende Tätigkeiten");
        ((TextView)findViewById(R.id.txtmäßigebewegung)).setText("Ihre berufstätigkeit bzw. Ausbildung umfasst mäßige Bewegung");
        ((TextView)findViewById(R.id.txtintensivebewegung)).setText("Ihre berufstätigkeit bzw. Ausbildung umfasst intensive Bewegung");
        ((TextView)findViewById(R.id.txtwieoftsport)).setText("An wie vielen Tagen und wie lange haben Sie die folgenen Aktivitäten ausgeübt?");
        ((TextView)findViewById(R.id.txtsportlichaktiv)).setText("Haben Sie in der letzten Zeit regelmäßig sportliche Aktivität betrieben? Hierzu zählen Aktivitäten, die größere Muskelgruppen beanspruchen und zur Verbesserung der Ausdauer, Kraft und/oder Beweglichkeit führen, wie z.B. Fahrradfahren, Joggen, Fußball spielen und Reiten. Ausgeschlossen werden dabei Aktivitäten wie z.B. Schach, Billiard und Angeln.");
        ((TextView)findViewById(R.id.txtsportlicheaktivitäten)).setText("Um welche sportliche(n) Aktivitäte(en) handelt es sich dabei?");
    }



    private void scoringbewegung(){

    }

    private void scoringsport(){
        finish();
    }

    private void scoringgesamt(){
        finish();
    }


    }


