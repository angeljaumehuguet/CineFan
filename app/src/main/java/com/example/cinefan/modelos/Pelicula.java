package com.example.cinefan.modelos;

import java.io.Serializable;
import java.util.List;

/**
 * modelo completo para representar una pelicula
 */
public class Pelicula implements Serializable {

    private int id;
    private String titulo;
    private String director;
    private int anoLanzamiento;
    private int duracionMinutos;
    private String genero;
    private String sinopsis;
    private String imagenUrl;
    private String trailerUrl;
    private String paisOrigen;
    private String idioma;

    // estadisticas y puntuaciones
    private double puntuacionPromedio;
    private int totalResenas;
    private int totalVotos;
    private int totalFavoritos;
    private double recaudacionMundial;
    private String clasificacionEdad;

    // metadatos
    private String fechaEstreno;
    private String fechaCreacion;
    private String fechaActualizacion;
    private int idUsuarioCreador;

    // reparto principal
    private String actorPrincipal;
    private String actrizPrincipal;
    private String reparto;

    // relaciones
    private Usuario usuarioCreador;
    private List<Resena> resenas;
    private List<Usuario> usuariosFavoritos;

    // campos calculados
    private boolean esFavorita;
    private boolean tieneResenaUsuario;
    private int resenaUsuarioId;

    // constructores
    public Pelicula() {
        this.puntuacionPromedio = 0.0;
        this.totalResenas = 0;
        this.totalVotos = 0;
        this.totalFavoritos = 0;
        this.esFavorita = false;
        this.tieneResenaUsuario = false;
        this.recaudacionMundial = 0.0;
    }

    public Pelicula(String titulo, String director, int anoLanzamiento) {
        this();
        this.titulo = titulo;
        this.director = director;
        this.anoLanzamiento = anoLanzamiento;
    }

    // getters y setters
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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAnoLanzamiento() {
        return anoLanzamiento;
    }

