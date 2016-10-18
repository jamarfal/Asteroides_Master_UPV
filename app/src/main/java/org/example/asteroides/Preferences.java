package org.example.asteroides;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.example.asteroides.fragment.PreferencesFragment;

/**
 * Created by jamarfal on 3/10/16.
 */

public class Preferences extends AppCompatActivity {

    public final static String KEY_MUSIC = "music";
    public final static String KEY_GRAPH = "graphics";
    public final static String KEY_ASTEROID_FRAGMENTS = "asteroid_fragments";
    public final static String KEY_ACTIVATE_MULTIPLAYER = "activate_multiplayer";
    public final static String KEY_MAX_NUM_PLAYER = "max_num_players";
    public final static String KEY_CONNECTION_TYPE = "connection_type";
    public static final String KEY_SENSOR = "sensor_control";

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
