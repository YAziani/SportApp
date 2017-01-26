package com.example.mb7.sportappbp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiaryEntryActivity extends AppCompatActivity {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;
    DiaryEntry diaryEntry;
    AllDiaryEntries allDiaryEntries;

    private Menu menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryentry);

        //activate the back button on the toolbar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        evaluateData();
    }

    private void evaluateData(){

        radioGroup = (RadioGroup) findViewById(R.id.RadioGroupTraining);
        btnSave = (Button) findViewById(R.id.buttonSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedID);

                //check if a button is selected, if not give a message to the user
                if(radioButton == null)
                    Toast.makeText(DiaryEntryActivity.this, "Button nicht gewaehlt!", Toast.LENGTH_SHORT).show();
                else {

                    //Create the diary object with the input of the user
                    diaryEntry = new DiaryEntry(getCurrentDate(), radioButtonYesOrNo(radioButton));
                    //add the entry to the diary list
                    allDiaryEntries.getInstance().getDiaryList().add(diaryEntry);

                    //todo Save object to the database
                    //Display answer
                    //Toast.makeText(DiaryEntryActivity.this, strDate, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
        inflater.inflate(R.menu.menu_add, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:
                //Open the new activity
                Intent open = new Intent(DiaryEntryActivity.this, ActivityDiaryCategories.class);
                startActivity(open);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * This method evaluates the radio button of a radio button group, if the user selected yes or no.
     * When the text of the button called "Ja" the method returns true, if it's "Nein" it returns false.
     * @param radioBtn the selected radio button of a radio button group
     * @return true or false
     */
    private boolean radioButtonYesOrNo(RadioButton radioBtn){
        if(radioBtn.getText() == "Ja")
            return true;
        else
            return false;
    }

    /**
     * This method returns the current date as a string in the format "dd.MM.yy".
     * @return a String with the date "dd.MM.yy"
     */
    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }
}
