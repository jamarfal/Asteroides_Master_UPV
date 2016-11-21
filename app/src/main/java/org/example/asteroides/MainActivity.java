package org.example.asteroides;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.StrictMode;
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

import org.example.asteroides.logic.AlmacenPuntuacionesFicheroExterno;
import org.example.asteroides.logic.AlmacenPuntuacionesFicheroExternoExtApl;
import org.example.asteroides.logic.AlmacenPuntuacionesFicheroInterno;
import org.example.asteroides.logic.AlmacenPuntuacionesGSon;
import org.example.asteroides.logic.AlmacenPuntuacionesJson;
import org.example.asteroides.logic.AlmacenPuntuacionesPreferencias;
import org.example.asteroides.logic.AlmacenPuntuacionesProvider;
import org.example.asteroides.logic.AlmacenPuntuacionesRecursosAssets;
import org.example.asteroides.logic.AlmacenPuntuacionesRecursosRaw;
import org.example.asteroides.logic.AlmacenPuntuacionesSQLite;
import org.example.asteroides.logic.AlmacenPuntuacionesSQLiteRel;
import org.example.asteroides.logic.AlmacenPuntuacionesSW_PHP;
import org.example.asteroides.logic.AlmacenPuntuacionesSW_PHP_AsyncTask;
import org.example.asteroides.logic.AlmacenPuntuacionesSocket;
import org.example.asteroides.logic.PointsStorage;
import org.example.asteroides.logic.PointsStorageArray;
import org.example.asteroides.logic.PointsStorageXML_SAX;
import org.example.asteroides.preferences.GamePreferences;
import org.example.asteroides.service.ServicioMusica;
import org.example.asteroides.view.GameView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    public static final String SONG_POSITION_TIME = "song_position_time";
    public static final int REQUEST_CODE = 1234;
    private Button aboutButton, scoreButon, playButton, configButton;
    private TextView gameTitleTextView;
    private ImageView asteroidInRotation;
    private GestureOverlayView gestureOverlayView;
    Animation rotateAndZoom, appear, translationRight, translationLeft, zoomMaxMin, loopRotation;
    private GestureLibrary gestureLibrary;
    public static PointsStorage pointsStorage;
    private GamePreferences gamePreferences;


    //region Life Cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        configOnClickListeners();

        initGesturesDetection();

        initAnimations();

        startAnimations();

        gamePreferences = new GamePreferences(this);

        if (gamePreferences.playMusic()) {
            initMusic();
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    private void initMusic() {
        startService(new Intent(MainActivity.this, ServicioMusica.class));
    }

    private void stopMusic() {
        stopService(new Intent(MainActivity.this, ServicioMusica.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gamePreferences.playMusic())
            initMusic();

        initSaveMethod();
    }

    private void initSaveMethod() {
        int saveMethodType = gamePreferences.getSaveMethod();
        switch (saveMethodType) {
            case 0:
                pointsStorage = new PointsStorageArray();
                break;
            case 1:
                pointsStorage = new AlmacenPuntuacionesPreferencias(this);
                break;
            case 2:
                pointsStorage = new AlmacenPuntuacionesFicheroInterno(this);
                break;
            case 3:
                pointsStorage = new AlmacenPuntuacionesFicheroExterno(this);
                break;
            case 4:
                pointsStorage = new AlmacenPuntuacionesFicheroExternoExtApl(this);
                break;
            case 5:
                pointsStorage = new AlmacenPuntuacionesRecursosRaw(this);
                break;
            case 6:
                pointsStorage = new AlmacenPuntuacionesRecursosAssets(this);
                break;
            case 7:
                pointsStorage = new PointsStorageXML_SAX(this);
                break;
            case 8:
                pointsStorage = new AlmacenPuntuacionesGSon(this);
                break;
            case 9:
                pointsStorage = new AlmacenPuntuacionesJson(this);
                break;
            case 10:
                pointsStorage = new AlmacenPuntuacionesSQLiteRel(this);
                break;
            case 11:
                pointsStorage = new AlmacenPuntuacionesProvider(this);
                break;
            case 12:
                pointsStorage = new AlmacenPuntuacionesSocket();
                break;
            case 13:
                pointsStorage = new AlmacenPuntuacionesSW_PHP();
                break;
            case 14:
                pointsStorage = new AlmacenPuntuacionesSW_PHP_AsyncTask(this);
                break;
        }
    }

    @Override
    protected void onPause() {
        if (gamePreferences.playMusic())
            stopMusic();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //endregion


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (mediaPlayer != null) {
//            int position = mediaPlayer.getCurrentPosition();
//            outStat


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null && mediaPlayer != null) {
//            int position = savedInstanceState.getInt(SONG_POSITION_TIME);
//            mediaPlayer.seekTo(position);
//        }

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
        startActivityForResult(intent, REQUEST_CODE);
    }
    //endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int puntuacion = data.getExtras().getInt(GameView.POINTS);
            String nombre = "Yo";
            // Mejor leer nombre desde un AlertDialog.Builder o preferencias
            pointsStorage.saveScore(puntuacion, nombre, System.currentTimeMillis());
            throwScoreActivity(null);
        }
    }

    //region Show Preferences Values method
    public void showPreferences() {
        GamePreferences gamePreferences = new GamePreferences(this);
        String s = " Música: " + gamePreferences.playMusic()
                + "\n Gráficos: " + getResources().getStringArray(R.array.graphicTypes)[gamePreferences.getGraphicType()]
                + "\n Fragmentos: " + gamePreferences.getNumFragments()
                + "\n Activar Multiplayer: " + gamePreferences.isMultiplayer()
                + "\n Máximo número de Jugadores: " + gamePreferences.getMaxNumberPlayer()
                + "\n Tipo de conexión: " + getResources().getStringArray(R.array.connectionTypes)[gamePreferences.getConnectionType()]
                + "\n Control: " + getResources().getStringArray(R.array.controllerTypes)[gamePreferences.getController()]
                + "\n Método de Guardado: " + getResources().getStringArray(R.array.saveMethodsTypesValues)[gamePreferences.getSaveMethod()];
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    //endregion


}
