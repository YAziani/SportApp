package com.example.mb7.sportappbp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Fragment to show a time picker
 * Created by Aziani on 18.01.2017.
 */

public class TimePickerFragment extends DialogFragment  implements TimePickerDialog.OnTimeSetListener, TimePickerDialog.OnCancelListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, 0, 0,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onTimeSet(null,999,999);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SettingInitializerActivity activity = (SettingInitializerActivity) getActivity();
        activity.setTime(hourOfDay, minute);
    }
}