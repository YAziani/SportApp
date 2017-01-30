package com.example.mb7.sportappbp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Objects.Exercise;

import java.util.ArrayList;

public class ActivityCategories extends AppCompatActivity {
    //request id for the activitiy request
    final static int REQUEST_ID = 3;

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String[] listOfCategories = {"Leistungstests", "Training", "Wellness", "Reiner Aufenthalt"};
    Intent open;

    ArrayList<Exercise> exercisesList;
    String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_categories);

        listView = (ListView) findViewById(R.id.listviewCategories);
        arrayAdapter = new ArrayAdapter<String>(ActivityCategories.this, android.R.layout.simple_list_item_1, listOfCategories);
        listView.setAdapter(arrayAdapter);

        exercisesList = receiveExerciseList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String category = (String) adapterView.getItemAtPosition(position);

                switch (category){
                    case "Leistungstests":
                        forwardOldExerciseList(category, exercisesList);
                        break;
                    case "Training":
                        forwardOldExerciseList(category, exercisesList);
                        break;
                    case "Wellness":
                        forwardOldExerciseList(category, exercisesList);
                        break;
                    case "Reiner Aufenthalt":
                        forwardOldExerciseList(category, exercisesList);
                        break;
                    default:

                }

            }
        });

        //todo Liste uebergeben, wenn back Button gedrueckt wird

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){
            ArrayList<Exercise> result = data.getParcelableArrayListExtra("newExercises");
            forwardResult(result);
        }
    }

    /**
     * This method forwards the result of the request
     * @param result
     */
    private void forwardResult(ArrayList<Exercise> result){

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("newExercises",result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void forwardOldExerciseList(String category, ArrayList<Exercise> exerciseList){

        Intent intent = new Intent(ActivityCategories.this, ActivityExercises.class);
        intent.putExtra("category", category);
        intent.putParcelableArrayListExtra("oldExercises", exerciseList);
        startActivityForResult(intent, REQUEST_ID);
    }

    private void receiveSerializable(){
        Exercise testExercise;
        ArrayList<Exercise> testList = new ArrayList<>();

        testList = getIntent().getParcelableArrayListExtra("category");

        double test = testList.get(0).getWeighting();


        Toast.makeText(ActivityCategories.this, String.valueOf(test), Toast.LENGTH_SHORT).show();


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
}
