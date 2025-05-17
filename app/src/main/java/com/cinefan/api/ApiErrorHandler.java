package com.cinefan.api;

import android.content.Context;
import android.content.Intent;

import com.cinefan.actividades.LoginActivity;
import com.cinefan.models.ApiResponse;
import com.cinefan.utils.ErrorUtils;
import com.cinefan.utils.PreferenceManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Manejador de errores específicos de la API
 */
public class ApiErrorHandler {

    /**
     * Maneja una respuesta de error HTTP
     * @param context Contexto de la aplicación
     * @param throwable Error producido
     * @return true si se ha manejado el error, false en caso contrario
     */
    public static boolean handleError(Context context, Throwable throwable) {
        if (context == null || throwable == null) {
            return false;
        }
        
        // Manejo de error de autenticación (401)
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            
            // Si es error de autenticación, redirigir a login
            if (httpException.code() == 401) {
                handleAuthError(context);
                return true;
            }
            
            // Intentar parsear el cuerpo del error
            try {
                ResponseBody errorBody = httpException.response().errorBody();
                if (errorBody != null) {
                    String errorString = errorBody.string();
                    try {
                        JSONObject errorJson = new JSONObject(errorString);
                        String message = errorJson.optString("message", null);
                        if (message != null) {
                            ErrorUtils.handleApiErrorWithSnackbar(null, new Exception(message));
                            return true;
                        }
                    } catch (Exception e) {
                        // Error al parsear JSON, continuar con manejo genérico
                    }
                }
            } catch (IOException e) {
                // Error al leer el cuerpo, continuar con manejo genérico
            }
        }
        
        // Manejo genérico de errores
        ErrorUtils.handleApiError(context, throwable);
        return true;
    }
    
    /**
     * Maneja un error específico de respuesta de la API
     * @param context Contexto de la aplicación
     * @param response Respuesta de la API
     * @return true si la respuesta indica error, false si es exitosa
     */
    public static boolean handleApiResponse(Context context, ApiResponse response) {
        if (response == null) {
            ErrorUtils.handleApiError(context, new Exception("Respuesta nula"));
            return true;
        }
        
        if (!"success".equals(response.getStatus())) {
            ErrorUtils.handleApiError(context, new Exception(response.getMessage()));
            return true;
        }
        
        return false; // No hay error
    }
    
    /**
     * Maneja un error de autenticación redirigiendo al login
     * @param context Contexto de la aplicación
     */
    private static void handleAuthError(Context context) {
        // Limpiar token y sesión
        ApiClient.clearToken();
        new PreferenceManager(context).clearSession();
        
        // Redirigir a la pantalla de login
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}