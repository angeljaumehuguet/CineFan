package com.cinefan.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para la solicitud de creación/actualización de película
 */
public class PeliculaRequest {
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("titulo")
    private String titulo;
    
    @SerializedName("anio")
    private int anio;
    
    @SerializedName("director")
    private String director;
    
    @SerializedName("genero")
    private String genero;
    
    @SerializedName("sinopsis")
    private String sinopsis;
    
    @SerializedName("imagenUrl")
    private String imagenUrl;
    
    @SerializedName("trailerUrl")
    private String trailerUrl;
    
    @SerializedName("duracion")
    private int duracion;
    
    // Constructor para crear una nueva película
    public PeliculaRequest(String titulo, int anio, String director, String genero, 
                          String sinopsis, String imagenUrl, String trailerUrl, int duracion) {
        this.titulo = titulo;
        this.anio = anio;
        this.director = director;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.imagenUrl = imagenUrl;
        this.trailerUrl = trailerUrl;
        this.duracion = duracion;
    }
    
    // Constructor para actualizar una película existente
    public PeliculaRequest(int id, String titulo, int anio, String director, String genero, 
                          String sinopsis, String imagenUrl, String trailerUrl, int duracion) {
        this.id = id;
        this.titulo = titulo;
        this.anio = anio;
        this.director = director;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.imagenUrl = imagenUrl;
        this.trailerUrl = trailerUrl;
        this.duracion = duracion;
    }
}