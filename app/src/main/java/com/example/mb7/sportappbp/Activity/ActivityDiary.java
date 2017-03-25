package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mb7.sportappbp.Adapters.DiaryViewAdapter;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.BusinessLayer.DiaryEntry;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

public class ActivityDiary extends AppCompatActivity {

    final static int REQUEST_ID = 555;
    static DiaryViewAdapter diaryViewAdapter;
    ListView listView;
    AllDiaryEntries allDiaryEntries;
    DiaryEntry diaryEntry;

    ArrayList<Exercise> exerciseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);


        //get a list with all diary entries
        allDiaryEntries = allDiaryEntries.getInstance();


        //get the listView of the layout
        listView = (ListView) findViewById(R.id.listviewDiary);
        //create the new adapter with the list of all diary entries
        diaryViewAdapter = new DiaryViewAdapter(ActivityDiary.this, allDiaryEntries.getDiaryList());
        //set the adapter for the listview
        listView.setAdapter(diaryViewAdapter);
        //set listener when an item has been clicked
        listView.setOnItemClickListener(listViewOnItemClickListener);

        //clear list against duplicates
        allDiaryEntries.getDiaryList().clear();
        //load list from firesbase
        //todo laden aller Eintraege nicht nur einem
        ActivityMain.mainUser.LoadCompleteDiry();

    }

    /**
     * Action when an item has been clicked in the list
     */
    AdapterView.OnItemClickListener listViewOnItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            //get entry of the clicked item
            diaryEntry = (DiaryEntry) adapterView.getItemAtPosition(position);

            exerciseList = diaryEntry.getExerciseList();
            //send the exercise list of the entry to the diaryEntry activity
            sendOldAndRequestNewExerciseList();

        }};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        ArrayList<Exercise> result;

        //check the correctness of the received packages
        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){

            //get and set the result of the request
            result = data.getParcelableArrayListExtra("newExercises");
            exerciseList = result;
            diaryEntry.setExerciseList(result);

        }
    }

    /**
     * This method start notifyDataSetChanged of the view adapter. When data has been changed in
     * the AllDiaryEntries object from external, this method has to be executed
     */
    public static void notifyDataChanged(){
        diaryViewAdapter.notifyDataSetChanged();
    }


    /**
     * this method send the exerciseList of the selected diaryEntry to the diaryEntry Activity.
     * Furthermore this method sends a request to receive the prepared exerciseList
     */
    public void sendOldAndRequestNewExerciseList(){
        ArrayList<Exercise> oldList = exerciseList;
        //set goal activity
        Intent pickExerciseIntent = new Intent(this, ActivityDiaryEntry.class);
        //List in package
        pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        //send package and open the goal activity
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

}
