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
import com.example.mb7.sportappbp.Objects.Exercise;
import com.example.mb7.sportappbp.Objects.LeistungstestsExercise;
import com.example.mb7.sportappbp.Objects.ReinerAufenthaltExercise;
import com.example.mb7.sportappbp.Objects.TrainingExercise;
import com.example.mb7.sportappbp.Objects.WellnessExercise;
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

public class ActivityExercises extends AppCompatActivity {


    private ListView listviewExercises;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> exLst;
    private ArrayList<Exercise> exerciseList;

    private ListView listViewSelected;
    private ExerciseViewAdapter exerciseViewAdapter;

    private int timeMinutes;
    private int timeHours;

    //private String chosenExercise;
    private String selectedCategory;

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
        exLst = getListOfActivities(selectedCategory);

        //get the listviewExercises and set the adapter for the list of exercises
        listviewExercises = (ListView) findViewById(R.id.listviewExercises);
        arrayAdapter = new ArrayAdapter<String>(ActivityExercises.this, android.R.layout.simple_list_item_1, exLst);
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

                exercise.setName(selectedExercise);
                exercise.setTimeHours(00);
                exercise.setTimeMinutes(00);

                numberPicker(exercise);


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


    private String receiveCategory(){
        String category = "";

        final Bundle extra = getIntent().getExtras();
        if (extra != null) {
            category = extra.getString("category");
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
            Toast.makeText(ActivityExercises.this, "Kategorie wurde nicht richtig gewählt!", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(ActivityExercises.this, "Kategorie wurde nicht richtig gewählt!", Toast.LENGTH_SHORT).show();

    }


    private Exercise createSelectedCategoryExercise(){

        if(selectedCategory.equals("Leistungstests")){
            return new LeistungstestsExercise();
        }
        else if(selectedCategory.equals("Training")){
            return new TrainingExercise();
        }
        else if(selectedCategory.equals("Wellness")){
            return new WellnessExercise();
        }
        else if(selectedCategory.equals("Reiner Aufenthalt")){
            return new ReinerAufenthaltExercise();
        }//todo overthink else case
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
                    Toast.makeText(ActivityExercises.this, "Es wurde keine Zeit gesetzt!", Toast.LENGTH_SHORT).show();
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


    private ArrayList getListOfActivities(String category){

        ArrayList<String> result = new ArrayList<>();

        switch(category){
            case "Leistungstests":
                result.add("Spiroergometrie");
                result.add("Laktattest");
                result.add("Beweglichkeitstest");
                result.add("Krafttest");
                result.add("Anderer Leistungstest");
                return result;

            case "Training":
                result.add("Krafttraining");
                result.add("Laufen");
                result.add("Cycling");
                result.add("Beweglichkeit/Flexibilität");
                result.add("Yoga");
                result.add("Rückentraining");
                result.add("Progressive");
                result.add("Muskelentspannung");
                result.add("Autogenes Training");
                result.add("Meditation");
                result.add("Anderes Training");

                return result;

            case "Wellness":
                result.add("Sauna");
                result.add("Dampfbad");
                result.add("Massage");
                result.add("Solarium");
                result.add("Andere Wellnessaktivität");
                return result;

            case "Reiner Aufenthalt":
                result.add("Soziale Kontakte");
                result.add("Bistro");
                result.add("Andere");
                return result;
            default:
                result.add("Kategorie falsch ausgeählt");
                return result;
        }

    }
}