package com.androidprojects.inventaireii.Preferences;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.androidprojects.inventaireii.R;


public  class AppSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);



    }


}