    public void setAnoLanzamiento(int anoLanzamiento) {
        this.anoLanzamiento = anoLanzamiento;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
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

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public double getPuntuacionPromedio() {
        return puntuacionPromedio;
    }

    public void setPuntuacionPromedio(double puntuacionPromedio) {
        this.puntuacionPromedio = puntuacionPromedio;
    }

    public int getTotalResenas() {
        return totalResenas;
    }

    public void setTotalResenas(int totalResenas) {
        this.totalResenas = totalResenas;
    }

    public int getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(int totalVotos) {
        this.totalVotos = totalVotos;
    }

    public int getTotalFavoritos() {
        return totalFavoritos;
    }

    public void setTotalFavoritos(int totalFavoritos) {
        this.totalFavoritos = totalFavoritos;
    }

    public double getRecaudacionMundial() {
        return recaudacionMundial;
    }

    public void setRecaudacionMundial(double recaudacionMundial) {
        this.recaudacionMundial = recaudacionMundial;
    }

    public String getClasificacionEdad() {
        return clasificacionEdad;
    }

    public void setClasificacionEdad(String clasificacionEdad) {
        this.clasificacionEdad = clasificacionEdad;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(int idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public String getActorPrincipal() {
        return actorPrincipal;
    }

    public void setActorPrincipal(String actorPrincipal) {
        this.actorPrincipal = actorPrincipal;
    }

    public String getActrizPrincipal() {
        return actrizPrincipal;
    }

    public void setActrizPrincipal(String actrizPrincipal) {
        this.actrizPrincipal = actrizPrincipal;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public Usuario getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(Usuario usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public List<Resena> getResenas() {
        return resenas;
    }

    public void setResenas(List<Resena> resenas) {
        this.resenas = resenas;
    }

    public List<Usuario> getUsuariosFavoritos() {
        return usuariosFavoritos;
    }

    public void setUsuariosFavoritos(List<Usuario> usuariosFavoritos) {
        this.usuariosFavoritos = usuariosFavoritos;
    }

    public boolean isEsFavorita() {
        return esFavorita;
    }

    public void setEsFavorita(boolean esFavorita) {
        this.esFavorita = esFavorita;
    }

    public boolean isTieneResenaUsuario() {
        return tieneResenaUsuario;
    }

    public void setTieneResenaUsuario(boolean tieneResenaUsuario) {
        this.tieneResenaUsuario = tieneResenaUsuario;
    }

    public int getResenaUsuarioId() {
        return resenaUsuarioId;
    }

    public void setResenaUsuarioId(int resenaUsuarioId) {
        this.resenaUsuarioId = resenaUsuarioId;
    }

    // metodos de utilidad

    // obtener duracion formateada
    public String getDuracionFormateada() {
        if (duracionMinutos <= 0) {
            return "N/A";
        }

        int horas = duracionMinutos / 60;
        int minutos = duracionMinutos % 60;

        if (horas > 0) {
            if (minutos > 0) {
                return String.format("%dh %dmin", horas, minutos);
            } else {
                return String.format("%dh", horas);
            }
        } else {
            return String.format("%dmin", minutos);
        }
    }

    // obtener puntuacion formateada
    public String getPuntuacionFormateada() {
        if (puntuacionPromedio > 0) {
            return String.format("%.1f", puntuacionPromedio);
        }
        return "N/A";
    }

    // obtener texto de resenas
    public String getTextoResenas() {
        if (totalResenas == 0) {
            return "Sin reseñas";
        } else if (totalResenas == 1) {
            return "1 reseña";
        } else {
            return totalResenas + " reseñas";
        }
    }

    // verificar si es pelicula reciente
    public boolean esPeliculaReciente() {
        int anoActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return anoLanzamiento >= (anoActual - 2);
    }

    // verificar si es pelicula clasica
    public boolean esPeliculaClasica() {
        int anoActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return anoLanzamiento <= (anoActual - 25);
    }

    // obtener categoria por edad
    public String getCategoriaEdad() {
        int anoActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int antiguedad = anoActual - anoLanzamiento;

        if (antiguedad <= 2) {
            return "Reciente";
        } else if (antiguedad <= 10) {
            return "Moderna";
        } else if (antiguedad <= 25) {
            return "Establecida";
        } else {
            return "Clásica";
        }
    }

    // obtener color para la puntuacion
    public String getColorPuntuacion() {
        if (puntuacionPromedio >= 4.5) {
            return "#4CAF50"; // verde excelente
        } else if (puntuacionPromedio >= 3.5) {
            return "#8BC34A"; // verde claro buena
        } else if (puntuacionPromedio >= 2.5) {
            return "#FFC107"; // amarillo regular
        } else if (puntuacionPromedio >= 1.5) {
            return "#FF9800"; // naranja mala
        } else if (puntuacionPromedio > 0) {
            return "#F44336"; // rojo muy mala
        } else {
            return "#757575"; // gris sin puntuacion
        }
    }

    // verificar si tiene imagen
    public boolean tieneImagen() {
        return imagenUrl != null && !imagenUrl.trim().isEmpty();
    }

    // verificar si tiene trailer
    public boolean tieneTrailer() {
        return trailerUrl != null && !trailerUrl.trim().isEmpty();
    }

    // verificar si tiene sinopsis
    public boolean tieneSinopsis() {
        return sinopsis != null && !sinopsis.trim().isEmpty();
    }

    // obtener titulo con ano
    public String getTituloConAno() {
        return titulo + " (" + anoLanzamiento + ")";
    }

    // obtener informacion basica
    public String getInformacionBasica() {
        StringBuilder info = new StringBuilder();

        if (director != null && !director.isEmpty()) {
            info.append(director).append(" • ");
        }

        info.append(anoLanzamiento);

        if (duracionMinutos > 0) {
            info.append(" • ").append(getDuracionFormateada());
        }

        return info.toString();
    }

    // obtener recaudacion formateada
    public String getRecaudacionFormateada() {
        if (recaudacionMundial <= 0) {
            return "N/A";
        }

        if (recaudacionMundial >= 1_000_000_000) {
            return String.format("$%.1fB", recaudacionMundial / 1_000_000_000);
        } else if (recaudacionMundial >= 1_000_000) {
            return String.format("$%.1fM", recaudacionMundial / 1_000_000);
        } else {
            return String.format("$%.0f", recaudacionMundial);
        }
    }

    // verificar si es pelicula exitosa (alta puntuacion y muchas resenas)
    public boolean esPeliculaExitosa() {
        return puntuacionPromedio >= 4.0 && totalResenas >= 10;
    }

    // verificar si es pelicula popular (muchas resenas)
    public boolean esPeliculaPopular() {
        return totalResenas >= 20 || totalFavoritos >= 15;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", anoLanzamiento=" + anoLanzamiento +
                ", genero='" + genero + '\'' +
                ", puntuacionPromedio=" + puntuacionPromedio +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Pelicula pelicula = (Pelicula) obj;
        return id == pelicula.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}