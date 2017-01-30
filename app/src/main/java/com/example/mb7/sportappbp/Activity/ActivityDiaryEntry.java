package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.mb7.sportappbp.Objects.AllDiaryEntries;
import com.example.mb7.sportappbp.Objects.DiaryEntry;
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityDiaryEntry extends AppCompatActivity {

    //request id for the activitiy request
    static final int REQUEST_ID = 1;

    private Button btnExerciseLst;
    private Button btnSave;
    private Button btnAdd;

    //private RadioGroup radioGroup;
    //private RadioButton radioButton;

    private DiaryEntry diaryEntry;
    private AllDiaryEntries allDiaryEntries;

    //private ListView listView;
    //private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Exercise> exerciseList;

    //private TextView totalDuration;

    private String activity;
    private Menu menu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryentry);

        diaryEntry = new DiaryEntry(getCurrentDate());
        exerciseList = diaryEntry.getExerciseList();

        //radioGroup = (RadioGroup) findViewById(R.id.RadioGroupTraining);

        //totalDuration = (TextView)findViewById(R.id.textViewTotalTime);
        //totalDuration.setText(String.valueOf(diaryEntry.getTotalDuration()));

        //activate the back button on the toolbar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //listOfActivities = new ArrayList<String>();

        //listView = (ListView) findViewById(R.id.diaryentryListView);
        //arrayAdapter = new ArrayAdapter<String>(DiaryEntryActivity.this, android.R.layout.simple_list_item_1, listOfActivities);
        //listView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
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

        /*
        int selectedID = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedID);

        //check if a button is selected, if not give a message to the user
        if (radioButton == null)
            Toast.makeText(DiaryEntryActivity.this, "Button nicht gewaehlt!", Toast.LENGTH_SHORT).show();
        else {

            //Create the diary object with the input of the user
            diaryEntry.setSuccessful(radioButtonYesOrNo(radioButton));
            diaryEntry.setExerciseList(exerciseList);
            //add the entry to the diary list
            allDiaryEntries.getInstance().getDiaryList().add(diaryEntry);

            //todo Save object to the database

            //Display answer
            //Toast.makeText(DiaryEntryActivity.this, strDate, Toast.LENGTH_SHORT).show();

            finish();

        }
        */
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

            //Toast.makeText(DiaryEntryActivity.this, diaryEntry.getExerciseList().get(0).getActivity(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method evaluates the radio button of a radio button group, if the user selected yes or no.
     * When the text of the button called "Ja" the method returns true, if it's "Nein" it returns false.
     * @param radioBtn the selected radio button of a radio button group
     * @return true or false
     */
    private boolean radioButtonYesOrNo(RadioButton radioBtn){
        if(radioBtn.getText() == "Ja")
            return true;
        else
            return false;
    }

    /**
     * This method returns the current date as a string in the format "dd.MM.yy".
     * @return a String with the date "dd.MM.yy"
     */
    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String strDate = sdf.format(c.getTime());
        return strDate;
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
