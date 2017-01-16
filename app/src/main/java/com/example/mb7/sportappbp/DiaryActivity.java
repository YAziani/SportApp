package com.example.mb7.sportappbp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mb7.sportappbp.Objects.AllDiaryEntries;

public class DiaryActivity extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    AllDiaryEntries allDiaryEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //get the listView of the layout
        listView = (ListView) findViewById(R.id.listviewDiary);
        //create the array adapter and get the list of all dates
        arrayAdapter = new ArrayAdapter<String>(DiaryActivity.this, android.R.layout.simple_list_item_1, allDiaryEntries.getInstance().getAllDates());
        listView.setAdapter(arrayAdapter);

    }
}
