package com.example.mb7.sportappbp.Activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
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
import com.example.mb7.sportappbp.R;

import java.util.ArrayList;

public class ActivityCategories extends AppCompatActivity {
    //request id for the activitiy request
    final static int REQUEST_ID = 3;

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String[] listOfCategories = {"Leistungstests", "Training", "Wellness", "Reiner Aufenthalt"};

    ListView listViewSelected;
    ExerciseViewAdapter exerciseViewAdapter;

    ArrayList<Exercise> exerciseList;
    String activity;

    private Boolean finalResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ActionBar actionbar = getActionBar();
        if(actionbar != null){
            actionbar.setHomeButtonEnabled(false);
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setDisplayShowHomeEnabled(false);
        }

        exerciseList = receiveExerciseList();

        listView = (ListView) findViewById(R.id.listviewCategories);
        arrayAdapter = new ArrayAdapter<String>(ActivityCategories.this, android.R.layout.simple_list_item_1, listOfCategories);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String category = (String) adapterView.getItemAtPosition(position);

                switch (category){
                    case "Leistungstests":
                        forwardOldExerciseList(category, exerciseList);
                        break;
                    case "Training":
                        forwardOldExerciseList(category, exerciseList);
                        break;
                    case "Wellness":
                        forwardOldExerciseList(category, exerciseList);
                        break;
                    case "Reiner Aufenthalt":
                        forwardOldExerciseList(category, exerciseList);
                        break;
                    default:

                }

            }
        });

        listViewSelected = (ListView) findViewById(R.id.listviewCategoriesSelected);
        exerciseViewAdapter = new ExerciseViewAdapter(ActivityCategories.this, exerciseList);
        listViewSelected.setAdapter(exerciseViewAdapter);

        registerForContextMenu(listViewSelected);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ID){
            ArrayList<Exercise> result = data.getParcelableArrayListExtra("newExercises");
            finalResult = data.getBooleanExtra("finalResult", false);

            if(finalResult)
                forwardResult(result);
            else {
                setNewList(exerciseList, result);
                exerciseViewAdapter.notifyDataSetChanged();
            }


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

    /**
     * This method forwards the result of the request
     * @param result
     */
    private void forwardResult(ArrayList<Exercise> result){

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("newExercises",result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void forwardOldExerciseList(String category, ArrayList<Exercise> exerciseList){

        Intent intent = new Intent(ActivityCategories.this, ActivityExercises.class);
        intent.putExtra("category", category);
        intent.putParcelableArrayListExtra("oldExercises", exerciseList);
        startActivityForResult(intent, REQUEST_ID);
    }

    private void receiveSerializable(){
        Exercise testExercise;
        ArrayList<Exercise> testList = new ArrayList<>();

        testList = getIntent().getParcelableArrayListExtra("category");

        double test = testList.get(0).getWeighting();


        Toast.makeText(ActivityCategories.this, String.valueOf(test), Toast.LENGTH_SHORT).show();


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

    private void setNewList(ArrayList<Exercise> oldLst, ArrayList<Exercise> newLst){

        Toast.makeText(ActivityCategories.this, String.valueOf(newLst.size()) , Toast.LENGTH_SHORT).show();

        //To clear the already selected items (no duplicates)
        oldLst.clear();

        for(Exercise i : newLst)
            oldLst.add(i);
    }

    private void numberPicker(final Exercise exercise){

        //create dialog window
        final Dialog dialog = new Dialog(ActivityCategories.this);

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
                    exercise.setTimeHours(h);
                    exercise.setTimeMinutes(m);
                    dialog.dismiss();
                    exerciseViewAdapter.notifyDataSetChanged();

                }
                else
                    Toast.makeText(ActivityCategories.this, "Es wurde keine Zeit gesetzt!", Toast.LENGTH_SHORT).show();
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
}
