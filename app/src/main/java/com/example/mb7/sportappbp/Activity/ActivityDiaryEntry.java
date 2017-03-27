package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.DiaryEntryViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.StimmungsAngabe;
import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.BusinessLayer.DiaryEntry;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.R;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityDiaryEntry extends AppCompatActivity {

    //request id for the activitiy request
    final static int REQUEST_ID = 1;

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

        //todo make diary Entry parceable
        diaryEntry = new DiaryEntry();

        // Now read the extra key - exerciseList
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if(extras!=null ) {

            //Get unpack the exerciseList und date
            exerciseList = extras.getParcelableArrayList("oldExercises");
            date = (Date) extras.getSerializable("date");
            //set attribute
            diaryEntry.setExerciseList(exerciseList);
            diaryEntry.setDate(date);
        }
        else{

            //Set attribute
            diaryEntry.setDate(calendar.getTime());
            exerciseList = diaryEntry.getExerciseList();

        }



        //Create Firebase reference
        mRootRef = new Firebase("https://sportapp-cbd6b.firebaseio.com/users");

        //allDiaryEntries = AllDiaryEntries.getInstance();





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
        inflater.inflate(R.menu.menu_add_and_save , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was pressed in the toolbar
        switch (item.getItemId()){
            case R.id.icon_add:
                //send current list to the next activity and open it (categories)
                //sendOldAndRequestNewExerciseList();
                openSelectedExercises();
                finish();

                return true;
            case R.id.icon_save:
                //calculate the totalpoints and save it
                diaryEntry.setTotalPoints(calculateTotalPoints());

                //check if any exercises have been added to the list
                if(diaryEntry.getExerciseList().size() > 0) {
                    //save data to firebase
                    SaveData();
                    //allDiaryEntries.getDiaryList().add(diaryEntry);
                }

                else//Say that there was nothing to save
                    Toast.makeText(ActivityDiaryEntry.this, R.string.keintagebucheintragerstellt , Toast.LENGTH_SHORT).show();

                finish();
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
     * Save diaryEntry to Firebase
     * @return
     */
    private boolean SaveData(){

        //ActivityMain.mainUser.GetLastTodayDiaryEntry(new Date());
        ActivityMain.getMainUser(this).SaveDiaryEntry(diaryEntry);
        Toast.makeText(ActivityDiaryEntry.this, R.string.Tagebucheintraggespeichert , Toast.LENGTH_SHORT).show();

        return true;
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

    private void openSelectedExercises(){

            Intent open = new Intent(ActivityDiaryEntry.this , ActivitySelectedExercises.class);

            // pass the clicked diaryEntry to the activity
            open.putParcelableArrayListExtra("oldExercises", exerciseList);
            open.putExtra("date", date);
            open.putExtra("prepare", true);
            startActivity(open);

    }

}
