package com.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.cinefan.R;
import com.cinefan.utils.LocaleHelper;
import com.cinefan.utils.PreferenceManager;
import com.cinefan.utils.UiUtils;
import com.google.android.material.button.MaterialButton;

/**
 * Actividad de configuración de la aplicación
 */
public class SettingsActivity extends BaseActivity {
    
    private Toolbar toolbar;
    private RadioGroup rgLanguage;
    private RadioGroup rgTheme;
    private MaterialButton btnSave;
    private PreferenceManager preferenceManager;
    
    // Idiomas disponibles
    private static final String LANGUAGE_ES = "es";
    private static final String LANGUAGE_CA = "ca";
    private static final String LANGUAGE_EN = "en";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Inicializar preferencias
        preferenceManager = new PreferenceManager(this);
        
        // Configurar toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }
        
        // Inicializar vistas
        rgLanguage = findViewById(R.id.rgLanguage);
        rgTheme = findViewById(R.id.rgTheme);
        btnSave = findViewById(R.id.btnSave);
        
        // Cargar configuración actual
        loadCurrentSettings();
        
        // Configurar eventos
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }
    
    /**
     * Carga la configuración actual
     */
    private void loadCurrentSettings() {
        // Cargar idioma
        String currentLanguage = LocaleHelper.getLanguage(this);
        
        // Seleccionar el RadioButton correspondiente al idioma actual
        if (LANGUAGE_ES.equals(currentLanguage)) {
            rgLanguage.check(R.id.rbSpanish);
        } else if (LANGUAGE_CA.equals(currentLanguage)) {
            rgLanguage.check(R.id.rbCatalan);
        } else if (LANGUAGE_EN.equals(currentLanguage)) {
            rgLanguage.check(R.id.rbEnglish);
        }
        
        // Cargar tema (no implementado en esta versión)
        // En una versión futura se puede implementar el cambio de tema
        rgTheme.check(R.id.rbLightTheme);
    }
    
    /**
     * Guarda la configuración
     */
    private void saveSettings() {
        // Obtener idioma seleccionado
        String selectedLanguage = LANGUAGE_ES; // Por defecto español
        
        int checkedLanguageId = rgLanguage.getCheckedRadioButtonId();
        if (checkedLanguageId == R.id.rbCatalan) {
            selectedLanguage = LANGUAGE_CA;
        } else if (checkedLanguageId == R.id.rbEnglish) {
            selectedLanguage = LANGUAGE_EN;
        }
        
        // Verificar si ha cambiado el idioma
        String currentLanguage = LocaleHelper.getLanguage(this);
        boolean languageChanged = !selectedLanguage.equals(currentLanguage);
        
        // Guardar idioma si ha cambiado
        if (languageChanged) {
            // Cambiar idioma
            LocaleHelper.setLocale(this, selectedLanguage);
            
            // Mostrar mensaje
            UiUtils.showToast(this, R.string.settings_saved);
            
            // Reiniciar la actividad para aplicar el cambio de idioma
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(refresh);
            finish();
        } else {
            // No ha cambiado nada, simplemente volver atrás
            UiUtils.showToast(this, R.string.no_changes);
            finish();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}