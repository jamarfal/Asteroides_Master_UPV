package org.example.asteroides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.example.asteroides.logic.PointsStorageArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private Button aboutButton, scoreButon, playButton, configButton;
    private TextView gameTitleTextView;
    private ImageView asteroidInRotation;
    private GestureOverlayView gestureOverlayView;
    Animation rotateAndZoom, appear, translationRight, translationLeft, zoomMaxMin, loopRotation;
    private GestureLibrary gestureLibrary;
    public static PointsStorageArray storageArray = new PointsStorageArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        configOnClickListeners();

        initGesturesDetection();

        initAnimations();

        startAnimations();
    }


    //region Init methods
    private void initViews() {
        aboutButton = (Button) findViewById(R.id.button_about);
        scoreButon = (Button) findViewById(R.id.button_score);
        playButton = (Button) findViewById(R.id.button_play);
        configButton = (Button) findViewById(R.id.button_config);
        gameTitleTextView = (TextView) findViewById(R.id.game_title_text_view);
        asteroidInRotation = (ImageView) findViewById(R.id.imageview_main_rotating_asteroid);
        gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestureoverlayview_main_gestures);
    }

    private void configOnClickListeners() {
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutButton.startAnimation(rotateAndZoom);
                throwAboutActivity(null);
            }
        });
    }

    private void initGesturesDetection() {
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLibrary.load()) {
            finish();
        }
        gestureOverlayView.addOnGesturePerformedListener(this);
    }


    private void initAnimations() {
        rotateAndZoom = AnimationUtils.loadAnimation(this, R.anim.rotation_with_zoom);
        appear = AnimationUtils.loadAnimation(this, R.anim.appear);
        translationRight = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        translationLeft = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        zoomMaxMin = AnimationUtils.loadAnimation(this, R.anim.zoom_max_min);
        loopRotation = AnimationUtils.loadAnimation(this, R.anim.loop_rotation);
    }


    private void startAnimations() {
        gameTitleTextView.startAnimation(rotateAndZoom);
        playButton.startAnimation(appear);
        configButton.startAnimation(translationRight);
        aboutButton.startAnimation(zoomMaxMin);
        scoreButon.startAnimation(translationLeft);
        asteroidInRotation.startAnimation(loopRotation);
    }
    //endregion

    //region Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; /** true -> el menú ya está visible */}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            throwPreferencesActivity(null);
            return true;
        }
        if (id == R.id.about_menu) {
            throwAboutActivity(null);
            return true;
        }

        if (id == R.id.info_preferences) {
            showPreferences();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Gestures Interfaces methods
    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
        if (predictions.size() > 0) {
            String command = predictions.get(0).name;
            if (command.equalsIgnoreCase("play")) {
                throwGameActivity(null);
            } else if (command.equalsIgnoreCase("config")) {
                throwPreferencesActivity(null);
            } else if (command.equalsIgnoreCase("about")) {
                throwAboutActivity(null);
            } else if (command.equalsIgnoreCase("cancel")) {
                finish();
            }
        }
    }
    //endregion

    //region Throw new Activities methods
    public void throwAboutActivity(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void throwPreferencesActivity(View view) {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }

    public void throwScoreActivity(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void throwGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    //endregion

    //region Show Preferences Values method
    public void showPreferences() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = " Música: " + pref.getBoolean(Preferences.KEY_MUSIC, true)
                + "\n Gráficos: " + pref.getString(Preferences.KEY_GRAPH, "1")
                + "\n Fragmentos: " + pref.getString(Preferences.KEY_ASTEROID_FRAGMENTS, "3")
                + "\n Activar Multiplayer: " + pref.getBoolean(Preferences.KEY_ACTIVATE_MULTIPLAYER, false)
                + "\n Máximo número de Jugadores: " + pref.getString(Preferences.KEY_MAX_NUM_PLAYER, "1")
                + "\n Tipo de conexión: " + pref.getString(Preferences.KEY_CONNECTION_TYPE, "1");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    //endregion


}
