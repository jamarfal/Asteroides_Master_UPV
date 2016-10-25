package org.example.asteroides.pool;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import org.example.asteroides.R;

/**
 * Created by jamarfal on 23/10/16.
 */

public class FxSoundPool {
    private static FxSoundPool instance;

    private SoundPool soundPool;
    int idShoot, idExplossion;

    public static FxSoundPool getInstance(Context context) {
        if (instance == null) {
            synchronized (FxSoundPool.class) {
                if (instance == null) {
                    instance = new FxSoundPool(context);
                }
            }
        }
        return instance;
    }

    private FxSoundPool(Context context) {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        loadSounds(context);
    }

    private void loadSounds(Context context) {
        idShoot = soundPool.load(context, R.raw.disparo, 0);
        idExplossion = soundPool.load(context, R.raw.explosion, 0);
    }

    public void explossion() {
        soundPool.play(idExplossion, 1, 1, 0, 0, 1);
    }

    public void misil() {
        soundPool.play(idShoot, 1, 1, 1, 0, 1);
    }
}
