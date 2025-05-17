package com.cinefan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Utilidad para gestionar el cambio de idioma en la aplicación
 */
public class LocaleHelper {
    
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    
    /**
     * Inicializa el idioma de la aplicación según el guardado en preferencias
     * @param context Contexto de la aplicación
     * @return Contexto con el idioma configurado
     */
    public static Context onAttach(Context context) {
        String language = getPersistedLanguage(context, Locale.getDefault().getLanguage());
        return setLocale(context, language);
    }
    
    /**
     * Establece un nuevo idioma para la aplicación
     * @param context Contexto de la aplicación
     * @param language Código del idioma a establecer (es, ca, en)
     * @return Contexto con el nuevo idioma
     */
    public static Context setLocale(Context context, String language) {
        persist(context, language);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return updateResourcesLegacy(context, language);
        }
    }
    
    /**
     * Obtiene el idioma guardado en preferencias
     * @param context Contexto de la aplicación
     * @param defaultLanguage Idioma por defecto si no hay ninguno guardado
     * @return Código del idioma guardado
     */
    private static String getPersistedLanguage(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }
    
    /**
     * Guarda el idioma seleccionado en preferencias
     * @param context Contexto de la aplicación
     * @param language Código del idioma a guardar
     */
    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }
    
    /**
     * Actualiza los recursos con el nuevo idioma (para Android 7.0+)
     * @param context Contexto de la aplicación
     * @param language Código del idioma
     * @return Contexto actualizado
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        
        return context.createConfigurationContext(configuration);
    }
    
    /**
     * Actualiza los recursos con el nuevo idioma (para versiones anteriores a Android 7.0)
     * @param context Contexto de la aplicación
     * @param language Código del idioma
     * @return Contexto actualizado
     */
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        
        return context;
    }
    
    /**
     * Obtiene el idioma actual de la aplicación
     * @param context Contexto de la aplicación
     * @return Código del idioma actual
     */
    public static String getLanguage(Context context) {
        return getPersistedLanguage(context, Locale.getDefault().getLanguage());
    }
}