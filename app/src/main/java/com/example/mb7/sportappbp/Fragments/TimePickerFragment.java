package com.example.mb7.sportappbp.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.example.mb7.sportappbp.Activity.ActivitySettingInitializer;

/**
 * Fragment to show a time picker
 * Created by Aziani on 18.01.2017.
 */

public class TimePickerFragment extends DialogFragment
        implements
        TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create a new instance of TimePickerDialog and return it
        TimePickerDialog dialog = new TimePickerDialog(
                getActivity(), this, 0, 0, DateFormat.is24HourFormat(getActivity()));
        // set delete and cancel button
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onTimeSet(null,999,999);
            }
        });
        return dialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ActivitySettingInitializer activity = (ActivitySettingInitializer) getActivity();
        // send the set time to the setting activity
        activity.setTime(hourOfDay, minute);
    }
}