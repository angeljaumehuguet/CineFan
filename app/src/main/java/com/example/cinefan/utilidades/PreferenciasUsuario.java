package com.example.cinefan.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * clase para manejar las preferencias del usuario
 */
public class PreferenciasUsuario {
    
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context contexto;
    
    // constructor
    public PreferenciasUsuario(Context contexto) {
        this.contexto = contexto;
        preferences = contexto.getSharedPreferences(Constantes.PREF_NOMBRE, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    
    // guardar datos del usuario despues del login
    public void guardarDatosUsuario(String token, int idUsuario, String nombreUsuario, 
                                   String email, String nombreCompleto) {
        editor.putString(Constantes.PREF_TOKEN, token);
        editor.putInt(Constantes.PREF_ID_USUARIO, idUsuario);
        editor.putString(Constantes.PREF_NOMBRE_USUARIO, nombreUsuario);
        editor.putString(Constantes.PREF_EMAIL, email);
        editor.putString(Constantes.PREF_NOMBRE_COMPLETO, nombreCompleto);
        editor.apply();
    }
    
    // obtener token
    public String obtenerToken() {
        return preferences.getString(Constantes.PREF_TOKEN, "");
    }
    
    // obtener id usuario
    public int obtenerIdUsuario() {
        return preferences.getInt(Constantes.PREF_ID_USUARIO, -1);
    }
    
    // obtener nombre usuario
    public String obtenerNombreUsuario() {
        return preferences.getString(Constantes.PREF_NOMBRE_USUARIO, "");
    }
    
    // obtener email
    public String obtenerEmail() {
        return preferences.getString(Constantes.PREF_EMAIL, "");
    }
    
    // obtener nombre completo
    public String obtenerNombreCompleto() {
        return preferences.getString(Constantes.PREF_NOMBRE_COMPLETO, "");
    }
    
    // verificar si hay sesion activa
    public boolean haySesionActiva() {
        String token = obtenerToken();
        return token != null && !token.isEmpty();
    }
    
    // cerrar sesion
    public void cerrarSesion() {
        editor.remove(Constantes.PREF_TOKEN);
        editor.remove(Constantes.PREF_ID_USUARIO);
        editor.remove(Constantes.PREF_NOMBRE_USUARIO);
        editor.remove(Constantes.PREF_EMAIL);
        editor.remove(Constantes.PREF_NOMBRE_COMPLETO);
        editor.apply();
    }
    
    // guardar estado de la musica
    public void guardarEstadoMusica(boolean activa) {
        editor.putBoolean(Constantes.PREF_MUSICA_ACTIVA, activa);
        editor.apply();
    }
    
    // obtener estado de la musica
    public boolean obtenerEstadoMusica() {
        return preferences.getBoolean(Constantes.PREF_MUSICA_ACTIVA, true);
    }
}