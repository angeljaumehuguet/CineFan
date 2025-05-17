package com.cinefan.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cinefan.R;

/**
 * Diálogo de carga personalizado que muestra un progreso circular
 */
public class LoadingDialog {
    
    private Dialog dialog;
    private TextView txtMessage;
    
    /**
     * Constructor que inicializa el diálogo
     * @param context Contexto para crear el diálogo
     */
    public LoadingDialog(Context context) {
        dialog = new Dialog(context);
        
        // Configurar diálogo sin título
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        
        // Inflar y configurar la vista
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        txtMessage = view.findViewById(R.id.txtMessage);
        
        dialog.setContentView(view);
        
        // Configurar fondo transparente
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    
    /**
     * Muestra el diálogo con un mensaje por defecto
     */
    public void show() {
        show(null);
    }
    
    /**
     * Muestra el diálogo con un mensaje personalizado
     * @param message Mensaje a mostrar
     */
    public void show(String message) {
        if (message != null && !message.isEmpty()) {
            txtMessage.setText(message);
            txtMessage.setVisibility(View.VISIBLE);
        } else {
            txtMessage.setVisibility(View.GONE);
        }
        
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }
    
    /**
     * Cambia el mensaje del diálogo
     * @param message Nuevo mensaje
     */
    public void updateMessage(String message) {
        if (txtMessage != null) {
            if (message != null && !message.isEmpty()) {
                txtMessage.setText(message);
                txtMessage.setVisibility(View.VISIBLE);
            } else {
                txtMessage.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * Oculta el diálogo
     */
    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    
    /**
     * Verifica si el diálogo está mostrándose
     * @return true si está visible, false en caso contrario
     */
    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}