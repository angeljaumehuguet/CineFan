package com.cinefan.repositories;

import android.content.Context;

import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.ApiResponse;
import com.cinefan.models.LoginRequest;
import com.cinefan.models.LoginResponse;
import com.cinefan.models.RegisterRequest;
import com.cinefan.models.RegisterResponse;
import com.cinefan.utils.PreferenceManager;

import retrofit2.Callback;

/**
 * Repositorio para la gestión de usuarios
 */
public class UserRepository {
    
    private static UserRepository instance;
    private CineFanApi apiService;
    private PreferenceManager preferenceManager;
    
    /**
     * Constructor privado (singleton)
     */
    private UserRepository(Context context) {
        apiService = ApiClient.getClient().create(CineFanApi.class);
        preferenceManager = new PreferenceManager(context);
    }
    
    /**
     * Obtiene la instancia única del repositorio
     * @param context Contexto de la aplicación
     * @return Instancia del repositorio
     */
    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Inicia sesión con un usuario
     * @param username Nombre de usuario o correo
     * @param password Contraseña
     * @param callback Callback para manejar la respuesta
     */
    public void login(String username, String password, Callback<LoginResponse> callback) {
        LoginRequest request = new LoginRequest(username, password);
        apiService.login(request).enqueue(callback);
    }
    
    /**
     * Registra un nuevo usuario
     * @param username Nombre de usuario
     * @param email Correo electrónico
     * @param password Contraseña
     * @param fullName Nombre completo
     * @param callback Callback para manejar la respuesta
     */
    public void register(String username, String email, String password, String fullName, 
                          Callback<RegisterResponse> callback) {
        RegisterRequest request = new RegisterRequest(username, email, password, fullName);
        apiService.register(request).enqueue(callback);
    }
    
    /**
     * Obtiene el perfil del usuario actual
     * @param callback Callback para manejar la respuesta
     */
    public void getProfile(Callback<ApiResponse> callback) {
        apiService.getProfile().enqueue(callback);
    }
    
    /**
     * Actualiza el perfil del usuario
     * @param profileData Datos del perfil a actualizar
     * @param callback Callback para manejar la respuesta
     */
    public void updateProfile(Object profileData, Callback<ApiResponse> callback) {
        apiService.updateProfile(profileData).enqueue(callback);
    }
    
    /**
     * Cambia la contraseña del usuario
     * @param oldPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @param callback Callback para manejar la respuesta
     */
    public void changePassword(String oldPassword, String newPassword, 
                               Callback<ApiResponse> callback) {
        apiService.changePassword(oldPassword, newPassword).enqueue(callback);
    }
    
    /**
     * Comprueba si el usuario está logueado
     * @return true si está logueado, false en caso contrario
     */
    public boolean isLoggedIn() {
        return preferenceManager.isLoggedIn();
    }
    
    /**
     * Guarda los datos de sesión de un usuario
     * @param userId ID del usuario
     * @param username Nombre de usuario
     * @param email Correo electrónico
     * @param fullName Nombre completo
     * @param token Token de autenticación
     */
    public void saveSession(int userId, String username, String email, String fullName, 
                             String token) {
        preferenceManager.setLoggedIn(true);
        preferenceManager.setUserData(userId, username, email, fullName, token);
        ApiClient.setToken(token);
    }
    
    /**
     * Cierra la sesión del usuario
     */
    public void logout() {
        preferenceManager.clearSession();
        ApiClient.clearToken();
    }
    
    /**
     * Obtiene el ID del usuario actual
     * @return ID del usuario
     */
    public int getUserId() {
        return preferenceManager.getUserId();
    }
    
    /**
     * Obtiene el nombre de usuario
     * @return Nombre de usuario
     */
    public String getUsername() {
        return preferenceManager.getUsername();
    }
    
    /**
     * Obtiene el correo electrónico
     * @return Correo electrónico
     */
    public String getEmail() {
        return preferenceManager.getEmail();
    }
    
    /**
     * Obtiene el nombre completo
     * @return Nombre completo
     */
    public String getFullName() {
        return preferenceManager.getFullName();
    }
    
    /**
     * Obtiene el token de autenticación
     * @return Token
     */
    public String getToken() {
        return preferenceManager.getToken();
    }
}