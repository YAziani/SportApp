package com.tud.bp.fitup.Activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.tud.bp.fitup.Adapters.ExerciseViewAdapter;
import com.tud.bp.fitup.BusinessLayer.Exercise;
import com.tud.bp.fitup.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This shows all exercises
 * Created by Sebastain
 */

public class ActivityCategories extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ListView listViewSelected;
    ExerciseViewAdapter exerciseViewAdapter;
    ArrayList<Exercise> exerciseList;
    Calendar calendar = Calendar.getInstance();
    Date date;
    String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        // Now receive and read data without a request - from ActivityDiaryEntry
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {

            //Get unpack the exerciseList und date
            exerciseList = extras.getParcelableArrayList("oldExercises");
            date = (Date) extras.getSerializable("date");
        } else {

            exerciseList = new ArrayList<Exercise>();
            date = calendar.getTime();

        }


        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(false);
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setDisplayShowHomeEnabled(false);
        }


        //Create a gridview with an adapter to show all categories in a list
        listView = (ListView) findViewById(R.id.listviewCategories);
        arrayAdapter = new ArrayAdapter<String>(ActivityCategories.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.ArrayCategories));
        listView.setAdapter(arrayAdapter);

        //set the action when a category was selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //open the next activity with the current exercise list of the selected category
                String category = (String) adapterView.getItemAtPosition(position);
                switch (category) {
                    case "Leistungstests":
                        forwardExerciseList(R.string.Leistungstests);
                        break;
                    case "Training":
                        forwardExerciseList(R.string.Training);
                        break;
                    case "Wellness":
                        forwardExerciseList(R.string.Wellness);
                        break;
                    case "Reiner Aufenthalt":
                        forwardExerciseList(R.string.ReinerAufenthalt);
                        break;
                    default:
                        throw new InvalidParameterException("The category items is not declared");

                }

            }
        });

        //create a listview to show all selected exercises
        listViewSelected = (ListView) findViewById(R.id.listviewCategoriesSelected);
        exerciseViewAdapter = new ExerciseViewAdapter(ActivityCategories.this, exerciseList);
        listViewSelected.setAdapter(exerciseViewAdapter);

        registerForContextMenu(listViewSelected);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        //set the menu with add and save icon
        inflater.inflate(R.menu.menu_context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //check which icon was pressed in the toolbar
        switch (item.getItemId()) {
            case R.id.delete_id: //remove the pressed item and update the listview
                exerciseList.remove(info.position);
                exerciseViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.edit_id: //open a dialog with a number picker to change the time
                numberPicker(exerciseList.get(info.position));
                return true;
            default:
                throw new InvalidParameterException("The menu items is not declared");
        }
    }

    /**
     * This method opens the activity exercises with an information, which category was selected by the user ,
     * the date of the entry and the current list
     * @param category the pressed category
     */
    private void forwardExerciseList(int category) {

        Intent open = new Intent(ActivityCategories.this, ActivityExercises.class);
        open.putParcelableArrayListExtra("oldExercises", exerciseList);
        open.putExtra("date", date);
        open.putExtra("category", category);
        startActivity(open);
        //close the current activity
        finish();
    }

    /**
     * This method opens a dialog window with two number picker and includes the action for the positive and negative
     * butten. When the positive one was pressed, the time will be saved to the diaryEntrz object. Else the window
     * will be closed.
     * @param exercise the pressed exercise to set the duration
     */
    private void numberPicker(final Exercise exercise) {

        //create dialog window
        final Dialog dialog = new Dialog(ActivityCategories.this);

        //set the layout for the dialog window
        dialog.setContentView(R.layout.dialog_two_numberpicker);

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

        //set the action for ok button to save the data and clos the window
        Button btnOk = (Button) dialog.findViewById(R.id.npOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int m = npMinutes.getValue();
                int h = npHoures.getValue();

                if (m != 0 || h != 0) {
                    //save the picked numbers
                    exercise.setTimeHours(h);
                    exercise.setTimeMinutes(m);
                    dialog.dismiss();
                    exerciseViewAdapter.notifyDataSetChanged();

                } else
                    Toast.makeText(ActivityCategories.this, R.string.ungueltigeZeit, Toast.LENGTH_SHORT).show();
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
