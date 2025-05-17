package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo para la respuesta de detalles de lista
 */
public class ListaDetailResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("lista")
    private Lista lista;
    
    @SerializedName("peliculas")
    private List<Pelicula> peliculas;
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Lista getLista() {
        return lista;
    }
    
    public List<Pelicula> getPeliculas() {
        return peliculas;
    }
}