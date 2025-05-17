package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para la solicitud de login
 */
public class LoginRequest {
    @SerializedName("username")
    private String username;
    
    @SerializedName("password")
    private String password;
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
