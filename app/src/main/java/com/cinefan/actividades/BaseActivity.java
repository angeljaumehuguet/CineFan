package com.cinefan.actividades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cinefan.R;
import com.cinefan.utils.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

/**
 * Actividad base que proporciona funcionalidades comunes para todas las actividades
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver networkReceiver;
    private Snackbar connectivitySnackbar;
    private boolean isNetworkReceiverRegistered = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar el receptor de cambios de conectividad
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateConnectionStatus();
            }
        };
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Registrar receptor para cambios de conectividad
        registerNetworkReceiver();
        
        // Comprobar el estado de la conexión al iniciar
        updateConnectionStatus();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        // Desregistrar receptor cuando la actividad no está visible
        unregisterNetworkReceiver();
        
        // Ocultar snackbar si está visible
        dismissConnectivitySnackbar();
    }
    
    /**
     * Registra el receptor para cambios de conectividad
     */
    private void registerNetworkReceiver() {
        if (!isNetworkReceiverRegistered) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkReceiver, filter);
            isNetworkReceiverRegistered = true;
        }
    }
    
    /**
     * Desregistra el receptor para cambios de conectividad
     */
    private void unregisterNetworkReceiver() {
        if (isNetworkReceiverRegistered) {
            unregisterReceiver(networkReceiver);
            isNetworkReceiverRegistered = false;
        }
    }
    
    /**
     * Actualiza la UI según el estado de la conexión
     */
    protected void updateConnectionStatus() {
        boolean isConnected = NetworkUtils.isNetworkAvailable(this);
        
        if (!isConnected) {
            showNoConnectionSnackbar();
        } else {
            dismissConnectivitySnackbar();
        }
    }
    
    /**
     * Muestra un snackbar indicando que no hay conexión
     */
    protected void showNoConnectionSnackbar() {
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            connectivitySnackbar = Snackbar.make(
                    rootView,
                    R.string.error_no_internet,
                    Snackbar.LENGTH_INDEFINITE);
            
            // Configurar acción para reintentar
            connectivitySnackbar.setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateConnectionStatus();
                }
            });
            
            // Personalizar estilo
            View snackbarView = connectivitySnackbar.getView();
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(2);
            
            connectivitySnackbar.show();
        }
    }
    
    /**
     * Oculta el snackbar de conectividad si está visible
     */
    protected void dismissConnectivitySnackbar() {
        if (connectivitySnackbar != null && connectivitySnackbar.isShown()) {
            connectivitySnackbar.dismiss();
        }
    }
    
    /**
     * Comprueba si hay conexión antes de realizar una operación de red
     * @return true si hay conexión, false en caso contrario
     */
    protected boolean checkNetworkBeforeOperation() {
        boolean isConnected = NetworkUtils.isNetworkAvailable(this);
        if (!isConnected) {
            showNoConnectionSnackbar();
        }
        return isConnected;
    }
}