package com.cinefan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gestor de preferencias para guardar la sesión del usuario
 */
public class PreferenceManager {
    
    private static final String PREF_NAME = "CineFanPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_TOKEN = "token";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    
    public PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public void setUserData(int userId, String username, String email, String fullName, String token) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }
    
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }
    
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }
    
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }
    
    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "");
    }
    
    public String getToken() {
        return prefs.getString(KEY_TOKEN, "");
    }
    
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
