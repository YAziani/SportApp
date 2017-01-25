package com.example.mb7.sportappbp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mb7.sportappbp.MotivationMethods.TrainingReminder;

/**
 * activity used to get the initial training times of the user
 * Created by Aziani on 17.01.2017.
 */
public class SettingInitializerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView weekdayListView;
    ListView timeListView;
    ArrayAdapter<String> weekdayArrayAdapter;
    ArrayAdapter<String> timeArrayAdapter;
    int userChoiceIndex;
    String[] textArray;
    String[] inputArray;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TrainingReminder trainingReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_initializer);

        // setup trainingReminder for studio address comparison
        trainingReminder = new TrainingReminder(this);

        // setup preferences file
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        editor = sharedPreferences.edit();

        // setup arrays with the displayed values
        textArray = new String[]{
                "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag", "Studioadresse"};
        inputArray = new String[]{"", "", "", "", "", "", "", ""};

        // fill inputArray with values from the preferences
        String preferenceString;
        for(int i = 0; i < textArray.length; i++) {
            preferenceString = sharedPreferences.getString(textArray[i], null);
            if(preferenceString == null) {
                inputArray[i] = "";
            }else {
                inputArray[i] = preferenceString;
            }
        }

        // setup adapter to fill the listviews with
        weekdayArrayAdapter = new ArrayAdapter<>(
                SettingInitializerActivity.this,
                android.R.layout.simple_list_item_1,
                textArray
        );
        weekdayListView = (ListView)findViewById(R.id.weekdayListView);
        weekdayListView.setAdapter(weekdayArrayAdapter);
        weekdayListView.setOnItemClickListener(this);

        timeArrayAdapter = new ArrayAdapter<>(
                SettingInitializerActivity.this,
                android.R.layout.simple_list_item_1,
                inputArray
        );
        timeListView = (ListView)findViewById(R.id.timeListView);
        timeListView.setAdapter(timeArrayAdapter);
        timeListView.setOnItemClickListener(this);

        // button to complete initialization and end the activity
        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getBoolean("initialized",false)) {
                    // close the activity
                    finish();
                }else {
                    // show dialog for choosing means of transportation
                    DialogFragment radioButtonFragment = new RadioButtonFragment();
                    radioButtonFragment.show(getFragmentManager(), "radiobutton");

                    // set initialized flag
                    editor.putBoolean("initialized", true);
                    editor.commit();
                }
            }
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // save the index for the preferences file
        userChoiceIndex = position;
        if(userChoiceIndex < 7) {
            // create a new time picker and show it
            DialogFragment pickerFragment = new TimePickerFragment();
            pickerFragment.show(getFragmentManager(),"timepicker");
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tragen Sie die Adresse Ihres Fitnessstudios ein.");
            builder.setCancelable(true);
            // set up the input
            final EditText input = new EditText(this);
            // specify the type of input
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            // set up buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(input.getText() != null) {
                        setStudioAddress(input.getText().toString());
                    }
                }
            });
            builder.setNegativeButton("Löschen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setStudioAddress("");
                }
            });
            builder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    /**
     * sets the studio address in the shared preferences
     * @param address entered address
     */
    public void setStudioAddress(String address) {
        // if dialog was canceled
        if(address == null || address.equals("")) {
            // remove previously inserted address
            inputArray[userChoiceIndex] = "";
            editor.remove(textArray[userChoiceIndex]);
        }else {
            // determine next fitting address and put it into the displayed array and into preferences
            address = trainingReminder.compareStudioPosition(address);
            if(address != null) {
                inputArray[userChoiceIndex] = address;
                editor.putString(textArray[userChoiceIndex], address);
            }else {
                inputArray[userChoiceIndex] = "";
                editor.remove(textArray[userChoiceIndex]);
            }
        }
        // commit changes to the preferences file
        editor.commit();
        // display the results
        timeListView.setAdapter(timeArrayAdapter);
    }

    /**
     * sets the training time in the shared preferences
     * @param hourOfDay entered hour
     * @param minute entered minute
     */
    public void setTime(int hourOfDay, int minute) {
        String displayedHour = String.valueOf(hourOfDay);
        String displayedMinute = String.valueOf(minute);

        // insert a 0 if necessary
        if(hourOfDay < 10) {
            displayedHour = "0" + String.valueOf(hourOfDay);
        }
        if(minute < 10) {
            displayedMinute = "0" + String.valueOf(minute);
        }

        // if time picker was canceled
        if(hourOfDay == 999) {
            // remove the previously inserted time from the preferences and the displayed array
            inputArray[userChoiceIndex] = "";
            editor.remove(textArray[userChoiceIndex]);
        }else {
            // save the inserted time in the preferences and in the displayed array
            inputArray[userChoiceIndex] = displayedHour + ":" + displayedMinute;
            editor.putString(textArray[userChoiceIndex], inputArray[userChoiceIndex]);
        }
        // commit changes to the preferences file
        editor.commit();
        // display the results
        timeListView.setAdapter(timeArrayAdapter);
    }
}
