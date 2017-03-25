package com.example.mb7.sportappbp.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.mb7.sportappbp.Adapters.ExerciseViewAdapter;
import com.example.mb7.sportappbp.BusinessLayer.Exercise;
import com.example.mb7.sportappbp.BusinessLayer.LeistungstestsExercise;
import com.example.mb7.sportappbp.BusinessLayer.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.BusinessLayer.TrainingExercise;
import com.example.mb7.sportappbp.BusinessLayer.WellnessExercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

public class ActivityExercises extends AppCompatActivity {


    private ListView listviewExercises;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Exercise> exerciseList;

    private ListView listViewSelected;
    private ExerciseViewAdapter exerciseViewAdapter;

    int listViewExerciseList;

    //private String chosenExercise;
    private int selectedCategory;

    private boolean finalResult = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        //get the selected category of the previous activity (categories)
        selectedCategory = receiveCategory();
        //get the already selected exercises
        exerciseList = receiveExerciseList();

        //Set title of the label
        setTitle(selectedCategory);
        //get the list of the activity for the selected category
        listViewExerciseList = getListOfActivities(selectedCategory);

        //get the listviewExercises and set the adapter for the list of exercises
        listviewExercises = (ListView) findViewById(R.id.listviewExercises);
        arrayAdapter = new ArrayAdapter<String>(ActivityExercises.this, android.R.layout.simple_list_item_1,getResources().getStringArray(listViewExerciseList));
        listviewExercises.setAdapter(arrayAdapter);

        //get the listviewExercises and set the adapter for the select4ed exercises
        listViewSelected = (ListView) findViewById(R.id.listviewExercisesSelected);
        exerciseViewAdapter = new ExerciseViewAdapter(ActivityExercises.this, exerciseList);
        listViewSelected.setAdapter(exerciseViewAdapter);

        //set action when an exercise has been selected
        listviewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String selectedExercise = (String) adapterView.getItemAtPosition(position);


                Exercise exercise = createSelectedCategoryExercise();

                if(exercise != null) {
                    exercise.setName(selectedExercise);
                    exercise.setTimeHours(00);
                    exercise.setTimeMinutes(00);

                    numberPicker(exercise);
                }
                else
                    Toast.makeText(ActivityExercises.this, R.string.KategorieNeuWählen , Toast.LENGTH_SHORT).show();


            }
        });

        registerForContextMenu(listViewSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set the menu with a save icon
        inflater.inflate(R.menu.menu_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        //check which icon was hidden in the toolbar
        switch (item.getItemId()){
            case android.R.id.home:
                //save the selected items and send them to the previous activity
                //returnResult();
                //close the activity
                finish();
                return true;
            case R.id.icon_save:
                finalResult = true;
                returnFinalResult();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.delete_id:
                exerciseList.remove(info.position);
                exerciseViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.edit_id:
                numberPicker(exerciseList.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private int receiveCategory(){
        int category = 0;

        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            category = extra.getInt("category");
        }
        return category;
    }

    private ArrayList<Exercise> receiveExerciseList(){

        ArrayList<Exercise> result;
        final Bundle extra = getIntent().getExtras();

        if (extra != null) {
            result = extra.getParcelableArrayList("oldExercises");
            return result;
        }
        else
            return result = new ArrayList<Exercise>();
    }

    /**
     * This method returns the result of the request. The result is the chosen activity
     */
    private void returnResult(Exercise result){

        if(result != null) {

            exerciseList.add(result);

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("newExercises", exerciseList);
            intent.putExtra("finalResult", finalResult);
            setResult(RESULT_OK, intent);
            exerciseViewAdapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(ActivityExercises.this, R.string.KategorieWurdNichtRichtigGewählt, Toast.LENGTH_LONG).show();

    }

    private void returnFinalResult(){

        if(exerciseList != null) {

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("newExercises", exerciseList);
            intent.putExtra("finalResult", finalResult);
            setResult(RESULT_OK, intent);
            exerciseViewAdapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(ActivityExercises.this, R.string.KategorieWurdNichtRichtigGewählt, Toast.LENGTH_LONG).show();

    }


    private Exercise createSelectedCategoryExercise(){

        if(selectedCategory == R.string.Leistungstests){
            return new LeistungstestsExercise();
        }
        else if(selectedCategory == (R.string.Training)){
            return new TrainingExercise();
        }
        else if(selectedCategory == (R.string.Wellness)){
            return new WellnessExercise();
        }
        else if(selectedCategory == (R.string.ReinerAufenthalt)){
            return new ReinerAufenthaltExercise();
        }
        else return null;

    }


    /**
     * Creates a dialog window with two number pickers for hours and minutes. When the ok button
     * will be pressed, the data will be saved. The cancel button is going to close the dialog
     * window
     */
    private void numberPicker(final Exercise exercise){

        //create dialog window
        final Dialog dialog = new Dialog(ActivityExercises.this);

        //set the layout for the dialog window
        dialog.setContentView(R.layout.dialog_two_numberpicker);


        final String[] nums = new String[30];
        for(int i=0; i<nums.length; i++) {
            nums[i] = Integer.toString(i);
        }

        //create the number picker for hours und set the possible values
        final NumberPicker npHoures = (NumberPicker)dialog.findViewById(R.id.numberPickerHours);
        npHoures.setMaxValue(24);
        npHoures.setMinValue(0);
        npHoures.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        npHoures.setValue(exercise.getTimeHours());

        //create the number picker for minutes und set the possible values
        final NumberPicker npMinutes = (NumberPicker)dialog.findViewById(R.id.numberPickerMinutes);
        npMinutes.setMaxValue(59);
        npMinutes.setMinValue(0);
        npMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        npMinutes.setValue(exercise.getTimeMunites());

        //set the action for ok button
        Button btnOk = (Button)dialog.findViewById(R.id.npOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int m = npMinutes.getValue();
                int h = npHoures.getValue();

                if(m != 0 || h != 0) {
                    //save the picked numbers
                    exerciseList.remove(exercise);
                    exercise.setTimeHours(h);
                    exercise.setTimeMinutes(m);
                    dialog.dismiss();
                    returnResult(exercise);
                }
                else
                    Toast.makeText(ActivityExercises.this, R.string.ungültigeZeit , Toast.LENGTH_SHORT).show();
            }
        });

        //set the action for cancel button
        Button btnCancel = (Button) dialog.findViewById(R.id.npCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exerciseList.add(exercise);
                //close the dialog windows without doing anything
                dialog.dismiss();
            }
        });

        //show the dialog window
        dialog.show();

    }


    private int getListOfActivities(int category) {

        ArrayList<String> result = new ArrayList<>();

        switch (category) {
            case R.string.Leistungstests:
                return R.array.ArrayLeistungstests;

            case R.string.Training:
                return R.array.ArrayTraining;

            case R.string.Wellness:
                return R.array.ArrayWellness;

            case R.string.ReinerAufenthalt:
                return R.array.ArrayReinerAufenthalt;
            default:
                return 0;


        }
    }
}