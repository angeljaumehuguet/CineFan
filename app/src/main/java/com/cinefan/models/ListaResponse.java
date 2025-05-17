package com.cinefan.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para la respuesta de listas
 */
public class ListaResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private List<Lista> data;
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public List<Lista> getData() {
        return data;
    }
}
