package com.example.mb7.sportappbp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.example.mb7.sportappbp.Objects.DiaryEntry;

import java.util.ArrayList;

public class ActivityActivityCategory extends AppCompatActivity {


    ListView listview;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listOfActivities;
    String[] activities;
    DiaryEntry diaryEntry;
    int newValue;
    int duration;
    String chosenActivity;
    String chosenCategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_category);

        chosenCategory = receiveCategory();

        //Set title of the label
        setTitle(chosenCategory);
        //get the list of the activity for the chosen category
        listOfActivities = getListOfActivities(chosenCategory);

        listview = (ListView) findViewById(R.id.listviewTraining);
        arrayAdapter = new ArrayAdapter<String>(ActivityActivityCategory.this, android.R.layout.simple_list_item_1, listOfActivities);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                chosenActivity = (String) adapterView.getItemAtPosition(position);

                numberPicker();

                //todo ausgewahlte item makieren und deslect ermoeglichen

            }
        });
    }

    private void numberPicker(){

        NumberPicker numberPicker = new NumberPicker(ActivityActivityCategory.this);
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(0);
        NumberPicker.OnValueChangeListener valueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                duration = newVal;
            }
        };
        numberPicker.setOnValueChangedListener(valueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityActivityCategory.this).setView(numberPicker);

        builder.setTitle("Wähle die Minuten");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               returnResult();
            }
        });
        builder.show();
    }

    private ArrayList getListOfActivities(String category){

        ArrayList<String> result = new ArrayList<>();

        switch(category){
            case "Leistungstests":
                result.add("Spiroergometrie");
                result.add("Laktattest");
                result.add("Beweglichkeitstest");
                result.add("Krafttest");
                result.add("Anderer Leistungstest");
                return result;

            case "Training":
                result.add("Krafttraining");
                result.add("Laufen");
                result.add("Cycling");
                result.add("Beweglichkeit/Flexibilität");
                result.add("Yoga");
            return result;

            case "Wellness":
                result.add("Sauna");
                result.add("Dampfbad");
                result.add("Massage");
                result.add("Solarium");
                result.add("Andere Wellnessaktivität");
                return result;

            case "Reiner Aufenthalt":
                result.add("Soziale Kontakte");
                result.add("Bistro");
                result.add("Andere");
                return result;
            default:
                result.add("Kategorie falsch ausgeählt");
                return result;
        }

    }

    private String receiveCategory(){
        String category = "";

        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            category = extra.getString("category");
        }
        return category;
    }

    /**
     * This method returns the result of the request. The result is the chosen activity
     */
    private void returnResult(){

        Intent intent = new Intent();
        intent.putExtra("activity", chosenActivity);
        intent.putExtra("duration", duration);
        setResult(RESULT_OK, intent);
        finish();

    }

}
