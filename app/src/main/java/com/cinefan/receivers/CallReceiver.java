package com.cinefan.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.cinefan.utils.NotificationUtils;

/**
 * Receptor de llamadas telefónicas
 */
public class CallReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        // Verificar que la acción sea de llamada entrante
        if (intent.getAction() != null && 
                intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            
            // Si es llamada entrante
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                handleIncomingCall(context, phoneNumber);
            }
        }
    }
    
    /**
     * Maneja una llamada entrante
     * @param context Contexto de la aplicación
     * @param phoneNumber Número de teléfono entrante
     */
    private void handleIncomingCall(Context context, String phoneNumber) {
        // Crear notificación de llamada
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            NotificationUtils.showIncomingCallNotification(context, phoneNumber);
        }
    }
}