package com.example.cinefan.utilidades;

import android.util.Patterns;
import java.util.regex.Pattern;
import java.util.Calendar;

import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.modelos.Resena;

public class Validador {

    // Patrones de validación
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]{2,100}$");
    private static final Pattern PATTERN_DIRECTOR = Pattern.compile("^[a-zA-ZÀ-ÿ\\s.,-]{2,100}$");
    private static final Pattern PATTERN_TITULO = Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s:.,!?()-]{1,255}$");

    // Patrones de seguridad para detectar inyecciones
    private static final Pattern PATTERN_SQL_INJECTION = Pattern.compile(
            "(?i)(\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b|\\b(AND|OR)\\b.*\\b(=|LIKE)\\b|'.*--|\\*|;|/\\*|\\*/|@@|@|\\b(CHAR|NCHAR|VARCHAR|NVARCHAR)\\b|\\b(GRANT|REVOKE)\\b|\\b(SYSOBJECTS|SYSCOLUMNS)\\b|\\b(EXEC|EXECUTE)\\b|\\b(SP_)\\b|\\b(XP_)\\b)"
    );

    private static final Pattern PATTERN_XSS = Pattern.compile(
            "(?i)(<script[^>]*>.*?</script>|<iframe[^>]*>.*?</iframe>|javascript:|vbscript:|on\\w+\\s*=|<\\s*\\w+\\s+.*?on\\w+\\s*=)"
    );

    /**
     * Validar nombre de usuario
     * @param nombreUsuario El nombre de usuario a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return false;
        }

        nombreUsuario = nombreUsuario.trim();

        // Verificar longitud
        if (nombreUsuario.length() < Constantes.MIN_LONGITUD_USUARIO ||
                nombreUsuario.length() > Constantes.MAX_LONGITUD_USUARIO) {
            return false;
        }

        // Verificar patrón (solo letras, números y guiones bajos)
        if (!PATTERN_USUARIO.matcher(nombreUsuario).matches()) {
            return false;
        }

        // Verificar que no contenga inyecciones SQL o XSS
        return !contieneTentativaInyeccion(nombreUsuario);
    }

    /**
     * Validar contraseña
     * @param password La contraseña a validar
     * @return true si es válida, false en caso contrario
     */
    public boolean validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        // Verificar longitud mínima
        if (password.length() < Constantes.MIN_LONGITUD_PASSWORD) {
            return false;
        }

        // No permitir solo espacios
        if (password.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Validar email
     * @param email El email a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        email = email.trim();

        // Usar el patrón de Android para validar emails
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }

        // Verificar que no contenga inyecciones
        return !contieneTentativaInyeccion(email);
    }

    /**
     * Validar nombre completo
     * @param nombreCompleto El nombre completo a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return false;
        }

        nombreCompleto = nombreCompleto.trim();

        // Verificar longitud
        if (nombreCompleto.length() > Constantes.MAX_LONGITUD_NOMBRE_COMPLETO) {
            return false;
        }

        // Verificar patrón (letras, espacios, acentos)
        if (!PATTERN_NOMBRE.matcher(nombreCompleto).matches()) {
            return false;
        }

        return !contieneTentativaInyeccion(nombreCompleto);
    }

    /**
     * Validar película completa
     * @param pelicula La película a validar
     * @return true si es válida, false en caso contrario
     */
    public boolean validarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            return false;
        }

        return validarTituloPelicula(pelicula.getTitulo()) &&
                validarDirector(pelicula.getDirector()) &&
                validarAno(pelicula.getAnoLanzamiento()) &&
                validarDuracion(pelicula.getDuracionMinutos());
    }

    /**
     * Validar título de película
     * @param titulo El título a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarTituloPelicula(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }

        titulo = titulo.trim();

        // Verificar longitud
        if (titulo.length() > Constantes.MAX_LONGITUD_TITULO) {
            return false;
        }

        // Verificar patrón
        if (!PATTERN_TITULO.matcher(titulo).matches()) {
            return false;
        }

        return !contieneTentativaInyeccion(titulo);
    }

    /**
     * Validar director
     * @param director El director a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            return false;
        }

        director = director.trim();

        // Verificar longitud
        if (director.length() > Constantes.MAX_LONGITUD_DIRECTOR) {
            return false;
        }

        // Verificar patrón
        if (!PATTERN_DIRECTOR.matcher(director).matches()) {
            return false;
        }

        return !contieneTentativaInyeccion(director);
    }

    /**
     * Validar año de lanzamiento
     * @param ano El año a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarAno(int ano) {
        return ano >= Constantes.MIN_ANO_PELICULA && ano <= Constantes.MAX_ANO_PELICULA;
    }

    /**
     * Validar duración en minutos
     * @param duracion La duración a validar
     * @return true si es válida, false en caso contrario
     */
    public boolean validarDuracion(int duracion) {
        return duracion >= Constantes.MIN_DURACION_PELICULA && duracion <= Constantes.MAX_DURACION_PELICULA;
    }

    /**
     * Validar reseña completa
     * @param resena La reseña a validar
     * @return true si es válida, false en caso contrario
     */
    public boolean validarResena(Resena resena) {
        if (resena == null) {
            return false;
        }

        return validarTextoResena(resena.getTextoResena()) &&
                validarPuntuacion(resena.getPuntuacion());
    }

    /**
     * Validar texto de reseña
     * @param textoResena El texto de la reseña a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validarTextoResena(String textoResena) {
        if (textoResena == null || textoResena.trim().isEmpty()) {
            return false;
        }

        textoResena = textoResena.trim();

        // Verificar longitud
        if (textoResena.length() < Constantes.MIN_LONGITUD_RESENA ||
                textoResena.length() > Constantes.MAX_LONGITUD_RESENA) {
            return false;
        }

        // Verificar que no contenga inyecciones
        return !contieneTentativaInyeccion(textoResena);
    }

    /**
     * Validar puntuación
     * @param puntuacion La puntuación a validar
     * @return true si es válida, false en caso contrario
     */
    public boolean validarPuntuacion(int puntuacion) {
        return puntuacion >= Constantes.MIN_PUNTUACION && puntuacion <= Constantes.MAX_PUNTUACION;
    }

    /**
     * Validar URL de imagen
     * @param url La URL a validar
     * @return true si es válida, false en caso contrario
     */
    public boolean validarUrlImagen(String url) {
        if (url == null || url.trim().isEmpty()) {
            return true; // URL es opcional
        }

        url = url.trim();

        // Verificar que sea una URL válida
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            return false;
        }

        // Verificar que termine en extensión de imagen
        String urlLower = url.toLowerCase();
        return urlLower.endsWith(".jpg") || urlLower.endsWith(".jpeg") ||
                urlLower.endsWith(".png") || urlLower.endsWith(".gif") ||
                urlLower.endsWith(".webp");
    }

    // =================== MÉTODOS DE SEGURIDAD ===================

    /**
     * Detectar tentativas de inyección SQL o XSS
     * @param texto El texto a verificar
     * @return true si contiene tentativas de inyección, false en caso contrario
     */
    private boolean contieneTentativaInyeccion(String texto) {
        if (texto == null) {
            return false;
        }

        // Verificar inyección SQL
        if (PATTERN_SQL_INJECTION.matcher(texto).find()) {
            return true;
        }

        // Verificar XSS
        if (PATTERN_XSS.matcher(texto).find()) {
            return true;
        }

        return false;
    }

    /**
     * Sanitizar texto eliminando caracteres peligrosos
     * @param texto El texto a sanitizar
     * @return El texto sanitizado
     */
    public String sanitizarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        // Eliminar tags HTML
        texto = texto.replaceAll("<[^>]*>", "");

        // Eliminar scripts
        texto = texto.replaceAll("(?i)javascript:", "");
        texto = texto.replaceAll("(?i)vbscript:", "");

        // Eliminar caracteres de control
        texto = texto.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");

        return texto.trim();
    }

    /**
     * Limpiar texto eliminando espacios innecesarios
     * @param texto El texto a limpiar
     * @return El texto limpio
     */
    public String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        // Eliminar espacios al inicio y final
        texto = texto.trim();

        // Reemplazar múltiples espacios por uno solo
        texto = texto.replaceAll("\\s+", " ");

        return texto;
    }

    // =================== MÉTODOS DE UTILIDAD ===================

    /**
     * Obtener el año actual
     * @return El año actual
     */
    public int obtenerAnoActual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Verificar si un año es futuro
     * @param ano El año a verificar
     * @return true si es futuro, false en caso contrario
     */
    public boolean esAnoFuturo(int ano) {
        return ano > obtenerAnoActual();
    }

    /**
     * Validar que una cadena no esté vacía después de limpiarla
     * @param texto El texto a validar
     * @return true si no está vacío, false en caso contrario
     */
    public boolean noEstaVacio(String texto) {
        return texto != null && !limpiarTexto(texto).isEmpty();
    }
}