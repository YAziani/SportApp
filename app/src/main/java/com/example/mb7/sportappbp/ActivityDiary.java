package com.example.mb7.sportappbp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mb7.sportappbp.Objects.AllDiaryEntries;

import static android.R.attr.entries;

public class ActivityDiary extends AppCompatActivity {


    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    AllDiaryEntries allDiaryEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //get the listView of the layout
        listView = (ListView) findViewById(R.id.listviewDiary);
        arrayAdapter = new ArrayAdapter<String>(ActivityDiary.this, android.R.layout.simple_list_item_1, entries);
        arrayAdapter = new ArrayAdapter<String>(ActivityDiary.this, android.R.layout.simple_list_item_1, allDiaryEntries.getInstance().getAllDates());
        listView.setAdapter(arrayAdapter);

    }
}
