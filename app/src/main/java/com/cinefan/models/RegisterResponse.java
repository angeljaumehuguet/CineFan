package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para la respuesta de registro
 */
public class RegisterResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("user")
    private User user;
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public User getUser() {
        return user;
    }
    
    public static class User {
        @SerializedName("id")
        private int id;
        
        @SerializedName("username")
        private String username;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("fullName")
        private String fullName;
        
        @SerializedName("token")
        private String token;
        
        public int getId() {
            return id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getFullName() {
            return fullName;
        }
        
        public String getToken() {
            return token;
        }
    }
}
