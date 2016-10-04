package org.example.asteroides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.example.asteroides.logic.PointsStorageArray;

public class MainActivity extends AppCompatActivity {

    private Button aboutButton, scoreButon;
    public static PointsStorageArray storageArray = new PointsStorageArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aboutButton = (Button) findViewById(R.id.button_about);
        scoreButon = (Button) findViewById(R.id.button_score);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwAboutActivity(null);
            }
        });
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

    public void showPreferences(View view) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica", true)
                + ", gráficos: " + pref.getString("graficos", "?")
                + ", Activar Multiplayer: " + pref.getBoolean("activate_multiplayer", false)
                + ", Máximo número de Jugadores: " + pref.getString("max_num_players", "1")
                + ", Tipo de conexión: " + pref.getString("conecction_type", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void exitApplication() {
        finish();
    }
}
