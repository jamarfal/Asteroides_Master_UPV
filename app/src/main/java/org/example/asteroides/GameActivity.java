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
        if (gameView.getGameThread() != null)
            gameView.getGameThread().pauseThread();
        if (gameView.getSensorController() != null)
            gameView.getSensorController().deactivateSensor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView.getGameThread() != null)
            gameView.getGameThread().resumeThread();
        if (gameView.getSensorController() != null)
            gameView.getSensorController().activateSensor();
    }

    @Override
    protected void onDestroy() {
        if (gameView.getGameThread() != null)
            gameView.getGameThread().stopThread();
        if (gameView.getSensorController() != null)
            gameView.getSensorController().deactivateSensor();
        super.onDestroy();
    }
}
