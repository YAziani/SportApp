package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.DiaryViewAdapter;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;
import java.util.Date;

public class ActivityDiary extends AppCompatActivity {

    final static int REQUEST_ID = 555;
    //ArrayAdapter<String> arrayAdapter;
    static DiaryViewAdapter diaryViewAdapter;
    ListView listView;
    AllDiaryEntries allDiaryEntries;
    DiaryEntry diaryEntry;

    ArrayList<Exercise> exerciseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        allDiaryEntries = allDiaryEntries.getInstance();


        //todo Daten von Datenbank laden


        //get the listView of the layout
        listView = (ListView) findViewById(R.id.listviewDiary);
        //create the new adapter with the list of all diary entries
        diaryViewAdapter = new DiaryViewAdapter(ActivityDiary.this, allDiaryEntries.getDiaryList());
        //set the adapter for the listview
        listView.setAdapter(diaryViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                diaryEntry = (DiaryEntry) adapterView.getItemAtPosition(position);

                exerciseList = diaryEntry.getExerciseList();
                sendOldAndRequestNewExerciseList();

                Toast.makeText(ActivityDiary.this, diaryEntry.getDate() , Toast.LENGTH_SHORT).show();
            }
        });


        ActivityMain.mainUser.GetDiaryEntry(new Date(2017,3,20 ));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Exercise> result;

        //check if the request was successful
        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){

            //get the new list
            result = data.getParcelableArrayListExtra("newExercises");
            exerciseList = result;
            diaryEntry.setExerciseList(result);

        }
    }

    public static void notifyDataChanged(){
        diaryViewAdapter.notifyDataSetChanged();
    }


    public void sendOldAndRequestNewExerciseList(){
        ArrayList<Exercise> oldList = exerciseList;
        Intent pickExerciseIntent = new Intent(this, ActivityDiaryEntry.class);
        pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

}
