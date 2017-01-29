package com.example.mb7.sportappbp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityCategories extends AppCompatActivity {
    //request id for the activitiy request
    final static int REQUEST_ID = 2;

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String[] listOfCategories = {"Leistungstests", "Training", "Wellness", "Reiner Aufenthalt"};
    Intent open;

    ArrayList<String> listOfActivities;
    String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_categories);

        listView = (ListView) findViewById(R.id.listviewCategories);
        arrayAdapter = new ArrayAdapter<String>(ActivityCategories.this, android.R.layout.simple_list_item_1, listOfCategories);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String category = (String) adapterView.getItemAtPosition(position);

                switch (category){
                    case "Leistungstests":
                        pickActivity(category);
                        break;
                    case "Training":
                        pickActivity(category);
                        break;
                    case "Wellness":
                        pickActivity(category);
                        break;
                    case "Reiner Aufenthalt":
                        pickActivity(category);
                        break;
                    default:

                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){
            forwardResult(data.getExtras().getString("activity"), data.getExtras().getInt("duration"));
        }
    }

    /**
     * This method forwards the result of the request
     * @param result
     */
    private void forwardResult(String result, int duration){

        Intent intent = new Intent();
        intent.putExtra("activity",result);
        intent.putExtra("duration", duration);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void pickActivity(String category){
        Intent intent = new Intent(ActivityCategories.this, ActivityActivityCategory.class);
        intent.putExtra("category", category);
        startActivityForResult(intent, REQUEST_ID);
    }
}
