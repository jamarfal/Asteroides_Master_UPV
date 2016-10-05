package org.example.asteroides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.example.asteroides.logic.PointsStorageArray;

public class MainActivity extends AppCompatActivity {

    private Button aboutButton, scoreButon, playButton, configButton;
    private TextView gameTitleTextView;
    Animation rotateAndZoom;
    public static PointsStorageArray storageArray = new PointsStorageArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aboutButton = (Button) findViewById(R.id.button_about);
        scoreButon = (Button) findViewById(R.id.button_score);
        playButton = (Button) findViewById(R.id.button_play);
        configButton = (Button) findViewById(R.id.button_config);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutButton.startAnimation(rotateAndZoom);
                throwAboutActivity(null);
            }
        });
        gameTitleTextView = (TextView) findViewById(R.id.game_title_text_view);
        rotateAndZoom = AnimationUtils.loadAnimation(this, R.anim.rotation_with_zoom);
        Animation appear = AnimationUtils.loadAnimation(this, R.anim.appear);
        Animation translationRight = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        Animation translationLeft = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        Animation zoomMaxMin = AnimationUtils.loadAnimation(this, R.anim.zoom_max_min);
        gameTitleTextView.startAnimation(rotateAndZoom);
        playButton.startAnimation(appear);
        configButton.startAnimation(translationRight);
        aboutButton.startAnimation(zoomMaxMin);
        scoreButon.startAnimation(translationLeft);
    }


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

    public void showPreferences() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = " Música: " + pref.getBoolean("music", true)
                + "\n Gráficos: " + pref.getString("graphics", "1")
                + "\n Fragmentos: " + pref.getString("asteroid_fragments", "3")
                + "\n Activar Multiplayer: " + pref.getBoolean("activate_multiplayer", false)
                + "\n Máximo número de Jugadores: " + pref.getString("max_num_players", "1")
                + "\n Tipo de conexión: " + pref.getString("connection_type", "1");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void exitApplication() {
        finish();
    }
}
