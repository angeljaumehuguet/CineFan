package com.cinefan.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.cinefan.utils.NotificationUtils;

/**
 * Receptor de mensajes SMS
 */
public class SmsReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        // Verificar que la acción sea de SMS recibido
        if (intent.getAction() != null && 
                intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            
            // Obtener los mensajes SMS
            SmsMessage[] messages = null;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Método para API 19+
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            } else {
                // Método para versiones anteriores
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        messages = new SmsMessage[pdus.length];
                        for (int i = 0; i < pdus.length; i++) {
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }
                    }
                }
            }
            
            // Procesar los mensajes
            if (messages != null) {
                handleIncomingSms(context, messages);
            }
        }
    }
    
    /**
     * Maneja los mensajes SMS entrantes
     * @param context Contexto de la aplicación
     * @param messages Mensajes recibidos
     */
    private void handleIncomingSms(Context context, SmsMessage[] messages) {
        if (messages.length > 0) {
            SmsMessage message = messages[0];
            String sender = message.getDisplayOriginatingAddress();
            String messageBody = message.getDisplayMessageBody();
            
            // Mostrar notificación
            NotificationUtils.showSmsNotification(context, sender, messageBody);
        }
    }
}