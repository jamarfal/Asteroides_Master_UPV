package org.example.asteroides.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.example.asteroides.R;

/**
 * Created by jamarfal on 3/10/16.
 */

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.asteroids_preferences);
    }
}