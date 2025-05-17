package com.cinefan.utils;

import java.util.regex.Pattern;

/**
 * Utilidades para validación de datos
 */
public class ValidationUtils {
    
    private static final String EMAIL_PATTERN = 
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    /**
     * Valida si un correo electrónico es válido
     * @param email Correo a validar
     * @return true si es un correo válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
    
    /**
     * Valida si una contraseña es segura
     * @param password Contraseña a validar
     * @return true si es una contraseña segura, false en caso contrario
     */
    public static boolean isValidPassword(String password) {
        // Validar longitud mínima
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Validar que tenga al menos una mayúscula
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }
        
        // Validar que tenga al menos una minúscula
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            return false;
        }
        
        // Validar que tenga al menos un número
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return false;
        }
        
        // Validar que tenga al menos un carácter especial
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Verifica si un campo no está vacío
     * @param text Texto a verificar
     * @return true si no está vacío, false en caso contrario
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
    
    /**
     * Valida si dos contraseñas coinciden
     * @param password Contraseña
     * @param confirmPassword Confirmación de contraseña
     * @return true si coinciden, false en caso contrario
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
