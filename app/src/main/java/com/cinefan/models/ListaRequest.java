package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para la solicitud de creación de lista
 */
public class ListaRequest {
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("publica")
    private boolean publica;
    
    public ListaRequest(String nombre, String descripcion, boolean publica) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.publica = publica;
    }
}