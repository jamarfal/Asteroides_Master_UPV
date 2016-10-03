package org.example.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by jamarfal on 3/10/16.
 */

public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.asteroids_preferences);
    }
}
