package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para las listas de películas
 */
public class Lista {
    @SerializedName("id")
    private int id;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("fechaCreacion")
    private String fechaCreacion;
    
    @SerializedName("publica")
    private boolean publica;
    
    @SerializedName("totalPeliculas")
    private int totalPeliculas;
    
    public Lista() {
    }
    
    public Lista(String nombre, String descripcion, boolean publica) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.publica = publica;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public boolean isPublica() {
        return publica;
    }
    
    public void setPublica(boolean publica) {
        this.publica = publica;
    }
    
    public int getTotalPeliculas() {
        return totalPeliculas;
    }
    
    public void setTotalPeliculas(int totalPeliculas) {
        this.totalPeliculas = totalPeliculas;
    }
}
