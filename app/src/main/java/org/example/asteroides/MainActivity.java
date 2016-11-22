package org.example.asteroides;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import org.example.asteroides.logic.PointsStorageExternalFile;
import org.example.asteroides.logic.AlmacenPuntuacionesFicheroExternoExtApl;
import org.example.asteroides.logic.PointsStorageInternalFile;
import org.example.asteroides.logic.AlmacenPuntuacionesGSon;
import org.example.asteroides.logic.AlmacenPuntuacionesJson;
import org.example.asteroides.logic.PointsStoragePreferences;
import org.example.asteroides.logic.AlmacenPuntuacionesProvider;
import org.example.asteroides.logic.AlmacenPuntuacionesRecursosAssets;
import org.example.asteroides.logic.AlmacenPuntuacionesRecursosRaw;
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
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 1232;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 1233;
    public static final int REQUEST_CODE = 1234;
    private Button aboutButton, scoreButon, playButton, configButton;
    private TextView gameTitleTextView;
    private ImageView asteroidInRotation;
    private GestureOverlayView gestureOverlayView;
    Animation rotateAndZoom, appear, translationRight, translationLeft, zoomMaxMin, loopRotation;
    private GestureLibrary gestureLibrary;
    public static PointsStorage pointsStorage;
    private GamePreferences gamePreferences;
    private int score;


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

    //region Overrided methods
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            score = data.getExtras().getInt(GameView.POINTS);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveScore();
            } else {
                String message = "Sin el permiso de escritura en la memoria externa no se pueden almacenar puntuaciones";
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, message, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveScore();
            } else {
                Toast.makeText(this, "Sin el permiso de escribir en la memoria externa no se pueden almacenar puntuaciones", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToScoreActivity();
            } else {
                Toast.makeText(this, "Sin el permiso de escribir en la memoria externa no se pueden leer puntuaciones", Toast.LENGTH_LONG).show();
            }
        }
    }

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
    //endregion

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

    private void initSaveMethod() {
        int saveMethodType = gamePreferences.getSaveMethod();
        switch (saveMethodType) {
            case 0:
                pointsStorage = new PointsStorageArray();
                break;
            case 1:
                pointsStorage = new PointsStoragePreferences(this);
                break;
            case 2:
                pointsStorage = new PointsStorageInternalFile(this);
                break;
            case 3:
                pointsStorage = new PointsStorageExternalFile(this);
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

    private void initMusic() {
        startService(new Intent(MainActivity.this, ServicioMusica.class));
    }

    private void stopMusic() {
        stopService(new Intent(MainActivity.this, ServicioMusica.class));
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            goToScoreActivity();
        } else {
            String message = "Sin el permiso de escritura en la memoria externa no se pueden almacenar puntuaciones";
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, message, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST, this);
        }
    }

    private void goToScoreActivity() {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void throwGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    //endregion

    //region Show Preferences Values method
    public void showPreferences() {
        String s = " Música: " + gamePreferences.playMusic()
                + "\n Usuario: " + gamePreferences.getUserName()
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

    //region Permission
    private void requestPermission(final String permission, String justication, final int code, final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            new AlertDialog.Builder(activity)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justication)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
        }
    }
    //endregion

    private void saveScore() {
        pointsStorage.saveScore(score, gamePreferences.getUserName(), System.currentTimeMillis());
        throwScoreActivity(null);
    }

}


