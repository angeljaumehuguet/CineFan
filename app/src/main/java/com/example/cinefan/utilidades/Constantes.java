package com.example.cinefan.utilidades;

/**
 * Constantes de la aplicación CineFan
 * VERSIÓN CORREGIDA - Incluye todos los endpoints y configuraciones necesarias
 */
public class Constantes {
    
    // ======= CONFIGURACIÓN DE LA API =======
    
    // URL base de la API (ACTUALIZAR según tu configuración)
    public static final String URL_BASE_API = "http://192.168.0.36/cinefan/api/";
    public static final String BASE_URL = "http://192.168.0.36/cinefan/api/";
    
    // ======= ENDPOINTS DE AUTENTICACIÓN =======
    public static final String ENDPOINT_LOGIN = "usuarios/login.php";
    public static final String ENDPOINT_REGISTRO = "usuarios/registro.php";
    
    // ======= ENDPOINTS DE USUARIOS =======
    public static final String ENDPOINT_PERFIL = "usuarios/perfil.php";
    public static final String ENDPOINT_SEGUIMIENTOS = "usuarios/seguimientos.php";
    
    // ======= ENDPOINTS DE PELÍCULAS =======
    public static final String ENDPOINT_PELICULAS_LISTAR = "peliculas/listar.php";
    public static final String ENDPOINT_PELICULAS_DETALLE = "peliculas/detalle.php";
    public static final String ENDPOINT_PELICULAS_CREAR = "peliculas/crear.php";
    public static final String ENDPOINT_PELICULAS_ACTUALIZAR = "peliculas/editar.php";
    public static final String ENDPOINT_PELICULAS_ELIMINAR = "peliculas/eliminar.php";
    
    // ======= ENDPOINTS DE RESEÑAS =======
    public static final String ENDPOINT_RESENAS_FEED = "resenas/feed.php";
    public static final String ENDPOINT_RESENAS_LISTAR = "resenas/listar.php";
    public static final String ENDPOINT_RESENAS_CREAR = "resenas/crear.php";
    public static final String ENDPOINT_RESENAS_ACTUALIZAR = "resenas/editar.php";
    public static final String ENDPOINT_RESENAS_ELIMINAR = "resenas/eliminar.php";
    public static final String ENDPOINT_RESENAS_LIKE = "resenas/like.php";
    
    // ======= ENDPOINTS DE BÚSQUEDA =======
    public static final String ENDPOINT_BUSCAR_GLOBAL = "buscar/global.php";
    
    // ======= ENDPOINTS DE GÉNEROS =======
    public static final String ENDPOINT_GENEROS_LISTAR = "generos/listar.php";
    
    // ======= EXTRAS PARA INTENTS =======
    public static final String EXTRA_PELICULA = "extra_pelicula";
    public static final String EXTRA_RESENA = "extra_resena";
    public static final String EXTRA_USUARIO = "extra_usuario";
    public static final String EXTRA_MODO_EDICION = "extra_modo_edicion";
    
    // ======= CÓDIGOS DE RESPUESTA =======
    public static final int CODIGO_EXITO = 200;
    public static final int CODIGO_CREADO = 201;
    public static final int CODIGO_NO_AUTORIZADO = 401;
    public static final int CODIGO_PROHIBIDO = 403;
    public static final int CODIGO_NO_ENCONTRADO = 404;
    public static final int CODIGO_ERROR_VALIDACION = 422;
    public static final int CODIGO_ERROR_SERVIDOR = 500;
    
    // ======= CÓDIGOS DE REQUEST =======
    public static final int REQUEST_LOGIN = 1001;
    public static final int REQUEST_REGISTRO = 1002;
    public static final int REQUEST_AGREGAR_PELICULA = 1003;
    public static final int REQUEST_EDITAR_PELICULA = 1004;
    public static final int REQUEST_AGREGAR_RESENA = 1005;
    public static final int REQUEST_EDITAR_RESENA = 1006;
    public static final int REQUEST_PERFIL = 1007;
    
    // ======= VALIDACIONES =======
    
    // Usuarios
    public static final int MIN_LONGITUD_USUARIO = 3;
    public static final int MAX_LONGITUD_USUARIO = 50;
    public static final int MIN_LONGITUD_PASSWORD = 6;
    public static final int MAX_LONGITUD_NOMBRE_COMPLETO = 100;
    
