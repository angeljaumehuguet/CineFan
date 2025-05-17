package com.cinefan.utils;

import android.content.Context;
import android.widget.EditText;

import com.cinefan.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Utilidad para validar campos de formularios
 */
public class InputValidator {
    
    /**
     * Valida si un campo no está vacío
     * @param context Contexto para obtener mensajes de error
     * @param editText Campo a validar
     * @param errorMessageResId ID del recurso con el mensaje de error
     * @return true si es válido, false en caso contrario
     */
    public static boolean validateNotEmpty(Context context, EditText editText, int errorMessageResId) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            setError(editText, context.getString(errorMessageResId));
            return false;
        }
        
        clearError(editText);
        return true;
    }
    
    /**
     * Valida si un campo de email es válido
     * @param context Contexto para obtener mensajes de error
     * @param editText Campo a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validateEmail(Context context, EditText editText) {
        String email = editText.getText().toString().trim();
        
        // Validar que no esté vacío
        if (email.isEmpty()) {
            setError(editText, context.getString(R.string.error_empty_email));
            return false;
        }
        
        // Validar formato
        if (!ValidationUtils.isValidEmail(email)) {
            setError(editText, context.getString(R.string.error_invalid_email));
            return false;
        }
        
        clearError(editText);
        return true;
    }
    
    /**
     * Valida si un campo de contraseña es seguro
     * @param context Contexto para obtener mensajes de error
     * @param editText Campo a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validatePassword(Context context, EditText editText) {
        String password = editText.getText().toString();
        
        // Validar que no esté vacío
        if (password.isEmpty()) {
            setError(editText, context.getString(R.string.error_empty_password));
            return false;
        }
        
        // Validar seguridad
        if (!ValidationUtils.isValidPassword(password)) {
            setError(editText, context.getString(R.string.error_password_requirements));
            return false;
        }
        
        clearError(editText);
        return true;
    }
    
    /**
     * Valida si dos contraseñas coinciden
     * @param context Contexto para obtener mensajes de error
     * @param passwordEditText Campo de contraseña
     * @param confirmPasswordEditText Campo de confirmación
     * @return true si coinciden, false en caso contrario
     */
    public static boolean validatePasswordMatch(Context context, EditText passwordEditText, 
                                               EditText confirmPasswordEditText) {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        
        // Validar que no esté vacío
        if (confirmPassword.isEmpty()) {
            setError(confirmPasswordEditText, context.getString(R.string.error_empty_password));
            return false;
        }
        
        // Validar coincidencia
        if (!password.equals(confirmPassword)) {
            setError(confirmPasswordEditText, context.getString(R.string.error_password_mismatch));
            return false;
        }
        
        clearError(confirmPasswordEditText);
        return true;
    }
    
    /**
     * Valida si un campo numérico está en un rango específico
     * @param context Contexto para obtener mensajes de error
     * @param editText Campo a validar
     * @param min Valor mínimo
     * @param max Valor máximo
     * @param errorMessageResId ID del recurso con el mensaje de error
     * @return true si es válido, false en caso contrario
     */
    public static boolean validateNumberRange(Context context, EditText editText, 
                                             int min, int max, int errorMessageResId) {
        String text = editText.getText().toString().trim();
        
        // Validar que no esté vacío
        if (text.isEmpty()) {
            setError(editText, context.getString(errorMessageResId));
            return false;
        }
        
        try {
            int value = Integer.parseInt(text);
            if (value < min || value > max) {
                setError(editText, context.getString(errorMessageResId));
                return false;
            }
        } catch (NumberFormatException e) {
            setError(editText, context.getString(errorMessageResId));
            return false;
        }
        
        clearError(editText);
        return true;
    }
    
    /**
     * Valida si una URL es válida
     * @param context Contexto para obtener mensajes de error
     * @param editText Campo a validar
     * @param errorMessageResId ID del recurso con el mensaje de error
     * @return true si es válido, false en caso contrario
     */
    public static boolean validateUrl(Context context, EditText editText, int errorMessageResId) {
        String url = editText.getText().toString().trim();
        
        // Validar que no esté vacío
        if (url.isEmpty()) {
            setError(editText, context.getString(errorMessageResId));
            return false;
        }
        
        // Validar formato simple (debe comenzar con http:// o https://)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            setError(editText, context.getString(errorMessageResId));
            return false;
        }
        
        clearError(editText);
        return true;
    }
    
    /**
     * Establece un mensaje de error en un campo
     * @param editText Campo donde mostrar el error
     * @param errorMessage Mensaje de error
     */
    private static void setError(EditText editText, String errorMessage) {
        // Si el campo está dentro de un TextInputLayout, establecer el error ahí
        if (editText.getParent().getParent() instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
            textInputLayout.setError(errorMessage);
        } else {
            // De lo contrario, establecer el error en el EditText
            editText.setError(errorMessage);
        }
    }
    
    /**
     * Limpia el error de un campo
     * @param editText Campo donde limpiar el error
     */
    private static void clearError(EditText editText) {
        // Si el campo está dentro de un TextInputLayout, limpiar el error ahí
        if (editText.getParent().getParent() instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) editText.getParent().getParent();
            textInputLayout.setError(null);
        } else {
            // De lo contrario, limpiar el error en el EditText
            editText.setError(null);
        }
    }
}