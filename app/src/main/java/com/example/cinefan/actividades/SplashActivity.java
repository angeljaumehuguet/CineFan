package com.example.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinefan.R;
import com.example.cinefan.utilidades.PreferenciasUsuario;

/**
 * actividad de splash screen - primera pantalla de la app
 */
public class SplashActivity extends AppCompatActivity {
    
    // duracion del splash en milisegundos
    private static final int DURACION_SPLASH = 3000;
    
    // vistas
    private TextView txtLogo;
    private TextView txtSubtitulo;
    
    // utilidades
    private PreferenciasUsuario preferencias;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // inicializar preferencias
        preferencias = new PreferenciasUsuario(this);
        
        // enlazar vistas
        txtLogo = findViewById(R.id.txt_logo);
        txtSubtitulo = findViewById(R.id.txt_subtitulo);
        
        // configurar animaciones
        configurarAnimaciones();
        
        // navegar despues del tiempo especificado
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navegarASiguientePantalla();
            }
        }, DURACION_SPLASH);
    }
    
    // configurar animaciones de entrada
    private void configurarAnimaciones() {
        // animacion de fade in para el logo
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1500);
        
        // animacion de slide up para el subtitulo
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        slideUp.setDuration(1000);
        slideUp.setStartOffset(500);
        
        // aplicar animaciones
        txtLogo.startAnimation(fadeIn);
        txtSubtitulo.startAnimation(slideUp);
    }
    
    // navegar a la siguiente pantalla segun el estado de sesion
    private void navegarASiguientePantalla() {
        Intent intent;
        
        if (preferencias.haySesionActiva()) {
            // ir a main activity si hay sesion activa
            intent = new Intent(this, MainActivity.class);
        } else {
            // ir a login si no hay sesion
            intent = new Intent(this, LoginActivity.class);
        }
        
        startActivity(intent);
        finish();
        
        // transicion suave
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    
    @Override
    public void onBackPressed() {
        // no permitir retroceso en splash
        // no llamar super.onBackPressed()
    }
}