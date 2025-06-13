package com.example.cinefan.modelos;

import java.io.Serializable;

/**
 * ✅ MODELO PARA GÉNEROS DE PELÍCULAS
 */
public class Genero implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private String colorHex;
    private boolean activo;

    // ========== CONSTRUCTORES ==========

    public Genero() {
        this.activo = true;
    }

    public Genero(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.activo = true;
    }

    // ========== GETTERS Y SETTERS ==========

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

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // ========== MÉTODOS AUXILIARES ==========

    @Override
    public String toString() {
        return nombre; // Para mostrar en dropdowns
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Genero genero = (Genero) obj;
        return id == genero.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}