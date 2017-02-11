package com.example.mb7.sportappbp.Activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mb7.sportappbp.Fragments.RadioButtonFragment;
import com.example.mb7.sportappbp.Fragments.TimePickerFragment;
import com.example.mb7.sportappbp.MotivationMethods.TrainingReminder;
import com.example.mb7.sportappbp.R;

import java.util.Collections;
import java.util.LinkedList;

/**
 * activity used to get the initial training times of the user
 * Created by Aziani on 17.01.2017.
 */
public class ActivitySettingInitializer extends AppCompatActivity {

    int userChoiceIndex;
    int trainingDateIndex;
    String studioAddress;
    String[] textArray;
    String[] weekdayTimes;
    TextView[] textViewArr;
    ImageButton[] imgButtonArray;
    ListView[] listViewArray;
    LinkedList<LinkedList<String>> inputList;
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
        listViewArray = new ListView[7];
        inputList = new LinkedList<>();

        // fill inputList with values from the preferences
        for(int i = 0; i < 7; i++) {
            inputList.add(i,new LinkedList<String>());
            weekdayTimes = sharedPreferences.getString(textArray[i], "").split(";");
            for(int j = 0; j < weekdayTimes.length; j++) {
                if(!weekdayTimes[j].equals("")) {
                    inputList.get(i).add(j, weekdayTimes[j]);
                }
            }
        }

        studioAddress = sharedPreferences.getString(textArray[7], "");

        // image view for the location icon
        final ImageView imageViewStudio = (ImageView)findViewById(R.id.imageViewStudio);

        // array of all week day buttons
        imgButtonArray = new ImageButton[8];
        imgButtonArray[0] = (ImageButton)findViewById(R.id.imgButtonMo);
        imgButtonArray[1] = (ImageButton)findViewById(R.id.imgButtonDi);
        imgButtonArray[2] = (ImageButton)findViewById(R.id.imgButtonMi);
        imgButtonArray[3] = (ImageButton)findViewById(R.id.imgButtonDo);
        imgButtonArray[4] = (ImageButton)findViewById(R.id.imgButtonFr);
        imgButtonArray[5] = (ImageButton)findViewById(R.id.imgButtonSa);
        imgButtonArray[6] = (ImageButton)findViewById(R.id.imgButtonSo);
        imgButtonArray[7] = (ImageButton)findViewById(R.id.imgButtonStudio);

        // array of all week day textviews
        textViewArr = new TextView[8];
        textViewArr[0] = (TextView)findViewById(R.id.textViewMo);
        textViewArr[1] = (TextView)findViewById(R.id.textViewDi);
        textViewArr[2] = (TextView)findViewById(R.id.textViewMi);
        textViewArr[3] = (TextView)findViewById(R.id.textViewDo);
        textViewArr[4] = (TextView)findViewById(R.id.textViewFr);
        textViewArr[5] = (TextView)findViewById(R.id.textViewSa);
        textViewArr[6] = (TextView)findViewById(R.id.textViewSo);
        textViewArr[7] = (TextView)findViewById(R.id.textViewStudio);
        // set text view content
        SpannableString s;
        String numberOfDates;

        for(int i = 0; i < 7; i++) {

            // set text on buttons
            numberOfDates = String.valueOf(inputList.get(i).size())+ " Termin";
            if(inputList.get(i).size() != 1) {
                numberOfDates += "e";
            }
            s = new SpannableString(textArray[i].substring(0,2) +"\n"+ numberOfDates);
            s.setSpan(new StyleSpan(Typeface.BOLD),0,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(0.75f),3,s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewArr[i].setText(s);
        }
        s = new SpannableString(textArray[7] + "\n" + studioAddress);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,textArray[7].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewArr[7].setText(s);

        // set color flash and OnClickListener
        for(int i = 0; i < 8; i++) {
            final int j = i;
            // color flash
            imgButtonArray[j].setOnTouchListener(new View.OnTouchListener() {
                Rect rect;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // if clicked and hold
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        imgButtonArray[j].setColorFilter(Color.argb(100, 0, 0, 255));
                        if(j == 7){
                            imageViewStudio.getDrawable()
                                    .setColorFilter(Color.argb(100, 0, 0, 255), PorterDuff.Mode.MULTIPLY);
                        }
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    }
                    // if finger removed or moved
                    else {
                        imgButtonArray[j].clearColorFilter();
                        if(j == 7){
                            imageViewStudio.getDrawable().clearColorFilter();
                        }
                    }
                    return false;
                }
            });

