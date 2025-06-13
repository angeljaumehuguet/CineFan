package com.example.cinefan.receptores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.cinefan.R;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.NotificacionHelper;

/**
 * receptor para detectar sms entrantes y mostrar notificacion
 */
public class ReceptorSms extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constantes.TAG_RECEPTOR, "ReceptorSms: onReceive llamado");

        try {
            String accion = intent.getAction();

            if (SMS_RECEIVED.equals(accion)) {
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    procesarSmsRecibido(context, bundle);
                }
            }

        } catch (Exception e) {
            Log.e(Constantes.TAG_RECEPTOR, "Error en ReceptorSms: " + e.getMessage());
        }
    }

    // procesar sms recibido
    private void procesarSmsRecibido(Context context, Bundle bundle) {
        try {
            Object[] pdus = (Object[]) bundle.get("pdus");
            String formato = bundle.getString("format");

            if (pdus != null && pdus.length > 0) {
                StringBuilder mensajeCompleto = new StringBuilder();
                String numeroRemitente = "";

                // procesar cada parte del sms
                for (Object pdu : pdus) {
                    SmsMessage smsMessage;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu, formato);
                    } else {
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    }

                    if (smsMessage != null) {
                        numeroRemitente = smsMessage.getOriginatingAddress();
                        mensajeCompleto.append(smsMessage.getMessageBody());
                    }
                }

                // procesar sms completo
                if (!mensajeCompleto.toString().isEmpty()) {
                    manejarSmsCompleto(context, numeroRemitente, mensajeCompleto.toString());
                }
            }

        } catch (Exception e) {
            Log.e(Constantes.TAG_RECEPTOR, "Error procesando SMS: " + e.getMessage());
        }
    }

    // manejar sms completo
    private void manejarSmsCompleto(Context context, String numeroRemitente, String mensaje) {
        Log.d(Constantes.TAG_RECEPTOR, "SMS recibido de: " + numeroRemitente);
        Log.d(Constantes.TAG_RECEPTOR, "Contenido: " + mensaje);

        // filtrar mensajes de spam o comerciales
        if (esMensajeComercial(mensaje, numeroRemitente)) {
            Log.d(Constantes.TAG_RECEPTOR, "SMS comercial ignorado");
            return;
        }

        // verificar si contiene palabras clave relacionadas con cine
        boolean esMensajeCine = contienePalabrasClaveCine(mensaje);

        // mostrar notificacion
        mostrarNotificacionSms(context, numeroRemitente, mensaje, esMensajeCine);
    }

    // verificar si es mensaje comercial o spam
    private boolean esMensajeComercial(String mensaje, String numeroRemitente) {
        // convertir a minusculas para comparacion
        String mensajeLower = mensaje.toLowerCase();

        // numeros comerciales tipicos
        if (numeroRemitente != null) {
            String numeroLimpio = numeroRemitente.replaceAll("[^0-9]", "");

            // numeros cortos comerciales
            if (numeroLimpio.length() <= 6) {
                return true;
            }

            // numeros que empiezan con codigos comerciales
            if (numeroLimpio.startsWith("900") || numeroLimpio.startsWith("901") ||
                    numeroLimpio.startsWith("902") || numeroLimpio.startsWith("905") ||
                    numeroLimpio.startsWith("800") || numeroLimpio.startsWith("803")) {
                return true;
            }
        }

        // palabras clave de mensajes comerciales
        String[] palabrasComerciales = {
                "oferta", "promocion", "descuento", "gratis", "premio",
                "ganador", "felicidades", "urgente", "limitada",
                "sms premium", "coste", "tarificacion", "baja"
        };

        for (String palabra : palabrasComerciales) {
            if (mensajeLower.contains(palabra)) {
                return true;
            }
        }

        return false;
    }

    // verificar si contiene palabras clave relacionadas con cine
    private boolean contienePalabrasClaveCine(String mensaje) {
        String mensajeLower = mensaje.toLowerCase();

        String[] palabrasCine = {
                "cine", "pelicula", "film", "actor", "actriz", "director",
                "estreno", "cartelera", "taquilla", "oscar", "festival",
                "trailer", "critica", "resena", "cinefan", "movie"
        };

        for (String palabra : palabrasCine) {
            if (mensajeLower.contains(palabra)) {
                return true;
            }
        }

        return false;
    }

    // mostrar notificacion de sms
    private void mostrarNotificacionSms(Context context, String numeroRemitente,
                                        String mensaje, boolean esMensajeCine) {
        String titulo;
        String contenido;
        int icono;

        if (esMensajeCine) {
            // mensaje relacionado con cine - notificacion especial
            titulo = context.getString(R.string.notificacion_sms_cine_titulo);
            icono = R.drawable.ic_message_cinema;
        } else {
            // mensaje normal
            titulo = context.getString(R.string.notificacion_sms_titulo);
            icono = R.drawable.ic_message;
        }

        // formatear contenido
        String remitenteFormateado = formatearRemitente(numeroRemitente);
        contenido = context.getString(R.string.notificacion_sms_contenido,
                remitenteFormateado, truncarMensaje(mensaje, 100));

        // crear notificacion expandible para mensajes largos
        if (mensaje.length() > 50) {
            NotificacionHelper.mostrarNotificacionExpandible(
                    context,
                    Constantes.NOTIFICACION_ID_SMS,
                    titulo,
                    contenido,
                    mensaje, // mensaje completo expandido
                    icono,
                    context.getString(R.string.responder),
                    null // sin accion por ahora
            );
        } else {
            // notificacion simple
            NotificacionHelper.mostrarNotificacion(
                    context,
                    Constantes.NOTIFICACION_ID_SMS,
                    titulo,
                    contenido,
                    icono,
                    esMensajeCine // con sonido solo si es mensaje de cine
            );
        }

        Log.d(Constantes.TAG_RECEPTOR, "Notificacion de SMS mostrada");
    }

    // formatear numero del remitente
    private String formatearRemitente(String numeroRemitente) {
        if (numeroRemitente == null || numeroRemitente.isEmpty()) {
            return "Número desconocido";
        }

        // si es un nombre en lugar de numero
        if (!numeroRemitente.matches(".*\\d.*")) {
            return numeroRemitente;
        }

        // formatear numero
        String numeroLimpio = numeroRemitente.replaceAll("[^0-9+]", "");

        // numeros españoles
        if (numeroLimpio.startsWith("+34") && numeroLimpio.length() == 12) {
            return String.format("+34 %s %s %s",
                    numeroLimpio.substring(3, 6),
                    numeroLimpio.substring(6, 9),
                    numeroLimpio.substring(9, 12));
        } else if (numeroLimpio.length() == 9) {
            return String.format("%s %s %s",
                    numeroLimpio.substring(0, 3),
                    numeroLimpio.substring(3, 6),
                    numeroLimpio.substring(6, 9));
        }

        return numeroRemitente;
    }

    // truncar mensaje para notificacion
    private String truncarMensaje(String mensaje, int maxCaracteres) {
        if (mensaje.length() <= maxCaracteres) {
            return mensaje;
        }

        return mensaje.substring(0, maxCaracteres - 3) + "...";
    }

    // obtener vista previa del mensaje
    private String obtenerVistaPrevia(String mensaje) {
        // quitar saltos de linea y caracteres especiales
        String vista = mensaje.replaceAll("[\\r\\n]+", " ");
        vista = vista.trim();

        // truncar si es muy largo
        return truncarMensaje(vista, 80);
    }

    // analizar sentimiento del mensaje (simplificado)
    private String analizarSentimiento(String mensaje) {
        String mensajeLower = mensaje.toLowerCase();

        String[] palabrasPositivas = {
                "genial", "excelente", "fantastico", "increible", "perfecto",
                "me encanta", "me gusta", "recomiendo", "buenisima"
        };

        String[] palabrasNegativas = {
                "horrible", "pesima", "mala", "terrible", "aburrida",
                "no me gusta", "decepcionante", "perdida de tiempo"
        };

        int puntuacionPositiva = 0;
        int puntuacionNegativa = 0;

        for (String palabra : palabrasPositivas) {
            if (mensajeLower.contains(palabra)) {
                puntuacionPositiva++;
            }
        }

        for (String palabra : palabrasNegativas) {
            if (mensajeLower.contains(palabra)) {
                puntuacionNegativa++;
            }
        }

        if (puntuacionPositiva > puntuacionNegativa) {
            return "positivo";
        } else if (puntuacionNegativa > puntuacionPositiva) {
            return "negativo";
        } else {
            return "neutral";
        }
    }

    // verificar si el sms contiene informacion de cartelera
    private boolean contieneInfoCartelera(String mensaje) {
        String mensajeLower = mensaje.toLowerCase();

        String[] palabrasCartelera = {
                "cartelera", "horarios", "sesiones", "funciones",
                "entradas", "tickets", "reserva", "sala"
        };

        for (String palabra : palabrasCartelera) {
            if (mensajeLower.contains(palabra)) {
                return true;
            }
        }

        return false;
    }
}