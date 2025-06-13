package com.example.cinefan.utilidades;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * clase para validar formularios de la aplicacion
 */
public class ValidadorFormularios {

    // validar nombre de usuario
    public String validarNombreUsuario(String usuario) {
        if (TextUtils.isEmpty(usuario)) {
            return "El nombre de usuario es requerido";
        }

        if (usuario.length() < Constantes.MIN_LONGITUD_USUARIO) {
            return "El usuario debe tener al menos " + Constantes.MIN_LONGITUD_USUARIO + " caracteres";
        }

        if (usuario.length() > Constantes.MAX_LONGITUD_USUARIO) {
            return "El usuario no puede tener más de " + Constantes.MAX_LONGITUD_USUARIO + " caracteres";
        }

        // verificar que solo contenga letras, numeros y guiones bajos
        if (!usuario.matches("^[a-zA-Z0-9_]+$")) {
            return "El usuario solo puede contener letras, números y guiones bajos";
        }

        // verificar que no empiece con numero
        if (Character.isDigit(usuario.charAt(0))) {
            return "El usuario no puede empezar con un número";
        }

        return null; // valido
    }

    // validar password
    public String validarPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "La contraseña es requerida";
        }

        if (password.length() < Constantes.MIN_LONGITUD_PASSWORD) {
            return "La contraseña debe tener al menos " + Constantes.MIN_LONGITUD_PASSWORD + " caracteres";
        }

        // verificar que contenga al menos una letra
        if (!password.matches(".*[a-zA-Z].*")) {
            return "La contraseña debe contener al menos una letra";
        }

        // verificar que contenga al menos un numero
        if (!password.matches(".*[0-9].*")) {
            return "La contraseña debe contener al menos un número";
        }

        return null; // valido
    }

    // validar email
    public String validarEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "El email es requerido";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "El formato del email no es válido";
        }

        return null; // valido
    }

    // validar titulo de pelicula
    public String validarTituloPelicula(String titulo) {
        if (TextUtils.isEmpty(titulo)) {
            return "El título de la película es requerido";
        }

        if (titulo.trim().length() < 1) {
            return "El título no puede estar vacío";
        }

        if (titulo.length() > 100) {
            return "El título no puede tener más de 100 caracteres";
        }

        return null; // valido
    }

    // validar director
    public String validarDirector(String director) {
        if (TextUtils.isEmpty(director)) {
            return "El director es requerido";
        }

        if (director.trim().length() < 2) {
            return "El nombre del director debe tener al menos 2 caracteres";
        }

        if (director.length() > 50) {
            return "El nombre del director no puede tener más de 50 caracteres";
        }

        return null; // valido
    }

    // validar ano de lanzamiento
    public String validarAnoLanzamiento(int ano) {
        if (ano < Constantes.MIN_ANO_PELICULA) {
            return "El año debe ser mayor a " + Constantes.MIN_ANO_PELICULA;
        }

        if (ano > Constantes.MAX_ANO_PELICULA) {
            return "El año no puede ser mayor a " + Constantes.MAX_ANO_PELICULA;
        }

        return null; // valido
    }

    // validar duracion de pelicula
    public String validarDuracion(int duracion) {
        if (duracion < Constantes.MIN_DURACION_PELICULA) {
            return "La duración debe ser al menos " + Constantes.MIN_DURACION_PELICULA + " minuto";
        }

        if (duracion > Constantes.MAX_DURACION_PELICULA) {
            return "La duración no puede ser mayor a " + Constantes.MAX_DURACION_PELICULA + " minutos";
        }

        return null; // valido
    }

    // validar genero de pelicula
    public String validarGenero(String genero) {
        if (TextUtils.isEmpty(genero)) {
            return "El género es requerido";
        }

        // verificar que el genero este en la lista de generos validos
        boolean generoValido = false;
        for (String g : Constantes.GENEROS) {
            if (g.equals(genero)) {
                generoValido = true;
                break;
            }
        }

        if (!generoValido) {
            return "Selecciona un género válido";
        }

        return null; // valido
    }

    // validar texto de resena
    public String validarTextoResena(String texto) {
        if (TextUtils.isEmpty(texto)) {
            return "El texto de la reseña es requerido";
        }

        if (texto.trim().length() < 10) {
            return "La reseña debe tener al menos 10 caracteres";
        }

        if (texto.length() > Constantes.MAX_LONGITUD_RESENA) {
            return "La reseña no puede tener más de " + Constantes.MAX_LONGITUD_RESENA + " caracteres";
        }

        return null; // valido
    }

    // validar puntuacion de resena
    public String validarPuntuacion(float puntuacion) {
        if (puntuacion < 0.5f) {
            return "La puntuación mínima es 0.5 estrellas";
        }

        if (puntuacion > 5.0f) {
            return "La puntuación máxima es 5 estrellas";
        }

        return null; // valido
    }

    // validar url de imagen
    public String validarUrlImagen(String url) {
        // url es opcional, puede estar vacia
        if (TextUtils.isEmpty(url)) {
            return null; // valido
        }

        // verificar formato basico de url
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            return "La URL de la imagen no es válida";
        }

        // verificar que sea una imagen
        String urlLower = url.toLowerCase();
        if (!urlLower.endsWith(".jpg") && !urlLower.endsWith(".jpeg") &&
                !urlLower.endsWith(".png") && !urlLower.endsWith(".gif") &&
                !urlLower.endsWith(".webp")) {
            return "La URL debe apuntar a una imagen (jpg, png, gif, webp)";
        }

        return null; // valido
    }

    // validar que un campo no este vacio
    public String validarCampoRequerido(String valor, String nombreCampo) {
        if (TextUtils.isEmpty(valor) || valor.trim().isEmpty()) {
            return "El campo " + nombreCampo + " es requerido";
        }
        return null; // valido
    }

    // validar longitud minima
    public String validarLongitudMinima(String valor, int minimo, String nombreCampo) {
        if (valor != null && valor.length() < minimo) {
            return "El campo " + nombreCampo + " debe tener al menos " + minimo + " caracteres";
        }
        return null; // valido
    }

    // validar longitud maxima
    public String validarLongitudMaxima(String valor, int maximo, String nombreCampo) {
        if (valor != null && valor.length() > maximo) {
            return "El campo " + nombreCampo + " no puede tener más de " + maximo + " caracteres";
        }
        return null; // valido
    }
}