    // Películas
    public static final int MIN_ANO_PELICULA = 1895;
    public static final int MAX_ANO_PELICULA = 2030;
    public static final int MIN_DURACION_PELICULA = 1;
    public static final int MAX_DURACION_PELICULA = 600;
    public static final int MAX_LONGITUD_TITULO = 255;
    public static final int MAX_LONGITUD_DIRECTOR = 100;
    public static final int MAX_LONGITUD_SINOPSIS = 1000;
    
    // Reseñas
    public static final int MIN_LONGITUD_RESENA = 10;
    public static final int MAX_LONGITUD_RESENA = 1000;
    public static final int MIN_PUNTUACION = 1;
    public static final int MAX_PUNTUACION = 5;
    
    // ======= ETIQUETAS PARA LOGS =======
    public static final String TAG_API = "CineFan_API";
    public static final String TAG_AUTH = "CineFan_Auth";
    public static final String TAG_UI = "CineFan_UI";
    public static final String TAG_DATABASE = "CineFan_DB";
    public static final String TAG_RECEPTOR = "CineFan_Receptor";
    public static final String TAG_MUSICA = "CineFan_Musica";
    public static final String TAG_MAPA = "CineFan_Mapa";
    
    // ======= MENSAJES DE ERROR =======
    public static final String ERROR_CONEXION = "Error de conexión. Verifica tu internet.";
    public static final String ERROR_SERVIDOR = "Error del servidor. Inténtalo más tarde.";
    public static final String ERROR_NO_AUTORIZADO = "No autorizado. Inicia sesión nuevamente.";
    public static final String ERROR_DATOS_INVALIDOS = "Los datos introducidos no son válidos.";
    public static final String ERROR_RECURSO_NO_ENCONTRADO = "El recurso solicitado no fue encontrado.";
    public static final String ERROR_TIMEOUT = "Tiempo de espera agotado. Inténtalo nuevamente.";
    
    // ======= CONFIGURACIÓN DE GÉNEROS =======
    public static final String[] GENEROS = {
        "Acción", "Aventura", "Animación", "Biografía", "Comedia", 
        "Crimen", "Documental", "Drama", "Familia", "Fantasía", 
        "Historia", "Horror", "Musical", "Misterio", "Romance", 
        "Ciencia Ficción", "Deporte", "Thriller", "Guerra", "Western"
    };
    
    public static final String[] GENEROS_PELICULAS = {
        "Acción", "Aventura", "Animación", "Biografía", "Comedia", 
        "Crimen", "Documental", "Drama", "Familia", "Fantasía", 
        "Historia", "Horror", "Musical", "Misterio", "Romance", 
        "Ciencia Ficción", "Deporte", "Thriller", "Guerra", "Western"
    };
    
    // ======= CONFIGURACIÓN DE PUNTUACIONES =======
    public static final String[] DESCRIPCIONES_PUNTUACION = {
        "Muy mala", "Mala", "Regular", "Buena", "Excelente"
    };
    
    // ======= CONFIGURACIÓN DE FILTROS =======
    public static final String FILTRO_TODAS = "todas";
    public static final String FILTRO_MIS_PELICULAS = "mis_peliculas";
    public static final String FILTRO_MIS_RESENAS = "mis_resenas";
    public static final String FILTRO_FAVORITOS = "favoritos";
    
    // ======= CONFIGURACIÓN DE ORDENACIÓN =======
    public static final String ORDEN_FECHA_DESC = "fecha_desc";
    public static final String ORDEN_FECHA_ASC = "fecha_asc";
    public static final String ORDEN_TITULO_ASC = "titulo_asc";
    public static final String ORDEN_PUNTUACION_DESC = "puntuacion_desc";
    
    // ======= CONFIGURACIÓN DE BÚSQUEDA =======
    public static final String TIPO_BUSQUEDA_PELICULAS = "peliculas";
    public static final String TIPO_BUSQUEDA_USUARIOS = "usuarios";
    public static final String TIPO_BUSQUEDA_RESENAS = "resenas";
    public static final String TIPO_BUSQUEDA_GLOBAL = "global";
    
