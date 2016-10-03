package org.example.asteroides;

import android.os.Bundle;
import android.preference.PreferenceFragment;

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