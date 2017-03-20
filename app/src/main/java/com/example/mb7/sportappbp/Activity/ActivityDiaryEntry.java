package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.DiaryEntryViewAdapter;
import com.example.mb7.sportappbp.Adapters.ExerciseViewAdapter;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityDiaryEntry extends AppCompatActivity {

    //request id for the activitiy request
    final static int REQUEST_ID = 1;

    private DiaryEntry diaryEntry;
    private AllDiaryEntries allDiaryEntries;

    private ListView listView;
    private ExerciseViewAdapter exerciseViewAdapter;
    private ArrayList<Exercise> exerciseList;

    private TextView textView;

    private GridView gridView;
    private DiaryEntryViewAdapter diaryEntryViewAdapter;

    private Firebase mRootRef;


    ArrayList<String> listCategories = new ArrayList<String>();
    ArrayList<Integer> listIcons = new ArrayList<Integer>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryentry);

        //Create Firebase reference
        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/players");

        //Create Diary Entry Object to safe all data
        diaryEntry = new DiaryEntry(getID(), getCurrentDate(), getCurrentTime());

        exerciseList = diaryEntry.getExerciseList();

        //set all categories for the viewadapter
        listCategories.add("Leistungstests");
        listCategories.add("Training");
        listCategories.add("Wellness");
        listCategories.add("Reiner Aufenthalt");

        //set all icons for the viewadapter
        listIcons.add(R.drawable.ic_fitnesscheck);
        listIcons.add(R.drawable.ic_sport);
        listIcons.add(R.drawable.ic_wellness);
        listIcons.add(R.drawable.ic_aufenthalt);


        diaryEntryViewAdapter = new DiaryEntryViewAdapter(ActivityDiaryEntry.this, listCategories, diaryEntry, listIcons);
        gridView = (GridView) findViewById(R.id.gridViewExercise);
        gridView.setAdapter(diaryEntryViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set the menu with add and save icon
        inflater.inflate(R.menu.menu_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:
                sendOldAndRequestNewExerciseList();
                return true;
            case R.id.icon_save:
                btnSaveAction();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void btnSaveAction() {

        //todo Save object to the database
        if(diaryEntry.getExerciseList().size() > 0)
            SaveData();

        else//Display answer
            Toast.makeText(ActivityDiaryEntry.this, "Es wurden keine Einträge registriert!" , Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Exercise> result;
        //check if the request was successful
        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){

            //add the new exercises to
            result = data.getParcelableArrayListExtra("newExercises");
            exerciseList = result;
            diaryEntry.setExerciseList(result);

            diaryEntryViewAdapter.notifyDataSetChanged();

        }
    }

    private boolean SaveData(){

        ActivityMain.mainUser.GetLastTodayDiaryEntry(new Date());
        ActivityMain.mainUser.SaveDiaryEntry(diaryEntry);
        Toast.makeText(ActivityDiaryEntry.this, "Eintrag gespeichert!" , Toast.LENGTH_SHORT).show();

        return true;
    }

    /**
     * This method returns the current date as a string in the format "dd.MM.yy".
     * @return a String with the date "dd.MM.yy"
     */
    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    private String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strTime = sdf.format(c.getTime());
        return strTime;
    }

    private String getID(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strID = sdf.format(c.getTime());
        return strID;
    }

    /**
     * This method starts a request to the activity ActiveCategorys
     */
    public void sendOldAndRequestNewExerciseList(){
        ArrayList<Exercise> oldList = exerciseList;
        Intent pickExerciseIntent = new Intent(this, ActivityExerciseOverview.class);
        pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

}
