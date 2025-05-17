package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para respuesta general de API
 */
public class ApiResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("id")
    private Integer id;
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Integer getId() {
        return id;
    }
    
    public boolean isSuccess() {
        return "success".equals(status);
    }
}
