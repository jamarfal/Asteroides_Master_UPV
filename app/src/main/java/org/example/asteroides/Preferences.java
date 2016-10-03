package org.example.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jamarfal on 3/10/16.
 */

public class Preferences extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPreferences();
    }

    private void initPreferences() {
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferencesFragment())
                .commit();
    }


}
