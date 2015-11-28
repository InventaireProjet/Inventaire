package com.androidprojects.inventaireii.Preferences;

import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;



public class AppSettingsActivity extends AppCompatActivity implements Preference.OnPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new AppSettingsFragment()).commit();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        finish();
        return false;
    }
}
