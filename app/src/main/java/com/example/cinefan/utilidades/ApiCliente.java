package com.example.cinefan.utilidades;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * cliente para manejar peticiones a la api de cinefan
 * centraliza la logica de comunicacion con el backend
 */
public class ApiCliente {

    private static ApiCliente instancia;
    private RequestQueue colaRequestQueue;
    private Context contexto;
    private PreferenciasUsuario preferencias;

    // timeouts en milisegundos
    private static final int TIMEOUT_PETICION = 15000; // 15 segundos
    private static final int MAX_REINTENTOS = 2;
    private static final float MULTIPLICADOR_BACKOFF = 1.0f;

    // constructor privado para singleton
    private ApiCliente(Context contexto) {
        this.contexto = contexto.getApplicationContext();
        this.preferencias = new PreferenciasUsuario(this.contexto);
        this.colaRequestQueue = getRequestQueue();
    }

    // obtener instancia singleton
    public static synchronized ApiCliente getInstance(Context contexto) {
        if (instancia == null) {
            instancia = new ApiCliente(contexto);
        }
        return instancia;
    }

    // obtener cola de peticiones
    public RequestQueue getRequestQueue() {
        if (colaRequestQueue == null) {
            colaRequestQueue = Volley.newRequestQueue(contexto);
        }
        return colaRequestQueue;
    }

    // interfaz para callbacks de respuesta
    public interface ApiCallback {
        void onExito(JSONObject response);
        void onError(VolleyError error);
    }

    // realizar peticion GET
    public void get(String endpoint, ApiCallback callback) {
        realizarPeticion(Request.Method.GET, endpoint, null, callback);
    }

    // realizar peticion GET con parametros
    public void get(String endpoint, Map<String, String> parametros, ApiCallback callback) {
        String url = construirUrlConParametros(endpoint, parametros);
        realizarPeticion(Request.Method.GET, url, null, callback);
    }

    // realizar peticion POST
    public void post(String endpoint, JSONObject datos, ApiCallback callback) {
        realizarPeticion(Request.Method.POST, endpoint, datos, callback);
    }

    // realizar peticion PUT
    public void put(String endpoint, JSONObject datos, ApiCallback callback) {
        realizarPeticion(Request.Method.PUT, endpoint, datos, callback);
    }

    // realizar peticion DELETE
    public void delete(String endpoint, JSONObject datos, ApiCallback callback) {
        realizarPeticion(Request.Method.DELETE, endpoint, datos, callback);
    }

