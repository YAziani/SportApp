package com.example.mb7.sportappbp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivityDiary extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    String[] entries =  {"1.1.2017","2.1.2017","3.1.2017","4.1.2017","5.1.2017"};
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listviewDiary);
        arrayAdapter = new ArrayAdapter<String>(ActivityDiary.this, android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(arrayAdapter);

    }
}
