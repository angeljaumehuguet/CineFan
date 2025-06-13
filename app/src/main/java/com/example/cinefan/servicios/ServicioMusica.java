package com.example.cinefan.servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.cinefan.R;
import com.example.cinefan.actividades.MainActivity;
import com.example.cinefan.utilidades.Constantes;

/**
 * servicio para reproducir musica de fondo en la aplicacion
 */
public class ServicioMusica extends Service {

    // media player para reproducir musica
    private MediaPlayer mediaPlayer;

    // binder para comunicacion con actividades
    private final IBinder binder = new MusicaBinder();

    // estado del servicio
    private boolean estaReproduciendo = false;
    private boolean estaPausado = false;

    // notificacion
    private static final int NOTIFICACION_ID = 1003;

    public class MusicaBinder extends Binder {
        public ServicioMusica getService() {
            return ServicioMusica.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constantes.TAG_MUSICA, "ServicioMusica: onCreate");

        // crear canal de notificacion
        crearCanalNotificacion();

        // inicializar media player
        inicializarMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constantes.TAG_MUSICA, "ServicioMusica: onStartCommand");

        // manejar acciones desde notificacion
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "ACTION_PLAY_PAUSE":
                    alternarReproduccion();
                    break;
                case "ACTION_STOP":
                    detenerMusica();
                    stopSelf();
                    break;
            }
        } else {
            // iniciar reproduccion normal
            iniciarMusica();
        }

        return START_STICKY; // reiniciar si el sistema mata el servicio
    }

    @Override
    public void onDestroy() {
        Log.d(Constantes.TAG_MUSICA, "ServicioMusica: onDestroy");

        // liberar recursos
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        estaReproduciendo = false;
        super.onDestroy();
    }

    // inicializar media player
    private void inicializarMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.musica_fondo);

            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true); // reproducir en bucle
                mediaPlayer.setVolume(0.3f, 0.3f); // volumen bajo para fondo

                // listener para cuando termina (por si falla el looping)
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (estaReproduciendo) {
                            mp.start(); // reiniciar si debe estar reproduciendose
                        }
                    }
                });

                Log.d(Constantes.TAG_MUSICA, "MediaPlayer inicializado correctamente");
            } else {
                Log.e(Constantes.TAG_MUSICA, "Error: No se pudo crear MediaPlayer");
            }

        } catch (Exception e) {
            Log.e(Constantes.TAG_MUSICA, "Error inicializando MediaPlayer: " + e.getMessage());
        }
    }

    // iniciar reproduccion de musica
    public void iniciarMusica() {
        try {
            if (mediaPlayer != null && !estaReproduciendo) {
                mediaPlayer.start();
                estaReproduciendo = true;
                estaPausado = false;

                // mostrar notificacion persistente
                mostrarNotificacion();

                Log.d(Constantes.TAG_MUSICA, "Musica iniciada");
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MUSICA, "Error iniciando musica: " + e.getMessage());
        }
    }

    // pausar musica
    public void pausarMusica() {
        try {
            if (mediaPlayer != null && estaReproduciendo) {
                mediaPlayer.pause();
                estaReproduciendo = false;
                estaPausado = true;

                // actualizar notificacion
                mostrarNotificacion();

                Log.d(Constantes.TAG_MUSICA, "Musica pausada");
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MUSICA, "Error pausando musica: " + e.getMessage());
        }
    }

    // reanudar musica
    public void reanudarMusica() {
        try {
            if (mediaPlayer != null && estaPausado) {
                mediaPlayer.start();
                estaReproduciendo = true;
                estaPausado = false;

                // actualizar notificacion
                mostrarNotificacion();

                Log.d(Constantes.TAG_MUSICA, "Musica reanudada");
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MUSICA, "Error reanudando musica: " + e.getMessage());
        }
    }

    // detener musica
    public void detenerMusica() {
        try {
            if (mediaPlayer != null && (estaReproduciendo || estaPausado)) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0); // volver al inicio
                estaReproduciendo = false;
                estaPausado = false;

                Log.d(Constantes.TAG_MUSICA, "Musica detenida");
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MUSICA, "Error deteniendo musica: " + e.getMessage());
        }
    }

    // alternar entre play y pausa
    public void alternarReproduccion() {
        if (estaReproduciendo) {
            pausarMusica();
        } else {
            if (estaPausado) {
                reanudarMusica();
            } else {
                iniciarMusica();
            }
        }
    }

    // obtener estado actual
    public boolean estaReproduciendo() {
        return estaReproduciendo;
    }

    public boolean estaPausado() {
        return estaPausado;
    }

    // crear canal de notificacion
    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    Constantes.CANAL_ID_NOTIFICACIONES,
                    "Reproductor CineFan",
                    NotificationManager.IMPORTANCE_LOW
            );
            canal.setDescription("Control de reproduccion de musica");
            canal.setShowBadge(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }

    // mostrar notificacion persistente
    private void mostrarNotificacion() {
        // intent para abrir la app
        Intent intentMain = new Intent(this, MainActivity.class);
        PendingIntent pendingMain = PendingIntent.getActivity(
                this, 0, intentMain,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // intent para play/pausa
        Intent intentPlayPause = new Intent(this, ServicioMusica.class);
        intentPlayPause.setAction("ACTION_PLAY_PAUSE");
        PendingIntent pendingPlayPause = PendingIntent.getService(
                this, 1, intentPlayPause,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // intent para detener
        Intent intentStop = new Intent(this, ServicioMusica.class);
        intentStop.setAction("ACTION_STOP");
        PendingIntent pendingStop = PendingIntent.getService(
                this, 2, intentStop,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // construir notificacion
        String titulo = "CineFan - Musica de Fondo";
        String texto = estaReproduciendo ? "Reproduciendo..." : "Pausado";
        int iconoPlayPause = estaReproduciendo ?
                android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, Constantes.CANAL_ID_NOTIFICACIONES
        )
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setContentIntent(pendingMain)
                .setOngoing(true) // no se puede descartar
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setColor(getResources().getColor(R.color.cinefan_primary, null))
                .addAction(iconoPlayPause,
                        estaReproduciendo ? "Pausar" : "Reproducir",
                        pendingPlayPause)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,
                        "Detener", pendingStop);

        Notification notificacion = builder.build();

        // mostrar como servicio en primer plano
        startForeground(NOTIFICACION_ID, notificacion);
    }
}