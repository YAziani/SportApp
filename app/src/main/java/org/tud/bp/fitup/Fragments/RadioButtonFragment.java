package com.tud.bp.fitup.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * class to show a radio button dialog to choose the users preferred means of transportation
 * Created by Aziani on 25.01.2017.
 */

public class RadioButtonFragment extends DialogFragment {

    int selectedItem = 1;
    SharedPreferences preferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // array of displayed items
        CharSequence[] charSequencesArray = new CharSequence[]{"zu Fuß", "Fahrrad", "Öffentliche Verkehrsmittel",
                "Auto"};
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set the dialog title
        builder.setTitle("Fortbewegungsmittel zum Fitnessstudio")
                // the listener through which to receive callbacks when item is selected
                .setSingleChoiceItems(charSequencesArray, Integer.valueOf(preferences.getString("lstVerkehrsmittel",
                        "0")) - 1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if the user checked the item, add it to the selected items
                                selectedItem = which + 1;
                            }
                        })
                // set the OK button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, save the selected item
                        preferences.edit().putString("lstVerkehrsmittel", String.valueOf(selectedItem)).apply();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}

