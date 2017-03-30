package com.tud.bp.fitup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.tud.bp.fitup.Adapters.DiaryEntryViewAdapter;
import com.tud.bp.fitup.BusinessLayer.DiaryEntry;
import com.tud.bp.fitup.BusinessLayer.Exercise;
import com.tud.bp.fitup.R;
import com.firebase.client.Firebase;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class shows the time and point for each category
 * and you can add new exercises or save the entry
 */
public class ActivityDiaryEntry extends AppCompatActivity {

    private DiaryEntry diaryEntry;
    private ArrayList<Exercise> exerciseList;
    private GridView gridView;
    private DiaryEntryViewAdapter diaryEntryViewAdapter;
    private Firebase mRootRef;
    private Calendar calendar = Calendar.getInstance();
    ArrayList<Integer> listCategories = new ArrayList<Integer>();
    ArrayList<Integer> listIcons = new ArrayList<Integer>();
    Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryentry);

        diaryEntry = new DiaryEntry();

        // Now receive and read data without a request - from ActivityDiary
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if(extras!=null ) {

            //Get unpack the exerciseList und date
            exerciseList = extras.getParcelableArrayList("oldExercises");
            date = (Date) extras.getSerializable("date");

            diaryEntry.setExerciseList(exerciseList);
            diaryEntry.setDate(date);
        }
        else{

            diaryEntry.setDate(calendar.getTime());
            exerciseList = diaryEntry.getExerciseList();

        }



        //Create Firebase reference
        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");


        //add all categories of activities for the grid view
        listCategories.add(R.string.Leistungstests);
        listCategories.add(R.string.Training);
        listCategories.add(R.string.Wellness);
        listCategories.add(R.string.ReinerAufenthalt);

        //set all icons for the viewadapter
        listIcons.add(R.drawable.ic_fitnesscheck);
        listIcons.add(R.drawable.ic_sport);
        listIcons.add(R.drawable.ic_wellness);
        listIcons.add(R.drawable.ic_aufenthalt);

        //create and set gridview with an adapter to show the categories with icons and their time and points
        diaryEntryViewAdapter = new DiaryEntryViewAdapter(ActivityDiaryEntry.this, listCategories, diaryEntry, listIcons);
        gridView = (GridView) findViewById(R.id.gridViewExercise);
        gridView.setAdapter(diaryEntryViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //set the menu with add and save icon
        inflater.inflate(R.menu.menu_add_and_save , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was pressed in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:

                //plus button to add/ an new activity
                openSelectedExercises();
                finish();

                return true;
            case R.id.icon_save:
                //calculate the totalpoints and save it
                diaryEntry.setTotalPoints(calculateTotalPoints());

                //check if any exercises have been added to the list before saving
                if(diaryEntry.getExerciseList().size() > 0) {
                    //save the entry with exercise list to database
                    SaveData();
                }

                else//Give feedback to the user that there was nothing to save
                    Toast.makeText(ActivityDiaryEntry.this, R.string.keintagebucheintragerstellt , Toast.LENGTH_SHORT).show();

                finish();
                return true;

            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }

    /**
     * Save diaryEntry to Firebase and give feedback to the user that the data has been saved successfully
     * @return
     */
    private boolean SaveData() {

        //ActivityMain.mainUser.GetLastTodayDiaryEntry(new Date());
        ActivityMain.getMainUser(this).SaveDiaryEntry(diaryEntry);
        Toast.makeText(ActivityDiaryEntry.this, R.string.Tagebucheintraggespeichert, Toast.LENGTH_SHORT).show();

        return true;
    }

    /**
     * This method calculates the total points from all categories
     * @return int value with total points
     */
    private int calculateTotalPoints(){
        int leistungstests = diaryEntry.getTotalTimePointsAsArrayLeistungstests()[2];
        int training = diaryEntry.getTotalTimePointsAsArrayTraining()[2];
        int wellness = diaryEntry.getTotalTimePointsAsArrayWellness()[2];
        int reinerAufenthalt = diaryEntry.getTotalTimePointsAsArrayReinerAufenthalt()[2];

        int totalPoints = leistungstests +training + wellness + reinerAufenthalt;

        return totalPoints;
    }

    /**
     * This method sends a package with three addition information to the selected exercises activity. The first
     * information is the cuurent list of exercises, the second one is the date of the diary entry. The last information
     * is an information, that the diary entry already exists and will just be prepared
     */
    private void openSelectedExercises(){

            Intent open = new Intent(ActivityDiaryEntry.this , ActivitySelectedExercises.class);

            // pass the clicked diaryEntry to the activity
            open.putParcelableArrayListExtra("oldExercises", exerciseList);
            open.putExtra("date", date);
            open.putExtra("prepare", true);
            startActivity(open);

    }

}
