package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.DiaryEntryViewAdapter;
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
    private boolean newData = true;
    private DiaryEntry diaryEntry;
    private AllDiaryEntries allDiaryEntries;
    private ArrayList<Exercise> exerciseList;
    private GridView gridView;
    private DiaryEntryViewAdapter diaryEntryViewAdapter;
    private Firebase mRootRef;
    ArrayList<Integer> listCategories = new ArrayList<Integer>();
    ArrayList<Integer> listIcons = new ArrayList<Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryentry);

        //Create Firebase reference
        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");

        allDiaryEntries = AllDiaryEntries.getInstance();

        //Create Diary Entry Object to safe all data
        diaryEntry = new DiaryEntry();
        diaryEntry.setId(getID());
        diaryEntry.setDate(getCurrentDate());
        diaryEntry.setTime(getCurrentTime());

        //exerciseList = diaryEntry.getExerciseList();
        exerciseList = receiveExerciseList();

        diaryEntry.setExerciseList(exerciseList);


        //set all categories for the viewadapter
        listCategories.add(R.string.Leistungstests);
        listCategories.add(R.string.Training);
        listCategories.add(R.string.Wellness);
        listCategories.add(R.string.ReinerAufenthalt);

        //set all icons for the viewadapter
        listIcons.add(R.drawable.ic_fitnesscheck);
        listIcons.add(R.drawable.ic_sport);
        listIcons.add(R.drawable.ic_wellness);
        listIcons.add(R.drawable.ic_aufenthalt);

        //create and set gridview with an adapter
        diaryEntryViewAdapter = new DiaryEntryViewAdapter(ActivityDiaryEntry.this, listCategories, diaryEntry, listIcons);
        gridView = (GridView) findViewById(R.id.gridViewExercise);
        gridView.setAdapter(diaryEntryViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //set the menu with add and save icon
        inflater.inflate(R.menu.menu_add_and_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was pressed in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:
                //send current list to the next activity and open it (categories)
                sendOldAndRequestNewExerciseList();
                return true;
            case R.id.icon_save:
                //calculate the totalpoints and save it
                diaryEntry.setTotalpoints(calculateTotalPoints());
                //check if a new entry has been created or just been prepared
                if(!newData) {
                    //return exerciseList to previous activity (diary)
                    returnResult();
                    //save prepared entry to firebase
                    //todo new save method to save prepared entries
                    SaveData();
                    //reset flag
                    newData = false;
                }
                else
                    //save new entry to database
                    saveNewData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

            //update listview
            diaryEntryViewAdapter.notifyDataSetChanged();

        }
    }

    /**
     *
     */
    private void saveNewData() {

        //check if any exercises have been added to the list
        if(diaryEntry.getExerciseList().size() > 0) {
            //save data to firebase
            SaveData();
            allDiaryEntries.getDiaryList().add(diaryEntry);
        }

        else//Say that there was nothing to save
            Toast.makeText(ActivityDiaryEntry.this, R.string.keintagebucheintragerstellt , Toast.LENGTH_SHORT).show();

        finish();
    }

    /**
     * Save diaryEntry to Firebase
     * @return
     */
    private boolean SaveData(){

        ActivityMain.mainUser.GetLastTodayDiaryEntry(new Date());
        ActivityMain.mainUser.SaveDiaryEntry(diaryEntry);
        Toast.makeText(ActivityDiaryEntry.this, R.string.Tagebucheintraggespeichert , Toast.LENGTH_SHORT).show();

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
     * This method receives the exerciseList of the previous activity (diaryEntry)
     * @return
     */
    private ArrayList<Exercise> receiveExerciseList(){

        ArrayList<Exercise> result;
        //get the bundle from the intent
        final Bundle extra = getIntent().getExtras();

        //if there are any extras, unpack it
        if (extra != null) {
            result = extra.getParcelableArrayList("oldExercises");
            //set flag for saving data later
            newData = false;
            return result;
        }
        else //else get the current exerciseList of the object
            return result = diaryEntry.getExerciseList();
    }

    /**
     * This method returns the prepared exerciseList to the previous activity (diaryEntry)
     */
    private void returnResult(){
        //create intent
        Intent intent = new Intent();
        //package the prepared list
        intent.putParcelableArrayListExtra("newExercises", exerciseList);
        setResult(RESULT_OK, intent);
        //close the current activity
        finish();
    }

    /**
     * this method send the exerciseList to the category Activity.
     * Furthermore this method sends a request to receive the prepared exerciseList
     */
    public void sendOldAndRequestNewExerciseList(){
        ArrayList<Exercise> oldList = exerciseList;
        //set goal activity
        Intent pickExerciseIntent = new Intent(this, ActivitySelectedExercises.class);
        //List in package
        pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        //send package and open the goal activity
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

    /**
     * This method calculates the total points of all categories
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

}
