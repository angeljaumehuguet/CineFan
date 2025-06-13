package com.example.cinefan.modelos;

import java.io.Serializable;
import java.util.List;

/**
 * modelo completo para representar un usuario
 */
public class Usuario implements Serializable {

    private int id;
    private String nombreUsuario;
    private String email;
    private String nombreCompleto;
    private String fechaRegistro;
    private String fechaUltimaActividad;
    private boolean activo;
    private String avatarUrl;
    private String biografia;
    private String paisOrigen;

    // estadisticas del usuario
    private int totalPeliculas;
    private int totalResenas;
    private int totalSeguidores;
    private int totalSiguiendo;
    private double puntuacionPromedio;

    // relaciones
    private List<Pelicula> peliculasFavoritas;
    private List<Resena> resenasRecientes;
    private List<Usuario> seguidores;
    private List<Usuario> siguiendo;

    // campos de configuracion
    private boolean notificacionesActivas;
    private boolean perfilPublico;
    private String idiomaPreferido;
    private String zonaHoraria;

    // constructores
    public Usuario() {
        this.activo = true;
        this.notificacionesActivas = true;
        this.perfilPublico = true;
        this.idiomaPreferido = "es";
        this.totalPeliculas = 0;
        this.totalResenas = 0;
        this.totalSeguidores = 0;
        this.totalSiguiendo = 0;
        this.puntuacionPromedio = 0.0;
    }

    public Usuario(String nombreUsuario, String email, String nombreCompleto) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
    }

    // getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaUltimaActividad() {
        return fechaUltimaActividad;
    }

    public void setFechaUltimaActividad(String fechaUltimaActividad) {
        this.fechaUltimaActividad = fechaUltimaActividad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public int getTotalPeliculas() {
        return totalPeliculas;
    }

    public void setTotalPeliculas(int totalPeliculas) {
        this.totalPeliculas = totalPeliculas;
    }

    public int getTotalResenas() {
        return totalResenas;
    }

    public void setTotalResenas(int totalResenas) {
        this.totalResenas = totalResenas;
    }

    public int getTotalSeguidores() {
        return totalSeguidores;
    }

    public void setTotalSeguidores(int totalSeguidores) {
        this.totalSeguidores = totalSeguidores;
    }

    public int getTotalSiguiendo() {
        return totalSiguiendo;
    }

    public void setTotalSiguiendo(int totalSiguiendo) {
        this.totalSiguiendo = totalSiguiendo;
    }

    public double getPuntuacionPromedio() {
        return puntuacionPromedio;
    }

    public void setPuntuacionPromedio(double puntuacionPromedio) {
        this.puntuacionPromedio = puntuacionPromedio;
    }

    public List<Pelicula> getPeliculasFavoritas() {
        return peliculasFavoritas;
    }

    public void setPeliculasFavoritas(List<Pelicula> peliculasFavoritas) {
        this.peliculasFavoritas = peliculasFavoritas;
    }

    public List<Resena> getResenasRecientes() {
        return resenasRecientes;
    }

    public void setResenasRecientes(List<Resena> resenasRecientes) {
        this.resenasRecientes = resenasRecientes;
    }

    public List<Usuario> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<Usuario> seguidores) {
        this.seguidores = seguidores;
    }

    public List<Usuario> getSiguiendo() {
        return siguiendo;
    }

    public void setSiguiendo(List<Usuario> siguiendo) {
        this.siguiendo = siguiendo;
    }

    public boolean isNotificacionesActivas() {
        return notificacionesActivas;
    }

    public void setNotificacionesActivas(boolean notificacionesActivas) {
        this.notificacionesActivas = notificacionesActivas;
    }

    public boolean isPerfilPublico() {
        return perfilPublico;
    }

    public void setPerfilPublico(boolean perfilPublico) {
        this.perfilPublico = perfilPublico;
    }

    public String getIdiomaPreferido() {
        return idiomaPreferido;
    }

    public void setIdiomaPreferido(String idiomaPreferido) {
        this.idiomaPreferido = idiomaPreferido;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    // metodos de utilidad

    // obtener nombre para mostrar (prioriza nombre completo sobre usuario)
    public String getNombreParaMostrar() {
        if (nombreCompleto != null && !nombreCompleto.trim().isEmpty()) {
            return nombreCompleto;
        }
        return nombreUsuario;
    }

    // obtener iniciales para avatar por defecto
    public String getIniciales() {
        if (nombreCompleto != null && !nombreCompleto.trim().isEmpty()) {
            String[] partes = nombreCompleto.trim().split("\\s+");
            if (partes.length >= 2) {
                return (partes[0].charAt(0) + "" + partes[1].charAt(0)).toUpperCase();
            } else if (partes.length == 1) {
                return partes[0].substring(0, Math.min(2, partes[0].length())).toUpperCase();
            }
        }

        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            return nombreUsuario.substring(0, Math.min(2, nombreUsuario.length())).toUpperCase();
        }

        return "??";
    }

    // verificar si tiene avatar personalizado
    public boolean tieneAvatarPersonalizado() {
        return avatarUrl != null && !avatarUrl.trim().isEmpty();
    }

    // obtener nivel basado en actividad
    public String getNivelUsuario() {
        int actividadTotal = totalPeliculas + totalResenas;

        if (actividadTotal >= 100) {
            return "Cinéfilo Experto";
        } else if (actividadTotal >= 50) {
            return "Cinéfilo Avanzado";
        } else if (actividadTotal >= 20) {
            return "Cinéfilo Intermedio";
        } else if (actividadTotal >= 5) {
            return "Cinéfilo Principiante";
        } else {
            return "Nuevo Usuario";
        }
    }

    // obtener color para el nivel
    public String getColorNivel() {
        String nivel = getNivelUsuario();

        switch (nivel) {
            case "Cinéfilo Experto":
                return "#FFD700"; // dorado
            case "Cinéfilo Avanzado":
                return "#C0C0C0"; // plateado
            case "Cinéfilo Intermedio":
                return "#CD7F32"; // bronce
            case "Cinéfilo Principiante":
                return "#4CAF50"; // verde
            default:
                return "#757575"; // gris
        }
    }

    // verificar si es usuario activo
    public boolean esUsuarioActivo() {
        return activo && totalResenas > 0;
    }

    // verificar si es usuario veterano
    public boolean esUsuarioVeterano() {
        return totalPeliculas >= 50 || totalResenas >= 30;
    }

    // obtener puntuacion formateada
    public String getPuntuacionFormateada() {
        if (puntuacionPromedio > 0) {
            return String.format("%.1f", puntuacionPromedio);
        }
        return "N/A";
    }

    // verificar si sigue a otro usuario
    public boolean sigue(Usuario otroUsuario) {
        if (siguiendo != null && otroUsuario != null) {
            for (Usuario usuario : siguiendo) {
                if (usuario.getId() == otroUsuario.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    // verificar si es seguido por otro usuario
    public boolean esSeguiPor(Usuario otroUsuario) {
        if (seguidores != null && otroUsuario != null) {
            for (Usuario usuario : seguidores) {
                if (usuario.getId() == otroUsuario.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    // obtener resumen de estadisticas
    public String getResumenEstadisticas() {
        return String.format("%d películas • %d reseñas • %d seguidores",
                totalPeliculas, totalResenas, totalSeguidores);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Usuario usuario = (Usuario) obj;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}