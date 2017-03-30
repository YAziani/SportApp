package com.tud.bp.fitup.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.tud.bp.fitup.Adapters.ExerciseViewAdapter;
import com.tud.bp.fitup.BusinessLayer.Exercise;
import com.tud.bp.fitup.BusinessLayer.LeistungstestsExercise;
import com.tud.bp.fitup.BusinessLayer.ReinerAufenthaltExercise;
import com.tud.bp.fitup.BusinessLayer.TrainingExercise;
import com.tud.bp.fitup.BusinessLayer.WellnessExercise;
import com.tud.bp.fitup.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class to show all exercises
 * Created by Sebastian on 24.01.2017.
 */

public class ActivityExercises extends AppCompatActivity {


    private ListView listviewExercises;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Exercise> exerciseList;
    private ListView listViewSelected;
    private ExerciseViewAdapter exerciseViewAdapter;
    int listViewExerciseList;
    private int selectedCategory;
    private boolean finalResult = false;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        // Now receive and read data without a request - from ActivityCategories
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate","We have reached it");
        if (extras != null) {

            //Get unpack the exerciseList, selected exercise and date
            exerciseList = extras.getParcelableArrayList("oldExercises");
            date = (Date) extras.getSerializable("date");
            selectedCategory = extras.getInt("category");

        }
        else{

            exerciseList = new ArrayList<Exercise>();
            selectedCategory = 0;

        }

        //Set title of the activity
        setTitle(selectedCategory);

        //set the list of exercises of the selected category
        listViewExerciseList = getListOfExercises(selectedCategory);

        //create a listview to show all exercises, which are available
        listviewExercises = (ListView) findViewById(R.id.listviewExercises);
        arrayAdapter = new ArrayAdapter<String>(ActivityExercises.this, android.R.layout.simple_list_item_1,getResources().getStringArray(listViewExerciseList));
        listviewExercises.setAdapter(arrayAdapter);

        //get the listviewExercises and set the adapter for the select4ed exercises
        listViewSelected = (ListView) findViewById(R.id.listviewExercisesSelected);
        exerciseViewAdapter = new ExerciseViewAdapter(ActivityExercises.this, exerciseList);
        listViewSelected.setAdapter(exerciseViewAdapter);

        //set action when an exercise has been selected to open a number pocker
        listviewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String selectedExercise = (String) adapterView.getItemAtPosition(position);

                //create an object of the selected category
                Exercise exercise = createSelectedCategoryExercise();

                if(exercise != null) {
                    exercise.setName(selectedExercise);
                    exercise.setTimeHours(00);
                    exercise.setTimeMinutes(00);

                    numberPicker(exercise);
                }
                else
                    Toast.makeText(ActivityExercises.this, R.string.KategorieNeuWaehlen, Toast.LENGTH_SHORT).show();


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
            case android.R.id.home: //go back to categories
                openActivityWithExtra(ActivityCategories.class);
                finish();
                return true;
            case R.id.icon_save: // go back to diarEntry
                finalResult = true;

                openActivityWithExtra(ActivityDiaryEntry.class);
                finish();
                return true;

            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //set the menu with add and save icon
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.delete_id: //remove the pressed exercise
                exerciseList.remove(info.position);
                exerciseViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.edit_id: //open numberpicker to cange the duration
                numberPicker(exerciseList.get(info.position));
                return true;
            default:
                throw new InvalidParameterException("The context menu items is not declared");
        }
    }

    private Exercise createSelectedCategoryExercise() {

        if (selectedCategory == R.string.Leistungstests) {
            return new LeistungstestsExercise();
        } else if (selectedCategory == (R.string.Training)) {
            return new TrainingExercise();
        } else if (selectedCategory == (R.string.Wellness)) {
            return new WellnessExercise();
        } else if (selectedCategory == (R.string.ReinerAufenthalt)) {
            return new ReinerAufenthaltExercise();
        } else
            throw new InvalidParameterException("The category items is not declared");


    }


    /**
     * This method opens a dialog window with two number picker and includes the action for the positive and negative
     * butten. When the positive one was pressed, the time will be saved to the diaryEntrz object. Else the window
     * will be closed.
     * @param exercise the pressed exercise to set the duration
     */
    private void numberPicker(final Exercise exercise) {

        //create dialog window
        final Dialog dialog = new Dialog(ActivityExercises.this);

        //set the layout for the dialog window
        dialog.setContentView(R.layout.dialog_two_numberpicker);


        final String[] nums = new String[30];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = Integer.toString(i);
        }

        //create the number picker for hours und set the possible values
        final NumberPicker npHoures = (NumberPicker) dialog.findViewById(R.id.numberPickerHours);
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
        final NumberPicker npMinutes = (NumberPicker) dialog.findViewById(R.id.numberPickerMinutes);
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
        Button btnOk = (Button) dialog.findViewById(R.id.npOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int m = npMinutes.getValue();
                int h = npHoures.getValue();

                if (m != 0 || h != 0) {
                    //save the picked numbers
                    exerciseList.remove(exercise);
                    exercise.setTimeHours(h);
                    exercise.setTimeMinutes(m);
                    dialog.dismiss();
                } else
                    Toast.makeText(ActivityExercises.this, R.string.ungueltigeZeit, Toast.LENGTH_SHORT).show();
            }
        });

        //set the action for cancel button
        Button btnCancel = (Button) dialog.findViewById(R.id.npCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the dialog windows without doing anything
                dialog.dismiss();
            }
        });

        //show the dialog window
        dialog.show();

    }

    /**
     * This method opens the entered activity with an information, which category was selected by the user ,
     * the date of the entry and the current list
     * @param destinationClass the class to open next
     */
    private void openActivityWithExtra(Class destinationClass) {

        Intent open = new Intent(ActivityExercises.this, destinationClass);
        open.putParcelableArrayListExtra("oldExercises", exerciseList);
        open.putExtra("date", date);
        startActivity(open);

    }


    /**
     * Resturns an arraylist with all exercises of a category
     * @param category
     * @return
     */
    private int getListOfExercises(int category) {

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
                throw new InvalidParameterException("The menu items is not declared");


        }
    }
}