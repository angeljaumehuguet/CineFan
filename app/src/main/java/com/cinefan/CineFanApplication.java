package com.cinefan;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.cinefan.utils.LocaleHelper;

/**
 * Clase Application personalizada para gestionar configuraciones globales
 */
public class CineFanApplication extends Application {
    
    @Override
    protected void attachBaseContext(Context base) {
        // Aplicar el idioma configurado
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Recargar el idioma configurado cuando cambie la configuración
        LocaleHelper.onAttach(this);
    }
}