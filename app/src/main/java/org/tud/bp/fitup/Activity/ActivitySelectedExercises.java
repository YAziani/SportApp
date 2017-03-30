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
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;


import com.tud.bp.fitup.Adapters.ExerciseViewAdapter;
import com.tud.bp.fitup.BusinessLayer.Exercise;
import com.tud.bp.fitup.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;


public class ActivitySelectedExercises extends AppCompatActivity {

    final static int REQUEST_ID = 2;

    ListView listView;
    ExerciseViewAdapter exerciseViewAdapter;
    ArrayList<Exercise> exerciseList;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedexercises);


        // Now read the extra key - exerciseList
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();
        Log.e("Oncreate", "We have reached it");
        if (extras != null) {

            //Get unpack the exerciseList und date
            exerciseList = extras.getParcelableArrayList("oldExercises");
            date = (Date) extras.getSerializable("date");
        } else {

            exerciseList = new ArrayList<Exercise>();

        }

        //create listview to thow all selected exercieses
        listView = (ListView) findViewById(R.id.listviewActivityOverview);
        exerciseViewAdapter = new ExerciseViewAdapter(ActivitySelectedExercises.this, exerciseList);
        listView.setAdapter(exerciseViewAdapter);


        registerForContextMenu(listView);

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

            case R.id.delete_id: //remove item of the list
                exerciseList.remove(info.position);
                exerciseViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.edit_id: //open a number picker to change the duration
                numberPicker(exerciseList.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //set an other menu xml
        inflater.inflate(R.menu.menu_add_and_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //check which icon was hidden in the toolbar
        switch (item.getItemId()) {
            case R.id.icon_add:
                //sendOldAndRequestNewExerciseList(exerciseList);
                openActivityWithExtra(ActivityCategories.class);
                finish();
                return true;
            case R.id.icon_save:
                openActivityWithExtra(ActivityDiaryEntry.class);
                finish();
                //returnResult();
                return true;

            default:
                throw new InvalidParameterException("The category items is not declared");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_ID) {
            ArrayList<Exercise> result = data.getParcelableArrayListExtra("newExercises");

            setNewList(exerciseList, result);
            exerciseViewAdapter.notifyDataSetChanged();
        }

    }

    private ArrayList<Exercise> receiveExerciseList() {

        ArrayList<Exercise> result;
        final Bundle extra = getIntent().getExtras();

        if (extra != null) {
            result = extra.getParcelableArrayList("oldExercises");
            return result;
        } else
            return result = new ArrayList<Exercise>();
    }

    private void sendOldAndRequestNewExerciseList(ArrayList<Exercise> oldList) {
        Intent pickExerciseIntent = new Intent(this, ActivityCategories.class);
        //Optional, wenn bereits ausgewehlte makiert werden sollen
        pickExerciseIntent.putParcelableArrayListExtra("oldExercises", oldList);
        startActivityForResult(pickExerciseIntent, REQUEST_ID);
    }

    private void setNewList(ArrayList<Exercise> oldLst, ArrayList<Exercise> newLst) {

        oldLst.clear();

        for (Exercise i : newLst)
            oldLst.add(i);
    }

    private void returnResult() {

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("newExercises", exerciseList);
        setResult(RESULT_OK, intent);
        finish();

    }

    private void numberPicker(final Exercise exercise) {

        //create dialog window
        final Dialog dialog = new Dialog(ActivitySelectedExercises.this);

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
                    //check if the user entered a number save the picked numbers
                    exercise.setTimeHours(h);
                    exercise.setTimeMinutes(m);
                    dialog.dismiss();
                    exerciseViewAdapter.notifyDataSetChanged();

                } else
                    Toast.makeText(ActivitySelectedExercises.this, R.string.ungueltigeZeit, Toast.LENGTH_SHORT).show();
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
     * This method opens the entered activity with the date and the exercise list of an exercise of the entry
     * @param destinationClass
     */
    private void openActivityWithExtra(Class destinationClass) {


        Intent open = new Intent(ActivitySelectedExercises.this, destinationClass);
        open.putParcelableArrayListExtra("oldExercises", exerciseList);
        open.putExtra("date", date);
        startActivity(open);

    }

}
