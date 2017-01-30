package com.example.mb7.sportappbp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.Objects.LeistungstestsExercise;
import com.example.mb7.sportappbp.Objects.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.Objects.TrainingExercise;
import com.example.mb7.sportappbp.Objects.WellnessExercise;

import java.util.ArrayList;

import static android.R.attr.value;

public class ActivityExercises extends AppCompatActivity {


    private ListView listview;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> exLst;
    private ArrayList<Exercise> exerciseList;
    private String[] activities;

    private int newValue;
    //object values
    private DiaryEntry diaryEntry;
    private int timeMinutes;
    private int timeHours;

    private String chosenExercise;
    private String chosenCategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_category);

        chosenCategory = receiveCategory();
        exerciseList = receiveExerciseList();

        //Set title of the label
        setTitle(chosenCategory);
        //get the list of the activity for the chosen category
        exLst = getListOfActivities(chosenCategory);

        listview = (ListView) findViewById(R.id.listviewTraining);
        arrayAdapter = new ArrayAdapter<String>(ActivityExercises.this, android.R.layout.simple_list_item_1, exLst);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                chosenExercise = (String) adapterView.getItemAtPosition(position);
                NumberPicker();
                //numberPicker();
            }
        });
    }

    private void oldnumberPicker(){

        NumberPicker numberPicker = new NumberPicker(ActivityExercises.this);
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(0);
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        NumberPicker.OnValueChangeListener valueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                int test = newVal;
            }
        };

        numberPicker.setOnValueChangedListener(valueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityExercises.this).setView(numberPicker);

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

    private ArrayList<Exercise> receiveExerciseList(){

        ArrayList<Exercise> result;
        final Bundle extra = getIntent().getExtras();

        if (extra != null) {
            result = extra.getParcelableArrayList("oldExercises");
            return result;
        }
        else
            return result = new ArrayList<Exercise>();
    }

    /**
     * This method returns the result of the request. The result is the chosen activity
     */
    private void returnResult(){

        Exercise result = chosenExerciseCategory();

        if(result != null) {
            result.setActivity(chosenExercise);
            result.setTimeMinutes(timeMinutes);
            result.setTimeHours(timeHours);
            exerciseList.add(result);

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("newExercises", exerciseList);
            setResult(RESULT_OK, intent);
            finish();
        }
        else
            Toast.makeText(ActivityExercises.this, "Kategorie wurde nicht richtig gewählt!", Toast.LENGTH_SHORT).show();

    }

    private Exercise chosenExerciseCategory(){

        if(chosenCategory.equals("Leistungstests")){
            return new LeistungstestsExercise();
        }
        else if(chosenCategory.equals("Training")){
            return new TrainingExercise();
        }
        else if(chosenCategory.equals("Wellness")){
            return new WellnessExercise();
        }
        else if(chosenCategory.equals("Reiner Aufenthalt")){
            return new ReinerAufenthaltExercise();
        }//todo overthink else case
        else return null;

    }


    private void NumberPicker(){

        final Dialog dialog = new Dialog(ActivityExercises.this);

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_two_numberpicker);

        final String[] nums = new String[30];
        for(int i=0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }

        final NumberPicker npHoures = (NumberPicker)dialog.findViewById(R.id.numberPickerHours);
        npHoures.setMaxValue(24);
        npHoures.setMinValue(0);
        npHoures.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        final NumberPicker npMinutes = (NumberPicker)dialog.findViewById(R.id.numberPickerMinutes);
        npMinutes.setMaxValue(59);
        npMinutes.setMinValue(0);
        npMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        Button button = (Button)dialog.findViewById(R.id.npOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeHours = npHoures.getValue();
                timeMinutes = npMinutes.getValue();
                dialog.dismiss();
                returnResult();
            }
        });

        dialog.show();

    }



}
