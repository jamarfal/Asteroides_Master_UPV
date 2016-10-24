package org.example.asteroides;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.example.asteroides.view.GameView;

public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = (GameView) findViewById(R.id.gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.getGameThread().pauseThread();
        gameView.deactivateSensor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.getGameThread().resumeThread();
        gameView.activateSensor();
    }

    @Override
    protected void onDestroy() {
        gameView.getGameThread().stopThread();
        gameView.deactivateSensor();
        super.onDestroy();
    }
}
