package com.example.mb7.sportappbp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.mb7.sportappbp.R;

/**
 * Created by M.Braei on 15.01.2017.
 */

public class ActivitySettings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Toast.makeText(this,
                getString(R.string.erfolgreichgespeichert), Toast.LENGTH_SHORT).show();
    }


}


















