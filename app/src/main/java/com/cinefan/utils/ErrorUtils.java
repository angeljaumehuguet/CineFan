package com.cinefan.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cinefan.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Utilidades para manejar y mostrar errores
 */
public class ErrorUtils {

    public static final int ERROR_NETWORK = 0;
    public static final int ERROR_TIMEOUT = 1;
    public static final int ERROR_SERVER = 2;
    public static final int ERROR_UNKNOWN = 3;
    public static final int ERROR_AUTH = 4;
    
    /**
     * Maneja un error de Retrofit y muestra un mensaje apropiado
     * @param context Contexto para mostrar el mensaje
     * @param throwable Error producido
     */
    public static void handleApiError(Context context, Throwable throwable) {
        if (context == null) return;
        
        int errorType = getErrorType(throwable);
        String errorMessage = getErrorMessage(context, errorType);
        
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Maneja un error de Retrofit y muestra un Snackbar con un mensaje apropiado
     * @param view Vista donde mostrar el Snackbar
     * @param throwable Error producido
     */
    public static void handleApiErrorWithSnackbar(android.view.View view, Throwable throwable) {
        if (view == null) return;
        
        Context context = view.getContext();
        int errorType = getErrorType(throwable);
        String errorMessage = getErrorMessage(context, errorType);
        
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
    }
    
    /**
     * Determina el tipo de error basado en la excepción
     * @param throwable Error producido
     * @return Tipo de error
     */
    public static int getErrorType(Throwable throwable) {
        if (throwable instanceof UnknownHostException || throwable instanceof IOException) {
            return ERROR_NETWORK;
        } else if (throwable instanceof SocketTimeoutException) {
            return ERROR_TIMEOUT;
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if (httpException.code() == 401 || httpException.code() == 403) {
                return ERROR_AUTH;
            }
            return ERROR_SERVER;
        }
        return ERROR_UNKNOWN;
    }
    
    /**
     * Obtiene un mensaje de error basado en el tipo
     * @param context Contexto para obtener el string
     * @param errorType Tipo de error
     * @return Mensaje de error
     */
    @NonNull
    public static String getErrorMessage(Context context, int errorType) {
        switch (errorType) {
            case ERROR_NETWORK:
                return context.getString(R.string.error_network);
            case ERROR_TIMEOUT:
                return context.getString(R.string.error_timeout);
            case ERROR_SERVER:
                return context.getString(R.string.error_server);
            case ERROR_AUTH:
                return context.getString(R.string.error_auth);
            case ERROR_UNKNOWN:
            default:
                return context.getString(R.string.error_unknown);
        }
    }
}