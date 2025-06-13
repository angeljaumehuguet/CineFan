package com.example.cinefan.receptores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.cinefan.R;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.NotificacionHelper;

/**
 * receptor para detectar llamadas entrantes y mostrar notificacion
 */
public class ReceptorLlamadas extends BroadcastReceiver {

    // Se usa una variable estática para rastrear el estado entre broadcasts
    private static boolean llamadaSonando = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state != null) {
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    llamadaSonando = true;
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.d(Constantes.TAG_RECEPTOR, "Llamada entrante de: " + incomingNumber);
                    mostrarNotificacionLlamada(context, incomingNumber);
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    // La llamada fue contestada
                    llamadaSonando = false;
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // La llamada ha terminado
                    if (llamadaSonando) {
                        // Si estaba sonando y ahora está inactiva, es una llamada perdida
                        Log.d(Constantes.TAG_RECEPTOR, "Llamada perdida detectada.");
                        mostrarNotificacionLlamadaFinalizada(context);
                    }
                    llamadaSonando = false;
                }
            }
        }
    }

    // mostrar notificacion de llamada entrante
    private void mostrarNotificacionLlamada(Context context, String numeroTelefono) {
        String titulo = context.getString(R.string.notificacion_llamada_titulo);
        String mensaje;

        if (numeroTelefono != null && !numeroTelefono.isEmpty()) {
            mensaje = context.getString(R.string.notificacion_llamada_mensaje_con_numero, numeroTelefono);
        } else {
            mensaje = context.getString(R.string.notificacion_llamada_mensaje_sin_numero);
        }

        NotificacionHelper.mostrarNotificacion(
                context,
                Constantes.NOTIFICACION_ID_LLAMADA,
                titulo,
                mensaje,
                R.drawable.ic_phone,
                true // con sonido
        );

        Log.d(Constantes.TAG_RECEPTOR, "Notificacion de llamada mostrada");
    }

    // mostrar notificacion de llamada finalizada/perdida
    private void mostrarNotificacionLlamadaFinalizada(Context context) {
        String titulo = context.getString(R.string.notificacion_llamada_finalizada_titulo);
        String mensaje = context.getString(R.string.notificacion_llamada_finalizada_mensaje);

        // NOTA: Asegúrate de tener el icono 'ic_phone_missed.xml' en res/drawable
        NotificacionHelper.mostrarNotificacion(
                context,
                Constantes.NOTIFICACION_ID_LLAMADA,
                titulo,
                mensaje,
                R.drawable.ic_phone_missed,
                false // sin sonido
        );

        Log.d(Constantes.TAG_RECEPTOR, "Notificacion de llamada finalizada mostrada");
    }
}