    // ======= UBICACIÓN DE LA SEDE =======
    // Coordenadas de Valls, Tarragona
    public static final double LATITUD_SEDE = 41.2861;
    public static final double LONGITUD_SEDE = 1.2486;
    public static final String NOMBRE_SEDE = "Sede CineFan Valls";
    public static final String DIRECCION_SEDE = "Carrer Major, 25, 43800 Valls, Tarragona";
    
    // ======= CONFIGURACIÓN DE NOTIFICACIONES =======
    public static final String CANAL_ID_NOTIFICACIONES = "cinefan_notificaciones";
    public static final String CANAL_NOMBRE = "Notificaciones CineFan";
    public static final String CANAL_DESCRIPCION = "Notificaciones de la aplicación CineFan";
    public static final int NOTIFICACION_ID_LLAMADA = 1001;
    public static final int NOTIFICACION_ID_SMS = 1002;
    public static final int NOTIFICACION_ID_MUSICA = 1003;
    
    // ======= PREFERENCIAS =======
    public static final String PREF_NOMBRE = "CineFanPrefs";
    public static final String PREFS_NOMBRE = "CineFanPrefs";
    public static final String PREF_TOKEN = "token";
    public static final String PREF_ID_USUARIO = "id_usuario";
    public static final String PREF_NOMBRE_USUARIO = "nombre_usuario";
    public static final String PREF_NOMBRE_COMPLETO = "nombre_completo";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_SESION_ACTIVA = "sesion_activa";
    public static final String PREF_PRIMER_USO = "primer_uso";
    public static final String PREF_MODO_OSCURO = "modo_oscuro";
    public static final String PREF_NOTIFICACIONES = "notificaciones";
    public static final String PREF_MUSICA_ACTIVA = "musica_activa";
    
    // ======= CONFIGURACIÓN DE TIMEOUTS =======
    public static final int TIMEOUT_CONEXION = 10000; // 10 segundos
    public static final int TIMEOUT_LECTURA = 15000;  // 15 segundos
    public static final int TIMEOUT_ESCRITURA = 10000; // 10 segundos
    
    // ======= URLS DE AYUDA Y SOPORTE =======
    public static final String URL_AYUDA = "https://cinefan.example.com/ayuda";
    public static final String URL_POLITICA_PRIVACIDAD = "https://cinefan.example.com/privacidad";
    public static final String URL_TERMINOS_SERVICIO = "https://cinefan.example.com/terminos";
    public static final String EMAIL_SOPORTE = "soporte@cinefan.example.com";
    
    // ======= CONFIGURACIÓN DE DESARROLLO =======
    public static final boolean DEBUG_MODE = true; // Cambiar a false en producción
    public static final boolean MOCK_DATA = false; // Para usar datos de prueba
    
    // ======= MÉTODOS DE UTILIDAD =======
    
    /**
     * Construir URL completa para un endpoint
     */
    public static String construirUrl(String endpoint, String... parametros) {
        StringBuilder url = new StringBuilder(URL_BASE_API + endpoint);
        
        if (parametros.length > 0) {
            url.append("?");
            for (int i = 0; i < parametros.length; i += 2) {
                if (i > 0) url.append("&");
                if (i + 1 < parametros.length) {
                    url.append(parametros[i]).append("=").append(parametros[i + 1]);
                }
            }
        }
        
        return url.toString();
    }
    
    /**
     * Verificar si una URL es válida
     */
    public static boolean esUrlValida(String url) {
        return url != null && 
               !url.trim().isEmpty() && 
               (url.startsWith("http://") || url.startsWith("https://"));
    }
    
    /**
     * Obtener descripción de puntuación
     */
    public static String obtenerDescripcionPuntuacion(int puntuacion) {
        if (puntuacion >= 1 && puntuacion <= 5) {
            return DESCRIPCIONES_PUNTUACION[puntuacion - 1];
        }
        return "Sin puntuación";
    }
    
    /**
     * Verificar si un año es válido para película
     */
    public static boolean esAnoValido(int ano) {
        return ano >= MIN_ANO_PELICULA && ano <= MAX_ANO_PELICULA;
    }
    
    /**
     * Verificar si una duración es válida
     */
    public static boolean esDuracionValida(int duracion) {
        return duracion >= MIN_DURACION_PELICULA && duracion <= MAX_DURACION_PELICULA;
    }
}