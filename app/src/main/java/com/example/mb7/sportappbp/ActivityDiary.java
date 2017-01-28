package com.example.mb7.sportappbp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        //create the new adapter with the list of all diary entries
        arrayAdapter = new ArrayAdapter<String>(ActivityDiary.this, android.R.layout.simple_list_item_1, allDiaryEntries.getInstance().getAllDates());
        //set the adapter for the listview
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String date = (String) adapterView.getItemAtPosition(position);

                Toast.makeText(ActivityDiary.this, date, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
