package org.example.asteroides.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jamarfal on 24/10/16.
 */

public class GamePreferences {

    private SharedPreferences sharedPreferences;
    public final static String KEY_MUSIC = "music";
    public final static String KEY_GRAPH = "graphics";
    public final static String KEY_ASTEROID_FRAGMENTS = "asteroid_fragments";
    public final static String KEY_ACTIVATE_MULTIPLAYER = "activate_multiplayer";
    public final static String KEY_MAX_NUM_PLAYER = "max_num_players";
    public final static String KEY_CONNECTION_TYPE = "connection_type";
    public static final String KEY_CONTROLLER = "controller";

    public GamePreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getNumFragments() {
        return tryParseInt(sharedPreferences.getString(KEY_ASTEROID_FRAGMENTS, "1"));
    }

    public boolean playMusic() {
        return sharedPreferences.getBoolean(KEY_MUSIC, true);
    }

    public boolean isMultiplayer() {
        return sharedPreferences.getBoolean(KEY_ACTIVATE_MULTIPLAYER, false);
    }

    public int getMaxNumberPlayer() {
        return tryParseInt(sharedPreferences.getString(KEY_MAX_NUM_PLAYER, "1"));
    }

    public int getConnectionType() {
        return tryParseInt(sharedPreferences.getString(KEY_CONNECTION_TYPE, "1"));
    }

    public boolean playerHasSelectedVectorial() {
        return sharedPreferences.getString(KEY_GRAPH, "1").equals("0");
    }

    public int getController() {
        return tryParseInt(sharedPreferences.getString(KEY_CONTROLLER, "1"));
    }

    public int getGraphicType() {
        return tryParseInt(sharedPreferences.getString(KEY_GRAPH, "1"));
    }

    private int tryParseInt(String numberStr) {
        int number;
        try {
            number = Integer.parseInt(numberStr);
        } catch (NumberFormatException exception) {
            number = 0;
        }
        return number;
    }

    public boolean playerHasSelectedSensorControl() {
        return sharedPreferences.getString(KEY_CONTROLLER, "2").equalsIgnoreCase("1") || sharedPreferences.getString(KEY_CONTROLLER, "2").equalsIgnoreCase("3");
    }

    public boolean playerHasSelectedKeyboardControl() {
        return sharedPreferences.getString(KEY_CONTROLLER, "2").equalsIgnoreCase("0");
    }

    public boolean playerHasSelectedTouchontrol() {
        return sharedPreferences.getString(KEY_CONTROLLER, "2").equalsIgnoreCase("2");
    }
}
