package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo para la respuesta detallada de una película
 */
public class PeliculaDetailResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("pelicula")
    private Pelicula pelicula;
    
    @SerializedName("similares")
    private List<Pelicula> similares;
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Pelicula getPelicula() {
        return pelicula;
    }
    
    public List<Pelicula> getSimilares() {
        return similares;
    }
}