    // metodo principal para realizar peticiones
    private void realizarPeticion(int metodo, String endpoint, JSONObject datos, ApiCallback callback) {
        String url = Constantes.URL_BASE_API + endpoint;

        Log.d(Constantes.TAG_API, "Realizando peticion: " + metodoToString(metodo) + " " + url);

        JsonObjectRequest peticion = new JsonObjectRequest(
                metodo,
                url,
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constantes.TAG_API, "Respuesta exitosa: " + response.toString());
                        if (callback != null) {
                            callback.onExito(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Constantes.TAG_API, "Error completo: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e(Constantes.TAG_API, "Código de respuesta: " + error.networkResponse.statusCode);
                            Log.e(Constantes.TAG_API, "Datos de respuesta: " + new String(error.networkResponse.data));
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return obtenerHeaders();
            }
        };

        // configurar politica de reintentos
        peticion.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_PETICION,
                MAX_REINTENTOS,
                MULTIPLICADOR_BACKOFF
        ));

        // agregar peticion a la cola
        colaRequestQueue.add(peticion);
    }

    // obtener headers comunes para todas las peticiones
    private Map<String, String> obtenerHeaders() {
        Map<String, String> headers = new HashMap<>();

        // content type
        headers.put("Content-Type", "application/json");

        // accept
        headers.put("Accept", "application/json");

        // user agent personalizado
        headers.put("User-Agent", "CineFan-Android/1.0");

        // token de autorizacion si esta disponible
        String token = preferencias.obtenerToken();
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }

        // id del usuario si esta disponible
        int idUsuario = preferencias.obtenerIdUsuario();
        if (idUsuario > 0) {
            headers.put("X-User-ID", String.valueOf(idUsuario));
        }

        return headers;
    }

    // construir url con parametros de consulta
    private String construirUrlConParametros(String endpoint, Map<String, String> parametros) {
        if (parametros == null || parametros.isEmpty()) {
            return endpoint;
        }

        StringBuilder url = new StringBuilder(endpoint);
        url.append("?");

        boolean primero = true;
        for (Map.Entry<String, String> entrada : parametros.entrySet()) {
            if (!primero) {
                url.append("&");
            }
            url.append(entrada.getKey()).append("=").append(entrada.getValue());
            primero = false;
        }

        return url.toString();
    }

    // manejar errores comunes de la api
    private void manejarError(VolleyError error) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;

            switch (statusCode) {
                case 401:
                    // token expirado o no autorizado
                    Log.w(Constantes.TAG_API, "Token expirado o no autorizado");
                    // podriamos automaticamente cerrar sesion aqui
                    break;

                case 403:
                    // prohibido
                    Log.w(Constantes.TAG_API, "Acceso prohibido");
                    break;

                case 404:
                    // no encontrado
                    Log.w(Constantes.TAG_API, "Recurso no encontrado");
                    break;

                case 422:
                    // errores de validacion
                    Log.w(Constantes.TAG_API, "Errores de validacion en los datos");
                    break;

                case 500:
                    // error interno del servidor
                    Log.e(Constantes.TAG_API, "Error interno del servidor");
                    break;

                case 503:
                    // servicio no disponible
                    Log.e(Constantes.TAG_API, "Servicio no disponible");
                    break;

                default:
                    Log.e(Constantes.TAG_API, "Error HTTP: " + statusCode);
                    break;
            }
        } else {
            // error de red
            Log.e(Constantes.TAG_API, "Error de conexion: " + error.getClass().getSimpleName());
        }
    }

    // metodos de conveniencia para endpoints especificos

    // autenticacion
    public void login(String usuario, String password, ApiCallback callback) {
        JSONObject datos = new JSONObject();
        try {
            datos.put("usuario", usuario);
            datos.put("password", password);
        } catch (Exception e) {
            Log.e(Constantes.TAG_API, "Error creando datos de login: " + e.getMessage());
        }

        post(Constantes.ENDPOINT_LOGIN, datos, callback);
    }

    public void registro(String nombreCompleto, String usuario, String email, String password, ApiCallback callback) {
        JSONObject datos = new JSONObject();
        try {
            datos.put("nombre_completo", nombreCompleto);
            datos.put("nombre_usuario", usuario);
            datos.put("email", email);
            datos.put("password", password);
        } catch (Exception e) {
            Log.e(Constantes.TAG_API, "Error creando datos de registro: " + e.getMessage());
        }

        post(Constantes.ENDPOINT_REGISTRO, datos, callback);
    }

    // peliculas
    public void listarPeliculas(ApiCallback callback) {
        get(Constantes.ENDPOINT_PELICULAS_LISTAR, callback);
    }

    public void listarPeliculasUsuario(int idUsuario, ApiCallback callback) {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("usuario", String.valueOf(idUsuario));
        get(Constantes.ENDPOINT_PELICULAS_LISTAR, parametros, callback);
    }

    public void crearPelicula(String titulo, String director, int ano, int duracion,
                              String genero, String imagenUrl, ApiCallback callback) {
        JSONObject datos = new JSONObject();
        try {
            datos.put("titulo", titulo);
            datos.put("director", director);
            datos.put("ano_lanzamiento", ano);
            datos.put("duracion_minutos", duracion);
            datos.put("genero", genero);
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                datos.put("imagen_url", imagenUrl);
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_API, "Error creando datos de pelicula: " + e.getMessage());
        }

        post(Constantes.ENDPOINT_PELICULAS_CREAR, datos, callback);
    }

    // resenas
    public void obtenerFeedResenas(int pagina, ApiCallback callback) {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("pagina", String.valueOf(pagina));
        parametros.put("limite", "10");
        get(Constantes.ENDPOINT_RESENAS_FEED, parametros, callback);
    }

    public void listarResenasUsuario(int idUsuario, ApiCallback callback) {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("usuario", String.valueOf(idUsuario));
        get(Constantes.ENDPOINT_RESENAS_LISTAR, parametros, callback);
    }

    public void crearResena(int idPelicula, int puntuacion, String textoResena, ApiCallback callback) {
        JSONObject datos = new JSONObject();
        try {
            datos.put("id_pelicula", idPelicula);
            datos.put("puntuacion", puntuacion);
            datos.put("texto_resena", textoResena);
        } catch (Exception e) {
            Log.e(Constantes.TAG_API, "Error creando datos de resena: " + e.getMessage());
        }

        post(Constantes.ENDPOINT_RESENAS_CREAR, datos, callback);
    }

    // metodos de utilidad

    // cancelar todas las peticiones
    public void cancelarTodas() {
        if (colaRequestQueue != null) {
            colaRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true; // cancelar todas
                }
            });
        }
    }

    // cancelar peticiones con tag especifico
    public void cancelarPorTag(Object tag) {
        if (colaRequestQueue != null) {
            colaRequestQueue.cancelAll(tag);
        }
    }

    // verificar si hay conectividad
    public boolean hayConectividad() {
        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            android.net.NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }

    // convertir metodo http a string
    private String metodoToString(int metodo) {
        switch (metodo) {
            case Request.Method.GET: return "GET";
            case Request.Method.POST: return "POST";
            case Request.Method.PUT: return "PUT";
            case Request.Method.DELETE: return "DELETE";
            case Request.Method.PATCH: return "PATCH";
            default: return "UNKNOWN";
        }
    }
}