package com.cinefan.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para la película
 */
public class Pelicula {
    @SerializedName("id")
    private int id;
    
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
    
    @SerializedName("valoracion")
    private float valoracion;
    
    @SerializedName("totalValoraciones")
    private int totalValoraciones;
    
    @SerializedName("imagenUrl")
    private String imagenUrl;
    
    @SerializedName("trailerUrl")
    private String trailerUrl;
    
    @SerializedName("duracion")
    private int duracion;
    
    // Getters y setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public int getAnio() {
        return anio;
    }
    
    public void setAnio(int anio) {
        this.anio = anio;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getSinopsis() {
        return sinopsis;
    }
    
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
    
    public float getValoracion() {
        return valoracion;
    }
    
    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }
    
    public int getTotalValoraciones() {
        return totalValoraciones;
    }
    
    public void setTotalValoraciones(int totalValoraciones) {
        this.totalValoraciones = totalValoraciones;
    }
    
    public String getImagenUrl() {
        return imagenUrl;
    }
    
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
    
    public int getDuracion() {
        return duracion;
    }
    
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
