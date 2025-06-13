package com.example.cinefan.utilidades;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cinefan.R;
import com.example.cinefan.actividades.MainActivity;

/**
 * clase helper para manejar notificaciones de la aplicacion
 */
public class NotificacionHelper {
    
    // crear canal de notificaciones (necesario para android 8+)
    public static void crearCanalNotificaciones(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombre = Constantes.CANAL_NOMBRE;
            String descripcion = "Canal para notificaciones de CineFan";
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            
            NotificationChannel canal = new NotificationChannel(
                Constantes.CANAL_ID_NOTIFICACIONES, 
                nombre, 
                importancia
            );
            canal.setDescription(descripcion);
            
            // configurar sonido y vibracion
            canal.enableVibration(true);
            canal.setVibrationPattern(new long[]{0, 500, 250, 500});
            
            // registrar canal en el sistema
            NotificationManager notificationManager = 
                context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(canal);
            }
        }
    }
    
    // mostrar notificacion basica
    public static void mostrarNotificacion(Context context, int id, String titulo, 
                                         String mensaje, int icono, boolean conSonido) {
        // crear canal si no existe
        crearCanalNotificaciones(context);
        
        // crear intent para abrir la app al tocar notificacion
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // construir notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
            context, 
            Constantes.CANAL_ID_NOTIFICACIONES
        )
            .setSmallIcon(icono)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(context.getResources().getColor(R.color.cinefan_primary, null));
        
        // agregar sonido si se solicita
        if (conSonido) {
            Uri sonidoUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(sonidoUri);
            builder.setVibrate(new long[]{0, 500, 250, 500});
        }
        
        // mostrar notificacion
        NotificationManagerCompat notificationManager = 
            NotificationManagerCompat.from(context);
        
        try {
            notificationManager.notify(id, builder.build());
        } catch (SecurityException e) {
            // manejar caso donde no hay permisos de notificacion
            android.util.Log.w("NotificacionHelper", 
                "No hay permisos para mostrar notificaciones");
        }
    }
    
    // mostrar notificacion expandible con accion
    public static void mostrarNotificacionExpandible(Context context, int id, String titulo,
                                                   String mensaje, String mensajeExpandido,
                                                   int icono, String textoAccion,
                                                   PendingIntent accionIntent) {
        crearCanalNotificaciones(context);
        
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
            context, 
            Constantes.CANAL_ID_NOTIFICACIONES
        )
            .setSmallIcon(icono)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(mensajeExpandido))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(context.getResources().getColor(R.color.cinefan_primary, null));
        
        // agregar accion si se proporciona
        if (textoAccion != null && accionIntent != null) {
            builder.addAction(icono, textoAccion, accionIntent);
        }
        
        // sonido y vibracion por defecto
        Uri sonidoUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(sonidoUri);
        builder.setVibrate(new long[]{0, 500, 250, 500});
        
        NotificationManagerCompat notificationManager = 
            NotificationManagerCompat.from(context);
        
        try {
            notificationManager.notify(id, builder.build());
        } catch (SecurityException e) {
            android.util.Log.w("NotificacionHelper", 
                "No hay permisos para mostrar notificaciones");
        }
    }
    
    // cancelar notificacion especifica
    public static void cancelarNotificacion(Context context, int id) {
        NotificationManagerCompat notificationManager = 
            NotificationManagerCompat.from(context);
        notificationManager.cancel(id);
    }
    
    // cancelar todas las notificaciones
    public static void cancelarTodasNotificaciones(Context context) {
        NotificationManagerCompat notificationManager = 
            NotificationManagerCompat.from(context);
        notificationManager.cancelAll();
    }
}