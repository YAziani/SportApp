package com.example.mb7.sportappbp.Activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

/**
 * activity used to get the initial training times of the user
 * Created by Aziani on 17.01.2017.
 */
public class ActivitySettingInitializer extends AppCompatActivity {

    int userChoiceIndex;
    String[] textArray;
    String[] inputArray;
    TextView[] textViewArr;
    ImageButton[] imgButtonArr;
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

        // image view for the location icon
        final ImageView imageViewStudio = (ImageView)findViewById(R.id.imageViewStudio);

        // array of all week day buttons
        imgButtonArr = new ImageButton[8];
        imgButtonArr[0] = (ImageButton)findViewById(R.id.imgButtonMo);
        imgButtonArr[1] = (ImageButton)findViewById(R.id.imgButtonDi);
        imgButtonArr[2] = (ImageButton)findViewById(R.id.imgButtonMi);
        imgButtonArr[3] = (ImageButton)findViewById(R.id.imgButtonDo);
        imgButtonArr[4] = (ImageButton)findViewById(R.id.imgButtonFr);
        imgButtonArr[5] = (ImageButton)findViewById(R.id.imgButtonSa);
        imgButtonArr[6] = (ImageButton)findViewById(R.id.imgButtonSo);
        imgButtonArr[7] = (ImageButton)findViewById(R.id.imgButtonStudio);

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
        for(int i = 0; i < 7; i++) {
            textViewArr[i].setText(textArray[i].substring(0,2) + "\n" + inputArray[i]);
        }
        textViewArr[7].setText(textArray[7] + "\n" + inputArray[7]);

        // set color flash and OnClickListener
        for(int i = 0; i < 8; i++) {
            final int j = i;
            // color flash
            imgButtonArr[j].setOnTouchListener(new View.OnTouchListener() {
                private Rect rect;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // if clicked and hold
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        imgButtonArr[j].setColorFilter(Color.argb(100, 0, 0, 255));
                        if(j == 7){
                            imageViewStudio.getDrawable().setColorFilter(Color.argb(100, 0, 0, 255), PorterDuff.Mode.MULTIPLY);
                        }
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        imgButtonArr[j].setColorFilter(Color.argb(0, 0, 0, 0));
                        if(j == 7){
                            imageViewStudio.getDrawable().clearColorFilter();
                        }
                    }
                    // if clicked and left
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            imgButtonArr[j].setColorFilter(Color.argb(100, 0, 0, 0));
                            if(j == 7){
                                imageViewStudio.getDrawable().setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }
                    return false;
                }
            });

            // set on click behavior
            imgButtonArr[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // save the index for the preferences file
                    userChoiceIndex = j;
                    if(userChoiceIndex < 7) {
                        // create a new time picker and show it
                        DialogFragment pickerFragment = new TimePickerFragment();
                        pickerFragment.show(getFragmentManager(),"timepicker");
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettingInitializer.this);
                        builder.setTitle("Tragen Sie die Adresse Ihres Fitnessstudios ein.");
                        builder.setCancelable(true);
                        // set up the input
                        final EditText input = new EditText(getApplicationContext());
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
        textViewArr[userChoiceIndex].setText(textArray[userChoiceIndex] + "\n" + inputArray[userChoiceIndex]);
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
        textViewArr[userChoiceIndex].setText(textArray[userChoiceIndex].substring(0,2) + "\n" + inputArray[userChoiceIndex]);
    }
}
