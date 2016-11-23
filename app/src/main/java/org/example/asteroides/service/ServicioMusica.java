package org.example.asteroides.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.example.asteroides.MainActivity;
import org.example.asteroides.R;

/**
 * Created by jamarfal on 6/11/16.
 */

public class ServicioMusica extends Service {
    private static final int ID_NOTIFICACION_CREAR = 1;
    MediaPlayer reproductor;

    @Override
    public void onCreate() {
        reproductor = MediaPlayer.create(this, R.raw.audio);
    }


    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        NotificationCompat.Builder notific = new NotificationCompat.Builder(this)
                .setContentTitle("Creando Servicio de Música")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("información adicional");
        PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notific.setContentIntent(intencionPendiente);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());

        reproductor.start();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        reproductor.stop();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICACION_CREAR);
    }


    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }
}