            // set on click behavior
            imgButtonArray[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // save the index for the preferences file
                    userChoiceIndex = j;
                    if(userChoiceIndex < 7) {
                        // create a new time picker and show it
                        /*
                        DialogFragment pickerFragment = new TimePickerFragment();
                        pickerFragment.show(getFragmentManager(),"timepicker");
                        */

                        // display the dates
                        LinkedList<String> displayValues = new LinkedList<>(inputList.get(userChoiceIndex));
                        for(int i = 0; i < displayValues.size(); i++) {
                            displayValues.set(i,displayValues.get(i) + " Uhr");
                        }
                        // adapter to fill the list view of the weekday with
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(
                                        getApplicationContext(),
                                        android.R.layout.simple_list_item_1, 0,displayValues);

                        // setup list view for the weekday
                        listViewArray[userChoiceIndex] = new ListView(getApplicationContext());
                        listViewArray[userChoiceIndex].setAdapter(adapter);
                        listViewArray[userChoiceIndex].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // open up new timepicker for existing date
                                trainingDateIndex = position;
                                DialogFragment pickerFragment = new TimePickerFragment();
                                pickerFragment.show(getFragmentManager(),"timepicker");
                            }
                        });

                        // show list of training dates of the chosen weekday
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettingInitializer.this);
                        builder.setTitle("Trainingszeiten " + textArray[userChoiceIndex]);
                        builder.setView(listViewArray[userChoiceIndex]);
                        builder.setPositiveButton("OK",null);
                        // button for new date
                        builder.setNeutralButton("Training hinzufügen",null);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Button button = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // open up new timepicker for new date
                                        trainingDateIndex = -1;
                                        DialogFragment pickerFragment = new TimePickerFragment();
                                        pickerFragment.show(getFragmentManager(),"timepicker");
                                    }
                                });
                            }
                        });
                        alertDialog.show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettingInitializer.this);
                        builder.setTitle("Tragen Sie die Adresse Ihres Fitnessstudios ein.");
                        builder.setCancelable(true);
                        // set up the input
                        final EditText input = new EditText(getApplicationContext());
                        input.setTextColor(Color.argb(255,0,0,0));
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
            });

            // set font and text size
            textViewArr[j].setTextSize(18);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set up save button
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.icon_save:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            studioAddress = "";
            editor.remove(textArray[userChoiceIndex]);
        }else {
            // determine next fitting address and put it into the displayed array and into preferences
            address = trainingReminder.compareStudioPosition(address);
            if(address != null) {
                studioAddress = address;
                editor.putString(textArray[userChoiceIndex], address);
            }else {
                studioAddress = "";
                editor.remove(textArray[userChoiceIndex]);
            }
        }
        // commit changes to the preferences file
        editor.commit();
        // display the results
        SpannableString s = new SpannableString(textArray[userChoiceIndex] + "\n" + studioAddress);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,textArray[userChoiceIndex].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewArr[userChoiceIndex].setText(s);
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
        if(hourOfDay == 999 && trainingDateIndex != -1) {
            // remove the previously inserted time from the preferences and the displayed array
            inputList.get(userChoiceIndex).remove(trainingDateIndex);
        }else if(trainingDateIndex != -1) {
            // save the inserted time in the preferences and in the displayed array
            inputList.get(userChoiceIndex).set(trainingDateIndex,displayedHour + ":" + displayedMinute);
        }

        // add new date to list
        if(hourOfDay != 999 && trainingDateIndex == -1) {
            inputList.get(userChoiceIndex).add(displayedHour + ":" + displayedMinute);
        }

        if(!(hourOfDay == 999 && trainingDateIndex == -1)) {
            // sort list
            Collections.sort(inputList.get(userChoiceIndex));

            // concatenate all training dates
            String preferenceString = "";
            for(int i = 0; i < inputList.get(userChoiceIndex).size(); i++) {
                if(!preferenceString.equals("")) {
                    preferenceString += ";";
                }
                preferenceString += inputList.get(userChoiceIndex).get(i);
            }
            // commit changes to the preferences file
            editor.putString(textArray[userChoiceIndex],preferenceString);
            editor.commit();

            // display the results
            LinkedList<String> displayValues = new LinkedList<>(inputList.get(userChoiceIndex));
            for(int i = 0; i < displayValues.size(); i++) {
                displayValues.set(i,displayValues.get(i) + " Uhr");
            }
            listViewArray[userChoiceIndex].setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,0,displayValues));
            // set text on buttons
            String numberOfDates = String.valueOf(inputList.get(userChoiceIndex).size())+ " Termin";
            if(inputList.get(userChoiceIndex).size() != 1) {
                numberOfDates += "e";
            }
            SpannableString s = new SpannableString(textArray[userChoiceIndex].substring(0,2) +"\n"+ numberOfDates);
            s.setSpan(new StyleSpan(Typeface.BOLD),0,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(0.75f),3,s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewArr[userChoiceIndex].setText(s);
        }
    }
}
