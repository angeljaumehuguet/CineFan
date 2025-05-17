package com.cinefan.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cinefan.R;
import com.cinefan.actividades.MainActivity;

/**
 * Utilidades para crear y mostrar notificaciones
 */
public class NotificationUtils {
    
    private static final String CHANNEL_ID_CALLS = "cinefan_channel_calls";
    private static final String CHANNEL_ID_SMS = "cinefan_channel_sms";
    private static final String CHANNEL_ID_GENERAL = "cinefan_channel_general";
    
    private static final int NOTIFICATION_ID_CALL = 1001;
    private static final int NOTIFICATION_ID_SMS = 1002;
    
    /**
     * Crea los canales de notificación para Android 8.0+
     * @param context Contexto de la aplicación
     */
    public static void createNotificationChannels(Context context) {
        // Solo es necesario para Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = 
                    context.getSystemService(NotificationManager.class);
            
            // Canal para llamadas
            NotificationChannel channelCalls = new NotificationChannel(
                    CHANNEL_ID_CALLS,
                    context.getString(R.string.channel_calls_name),
                    NotificationManager.IMPORTANCE_HIGH);
            channelCalls.setDescription(context.getString(R.string.channel_calls_description));
            
            // Canal para SMS
            NotificationChannel channelSms = new NotificationChannel(
                    CHANNEL_ID_SMS,
                    context.getString(R.string.channel_sms_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channelSms.setDescription(context.getString(R.string.channel_sms_description));
            
            // Canal general
            NotificationChannel channelGeneral = new NotificationChannel(
                    CHANNEL_ID_GENERAL,
                    context.getString(R.string.channel_general_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channelGeneral.setDescription(context.getString(R.string.channel_general_description));
            
            // Registrar los canales
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channelCalls);
                notificationManager.createNotificationChannel(channelSms);
                notificationManager.createNotificationChannel(channelGeneral);
            }
        }
    }
    
    /**
     * Muestra una notificación para una llamada entrante
     * @param context Contexto de la aplicación
     * @param phoneNumber Número de teléfono entrante
     */
    public static void showIncomingCallNotification(Context context, String phoneNumber) {
        // Intent para abrir la aplicación al pulsar la notificación
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        
        // Sonido de notificación
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_CALLS)
                .setSmallIcon(android.R.drawable.ic_dialog_call)
                .setContentTitle(context.getString(R.string.incoming_call))
                .setContentText(phoneNumber)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        
        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        
        // Comprobar permisos en Android 13+
        if (checkNotificationPermission(context)) {
            notificationManager.notify(NOTIFICATION_ID_CALL, builder.build());
        }
    }
    
    /**
     * Muestra una notificación para un SMS entrante
     * @param context Contexto de la aplicación
     * @param sender Remitente del SMS
     * @param messageBody Cuerpo del mensaje
     */
    public static void showSmsNotification(Context context, String sender, String messageBody) {
        // Intent para abrir la aplicación al pulsar la notificación
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        
        // Sonido de notificación
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_SMS)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(sender)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        
        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        
        // Comprobar permisos en Android 13+
        if (checkNotificationPermission(context)) {
            notificationManager.notify(NOTIFICATION_ID_SMS, builder.build());
        }
    }
    
    /**
     * Comprueba si la aplicación tiene permisos para mostrar notificaciones
     * @param context Contexto de la aplicación
     * @return true si tiene permisos, false en caso contrario
     */
    private static boolean checkNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // En Android 13+ se requiere el permiso POST_NOTIFICATIONS
            // Como esto es solo para demostración, simplemente verificamos que NotificationManagerCompat
            // tenga los permisos necesarios
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        
        return true;
    }
}