package com.example.mb7.sportappbp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by MB7 on 15.01.2017.
 */

public class SettingsActivity extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
