package com.example.cinefan.modelos;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * modelo completo para representar una resena
 */
public class Resena implements Serializable {

    private int id;
    private int puntuacion;
    private String textoResena;
    private String fechaResena;
    private String fechaActualizacion;
    private int likes;
    private int dislikes;
    private boolean esLikeado;
    private boolean esDislikeado;
    private boolean esSpoiler;
    private boolean esVerificada;
    private boolean esEditada;

    // relaciones
    private Usuario usuario;
    private Pelicula pelicula;

    // campos adicionales para vista
    private String nombreUsuario;
    private String tituloPelicula;
    private String avatarUsuario;
    private String imagenPelicula;

    // metadatos
    private int idUsuario;
    private int idPelicula;
    private String ipCreacion;
    private String dispositivo;

    // estadisticas de la resena
    private int totalComentarios;
    private int totalCompartidas;
    private int totalReportes;
    private double puntuacionUtilidad; // que tan util consideran otros usuarios esta resena

    // campos calculados
    private boolean esMia; // si la resena pertenece al usuario actual
    private boolean puedeEditar;
    private boolean puedeEliminar;
    private String tiempoTranscurrido;

    // formateadores de fecha
    private static final SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    // constructores
    public Resena() {
        this.likes = 0;
        this.dislikes = 0;
        this.esLikeado = false;
        this.esDislikeado = false;
        this.esSpoiler = false;
        this.esVerificada = false;
        this.esEditada = false;
        this.totalComentarios = 0;
        this.totalCompartidas = 0;
        this.totalReportes = 0;
        this.puntuacionUtilidad = 0.0;
        this.esMia = false;
        this.puedeEditar = false;
        this.puedeEliminar = false;
    }

    public Resena(int puntuacion, String textoResena) {
        this();
        this.puntuacion = puntuacion;
        this.textoResena = textoResena;
    }

    public Resena(int puntuacion, String textoResena, Usuario usuario, Pelicula pelicula) {
        this(puntuacion, textoResena);
        this.usuario = usuario;
        this.pelicula = pelicula;
        if (usuario != null) {
            this.idUsuario = usuario.getId();
            this.nombreUsuario = usuario.getNombreCompleto();
            this.avatarUsuario = usuario.getAvatarUrl();
        }
        if (pelicula != null) {
            this.idPelicula = pelicula.getId();
            this.tituloPelicula = pelicula.getTitulo();
            this.imagenPelicula = pelicula.getImagenUrl();
        }
    }

    // getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getTextoResena() {
        return textoResena;
    }

    public void setTextoResena(String textoResena) {
        this.textoResena = textoResena;
    }

    public String getFechaResena() {
        return fechaResena;
    }

    public void setFechaResena(String fechaResena) {
        this.fechaResena = fechaResena;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public boolean isEsLikeado() {
        return esLikeado;
    }

    public void setEsLikeado(boolean esLikeado) {
        this.esLikeado = esLikeado;
    }

    public boolean isEsDislikeado() {
        return esDislikeado;
    }

    public void setEsDislikeado(boolean esDislikeado) {
        this.esDislikeado = esDislikeado;
    }

    public boolean isEsSpoiler() {
        return esSpoiler;
    }

    public void setEsSpoiler(boolean esSpoiler) {
        this.esSpoiler = esSpoiler;
    }

    public boolean isEsVerificada() {
        return esVerificada;
    }

    public void setEsVerificada(boolean esVerificada) {
        this.esVerificada = esVerificada;
    }

    public boolean isEsEditada() {
        return esEditada;
    }

    public void setEsEditada(boolean esEditada) {
        this.esEditada = esEditada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            this.idUsuario = usuario.getId();
            this.nombreUsuario = usuario.getNombreCompleto();
            this.avatarUsuario = usuario.getAvatarUrl();
        }
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        if (pelicula != null) {
            this.idPelicula = pelicula.getId();
            this.tituloPelicula = pelicula.getTitulo();
            this.imagenPelicula = pelicula.getImagenUrl();
        }
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTituloPelicula() {
        return tituloPelicula;
    }

    public void setTituloPelicula(String tituloPelicula) {
        this.tituloPelicula = tituloPelicula;
    }

    public String getAvatarUsuario() {
        return avatarUsuario;
    }

    public void setAvatarUsuario(String avatarUsuario) {
        this.avatarUsuario = avatarUsuario;
    }

    public String getImagenPelicula() {
        return imagenPelicula;
    }

    public void setImagenPelicula(String imagenPelicula) {
        this.imagenPelicula = imagenPelicula;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(int idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getIpCreacion() {
        return ipCreacion;
    }

    public void setIpCreacion(String ipCreacion) {
        this.ipCreacion = ipCreacion;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public int getTotalComentarios() {
        return totalComentarios;
    }

    public void setTotalComentarios(int totalComentarios) {
        this.totalComentarios = totalComentarios;
    }

    public int getTotalCompartidas() {
        return totalCompartidas;
    }

    public void setTotalCompartidas(int totalCompartidas) {
        this.totalCompartidas = totalCompartidas;
    }

    public int getTotalReportes() {
        return totalReportes;
    }

    public void setTotalReportes(int totalReportes) {
        this.totalReportes = totalReportes;
    }

    public double getPuntuacionUtilidad() {
        return puntuacionUtilidad;
    }

    public void setPuntuacionUtilidad(double puntuacionUtilidad) {
        this.puntuacionUtilidad = puntuacionUtilidad;
    }

    public boolean isEsMia() {
        return esMia;
    }

    public void setEsMia(boolean esMia) {
        this.esMia = esMia;
    }

    public boolean isPuedeEditar() {
        return puedeEditar;
    }

    public void setPuedeEditar(boolean puedeEditar) {
        this.puedeEditar = puedeEditar;
    }

    public boolean isPuedeEliminar() {
        return puedeEliminar;
    }

    public void setPuedeEliminar(boolean puedeEliminar) {
        this.puedeEliminar = puedeEliminar;
    }

    public String getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public void setTiempoTranscurrido(String tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }

    // metodos de utilidad

    // obtener puntuacion formateada
    public String getPuntuacionFormateada() {
        return String.format("%.1f", (float) puntuacion);
    }

    // obtener fecha formateada para mostrar
    public String getFechaFormateada() {
        if (fechaResena == null || fechaResena.isEmpty()) {
            return "Fecha desconocida";
        }

        try {
            Date fecha = formatoEntrada.parse(fechaResena);
            if (fecha != null) {
                return formatoSalida.format(fecha);
            }
        } catch (ParseException e) {
            // si hay error, devolver fecha original
        }

        return fechaResena;
    }

    // obtener tiempo transcurrido desde la creacion
    public String getTiempoTranscurridoCalculado() {
        if (fechaResena == null || fechaResena.isEmpty()) {
            return "Fecha desconocida";
        }

        try {
            Date fechaCreacion = formatoEntrada.parse(fechaResena);
            if (fechaCreacion != null) {
                Date ahora = new Date();
                long diferencia = ahora.getTime() - fechaCreacion.getTime();

                // convertir a unidades de tiempo
                long segundos = diferencia / 1000;
                long minutos = segundos / 60;
                long horas = minutos / 60;
                long dias = horas / 24;
                long semanas = dias / 7;
                long meses = dias / 30;
                long anos = dias / 365;

                if (anos > 0) {
                    return anos == 1 ? "Hace 1 año" : "Hace " + anos + " años";
                } else if (meses > 0) {
                    return meses == 1 ? "Hace 1 mes" : "Hace " + meses + " meses";
                } else if (semanas > 0) {
                    return semanas == 1 ? "Hace 1 semana" : "Hace " + semanas + " semanas";
                } else if (dias > 0) {
                    return dias == 1 ? "Hace 1 día" : "Hace " + dias + " días";
                } else if (horas > 0) {
                    return horas == 1 ? "Hace 1 hora" : "Hace " + horas + " horas";
                } else if (minutos > 0) {
                    return minutos == 1 ? "Hace 1 minuto" : "Hace " + minutos + " minutos";
                } else {
                    return "Hace un momento";
                }
            }
        } catch (ParseException e) {
            // si hay error, usar formato simple
        }

        return getFechaFormateada();
    }

    // obtener categoria de puntuacion
    public String getCategoriaPuntuacion() {
        if (puntuacion >= 5) {
            return "Excelente";
        } else if (puntuacion >= 4) {
            return "Muy buena";
        } else if (puntuacion >= 3) {
            return "Buena";
        } else if (puntuacion >= 2) {
            return "Regular";
        } else {
            return "Mala";
        }
    }

    // obtener color para la puntuacion
    public String getColorPuntuacion() {
        if (puntuacion >= 5) {
            return "#4CAF50"; // verde excelente
        } else if (puntuacion >= 4) {
            return "#8BC34A"; // verde claro muy buena
        } else if (puntuacion >= 3) {
            return "#FFC107"; // amarillo buena
        } else if (puntuacion >= 2) {
            return "#FF9800"; // naranja regular
        } else {
            return "#F44336"; // rojo mala
        }
    }

    // verificar si es resena larga
    public boolean esResenaLarga() {
        return textoResena != null && textoResena.length() > 150;
    }

    // verificar si es resena corta
    public boolean esResenaCorta() {
        return textoResena != null && textoResena.length() < 50;
    }

    // obtener texto de likes
    public String getTextoLikes() {
        if (likes == 0) {
            return "Sin likes";
        } else if (likes == 1) {
            return "1 like";
        } else {
            return likes + " likes";
        }
    }

    // obtener ratio de likes/dislikes
    public double getRatioLikes() {
        int total = likes + dislikes;
        if (total == 0) {
            return 0.0;
        }
        return (double) likes / total;
    }

    // verificar si es resena popular (muchos likes)
    public boolean esResenaPopular() {
        return likes >= 10 || getRatioLikes() >= 0.8;
    }

    // verificar si es resena controversial (muchos likes y dislikes)
    public boolean esResenaControversial() {
        return likes >= 5 && dislikes >= 5;
    }

    // obtener vista previa del texto (primeras palabras)
    public String getVistaPrevia(int maxCaracteres) {
        if (textoResena == null || textoResena.isEmpty()) {
            return "";
        }

        if (textoResena.length() <= maxCaracteres) {
            return textoResena;
        }

        return textoResena.substring(0, maxCaracteres - 3) + "...";
    }

    // verificar si fue editada recientemente
    public boolean fueEditadaRecientemente() {
        if (!esEditada || fechaActualizacion == null) {
            return false;
        }

        try {
            Date fechaEdit = formatoEntrada.parse(fechaActualizacion);
            if (fechaEdit != null) {
                Date ahora = new Date();
                long diferencia = ahora.getTime() - fechaEdit.getTime();
                long horas = diferencia / (1000 * 60 * 60);
                return horas <= 24; // editada en las ultimas 24 horas
            }
        } catch (ParseException e) {
            // si hay error, asumir que no
        }

        return false;
    }

    // obtener texto de estado
    public String getTextoEstado() {
        StringBuilder estado = new StringBuilder();

        if (esVerificada) {
            estado.append("Verificada");
        }

        if (esEditada) {
            if (estado.length() > 0) estado.append(" • ");
            estado.append("Editada");
        }

        if (esSpoiler) {
            if (estado.length() > 0) estado.append(" • ");
            estado.append("Contiene spoilers");
        }

        return estado.toString();
    }

    // verificar si la resena es de calidad
    public boolean esResenaDeCalidad() {
        // criterios: no muy corta, buena ratio de likes, no muchos reportes
        return textoResena != null &&
                textoResena.length() >= 30 &&
                getRatioLikes() >= 0.6 &&
                totalReportes <= 2;
    }

    @Override
    public String toString() {
        return "Resena{" +
                "id=" + id +
                ", puntuacion=" + puntuacion +
                ", usuario=" + (usuario != null ? usuario.getNombreUsuario() : "null") +
                ", pelicula=" + (pelicula != null ? pelicula.getTitulo() : "null") +
                ", fechaResena='" + fechaResena + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Resena resena = (Resena) obj;
        return id == resena.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}