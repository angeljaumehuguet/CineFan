package com.example.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinefan.R;
import com.example.cinefan.adaptadores.AdaptadorResenas;
import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.modelos.Resena;
import com.example.cinefan.modelos.Usuario;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.PreferenciasUsuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ✅ ACTIVIDAD CORREGIDA - FEED DE RESEÑAS
 * Con los IDs exactos del layout XML
 */
public class FeedActivity extends AppCompatActivity implements AdaptadorResenas.OnResenaClickListener {

    // ========== VISTAS ==========
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private LinearLayout layoutSinResenas;  // ✅ Es un LinearLayout contenedor
    private TextView txtSinResenas;
    private FloatingActionButton fabAgregar;

    // ========== DATOS ==========
    private AdaptadorResenas adaptador;
    private List<Resena> listaResenas;

    // ========== UTILIDADES ==========
    private RequestQueue colaRequestQueue;
    private PreferenciasUsuario preferencias;

    // ========== PAGINACIÓN ==========
    private int paginaActual = 1;
    private boolean cargandoMas = false;
    private boolean todasCargadas = false;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Log.d(Constantes.TAG_API, "=== INICIANDO FeedActivity ===");

        // Configurar título
        setTitle(R.string.titulo_feed);

        // Habilitar botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar utilidades
        colaRequestQueue = Volley.newRequestQueue(this);
        preferencias = new PreferenciasUsuario(this);

        // Verificar sesión
        if (!preferencias.haySesionActiva()) {
            Log.w(Constantes.TAG_API, "Sesión no activa, redirigiendo a login");
            irALogin();
            return;
        }

        // Configurar interfaz
        enlazarVistas();
        configurarRecyclerView();
        configurarEventos();

        // Cargar datos iniciales
        cargarFeed();
    }

    // ========== CONFIGURACIÓN DE INTERFAZ ==========

    private void enlazarVistas() {
        // ✅ USANDO LOS IDs EXACTOS DE TU LAYOUT XML
        recyclerView = findViewById(R.id.recycler_resenas);        // ✅ CORRECTO
        swipeRefresh = findViewById(R.id.swipe_refresh);           // ✅ CORRECTO
        progressBar = findViewById(R.id.progress_bar);             // ✅ CORRECTO
        layoutSinResenas = findViewById(R.id.layout_sin_resenas);  // ✅ CORRECTO - LinearLayout contenedor
        txtSinResenas = findViewById(R.id.txt_sin_resenas);        // ✅ CORRECTO
        fabAgregar = findViewById(R.id.fab_agregar);               // ✅ CORRECTO

        // ✅ LOG DE VERIFICACIÓN
        Log.d(Constantes.TAG_API, "✅ RecyclerView encontrado: " + (recyclerView != null));
        Log.d(Constantes.TAG_API, "✅ SwipeRefresh encontrado: " + (swipeRefresh != null));
        Log.d(Constantes.TAG_API, "✅ ProgressBar encontrado: " + (progressBar != null));
        Log.d(Constantes.TAG_API, "✅ Layout sin reseñas encontrado: " + (layoutSinResenas != null));
        Log.d(Constantes.TAG_API, "✅ TextView encontrado: " + (txtSinResenas != null));
        Log.d(Constantes.TAG_API, "✅ FAB encontrado: " + (fabAgregar != null));
    }

    private void configurarRecyclerView() {
        // Configurar lista de reseñas
        listaResenas = new ArrayList<>();
        adaptador = new AdaptadorResenas(this, listaResenas, this);

        // Configurar layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptador);

        // ✅ SCROLL LISTENER PARA PAGINACIÓN
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!cargandoMas && !todasCargadas && dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        cargarMasResenas();
                    }
                }
            }
        });
    }

    private void configurarEventos() {
        // Swipe to refresh
        swipeRefresh.setOnRefreshListener(() -> refrescarFeed());
        swipeRefresh.setColorSchemeResources(R.color.cinefan_primary);

        // Botón agregar reseña
        fabAgregar.setOnClickListener(v -> abrirAgregarResena());
    }

    // ========== CARGA DE DATOS ==========

    private void cargarFeed() {
        Log.d(Constantes.TAG_API, "Cargando feed inicial");
        mostrarProgreso(true);
        paginaActual = 1;
        todasCargadas = false;
        cargarResenas(false);
    }

    private void refrescarFeed() {
        Log.d(Constantes.TAG_API, "Refrescando feed");
        paginaActual = 1;
        todasCargadas = false;
        cargarResenas(true);
    }

    private void cargarMasResenas() {
        if (!cargandoMas && !todasCargadas) {
            Log.d(Constantes.TAG_API, "Cargando más reseñas - Página: " + (paginaActual + 1));
            cargandoMas = true;
            paginaActual++;
            cargarResenas(false);
        }
    }

    private void cargarResenas(boolean esRefresh) {
        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_RESENAS_FEED +
                "?pagina=" + paginaActual + "&limite=10";

        Log.d(Constantes.TAG_API, "Petición feed: " + url);

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> procesarRespuestaFeed(response, esRefresh),
                error -> manejarErrorCarga(error, esRefresh)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + preferencias.obtenerToken());
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        colaRequestQueue.add(peticion);
    }

    // ========== PROCESAMIENTO DE RESPUESTAS ==========

    private void procesarRespuestaFeed(JSONObject response, boolean esRefresh) {
        try {
            Log.d(Constantes.TAG_API, "Procesando respuesta feed");

            boolean exito = response.getBoolean("exito");

            if (exito) {
                JSONArray arrayResenas = response.getJSONArray("datos");

                // Si es refresh, limpiar lista actual
                if (esRefresh || paginaActual == 1) {
                    listaResenas.clear();
                }

                // Agregar nuevas reseñas
                int resenasAgregadas = 0;
                for (int i = 0; i < arrayResenas.length(); i++) {
                    JSONObject jsonResena = arrayResenas.getJSONObject(i);
                    Resena resena = parsearResena(jsonResena);

                    if (resena != null) {
                        listaResenas.add(resena);
                        resenasAgregadas++;
                    }
                }

                // Verificar si hay más datos
                if (resenasAgregadas < 10) {
                    todasCargadas = true;
                }

                // Actualizar vista
                adaptador.notifyDataSetChanged();
                actualizarVistaVacia();

                Log.d(Constantes.TAG_API, "Cargadas " + resenasAgregadas + " reseñas");

            } else {
                String mensaje = response.getString("mensaje");
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error procesando respuesta: " + e.getMessage());
            Toast.makeText(this, "Error procesando respuesta del servidor", Toast.LENGTH_SHORT).show();
        }

        // Ocultar progreso
        mostrarProgreso(false);
        swipeRefresh.setRefreshing(false);
        cargandoMas = false;
    }

    // ✅ MÉTODO CORREGIDO: parsearResena con validación exhaustiva
    private Resena parsearResena(JSONObject jsonResena) {
        try {
            Resena resena = new Resena();

            // Datos básicos de la reseña
            resena.setId(jsonResena.optInt("id", -1));
            resena.setPuntuacion(jsonResena.optInt("puntuacion", 0));
            resena.setTextoResena(jsonResena.optString("texto_resena", ""));
            resena.setFechaResena(jsonResena.optString("fecha_resena", ""));
            resena.setLikes(jsonResena.optInt("likes", 0));

            // ✅ VALIDACIÓN ROBUSTA: Datos del usuario
            Usuario usuario = new Usuario();

            if (jsonResena.has("usuario") && !jsonResena.isNull("usuario")) {
                JSONObject jsonUsuario = jsonResena.getJSONObject("usuario");

                usuario.setId(jsonUsuario.optInt("id", -1));
                usuario.setNombreUsuario(jsonUsuario.optString("nombre_usuario", "Usuario anónimo"));
                usuario.setNombreCompleto(jsonUsuario.optString("nombre_completo", ""));
                usuario.setEmail(jsonUsuario.optString("email", ""));

                if (jsonUsuario.has("avatar_url") && !jsonUsuario.isNull("avatar_url")) {
                    usuario.setAvatarUrl(jsonUsuario.getString("avatar_url"));
                }

            } else {
                Log.w(Constantes.TAG_API, "Datos de usuario faltantes en reseña ID: " +
                        jsonResena.optInt("id", -1));
                usuario.setId(-1);
                usuario.setNombreUsuario("Usuario desconocido");
                usuario.setNombreCompleto("Usuario desconocido");
            }

            resena.setUsuario(usuario);

            // ✅ VALIDACIÓN ROBUSTA: Datos de la película
            if (jsonResena.has("pelicula") && !jsonResena.isNull("pelicula")) {
                JSONObject jsonPelicula = jsonResena.getJSONObject("pelicula");
                Pelicula pelicula = new Pelicula();

                pelicula.setId(jsonPelicula.optInt("id", -1));
                pelicula.setTitulo(jsonPelicula.optString("titulo", "Título desconocido"));
                pelicula.setDirector(jsonPelicula.optString("director", "Director desconocido"));
                pelicula.setAnoLanzamiento(jsonPelicula.optInt("ano_lanzamiento", 0));
                pelicula.setGenero(jsonPelicula.optString("genero", "Sin género"));
                pelicula.setDuracionMinutos(jsonPelicula.optInt("duracion_minutos", 0));

                if (jsonPelicula.has("imagen_url") && !jsonPelicula.isNull("imagen_url")) {
                    pelicula.setImagenUrl(jsonPelicula.getString("imagen_url"));
                }

                resena.setPelicula(pelicula);

            } else {
                Log.w(Constantes.TAG_API, "Datos de película faltantes en reseña ID: " +
                        jsonResena.optInt("id", -1));
            }

            return resena;

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error parseando reseña: " + e.getMessage());
            return null;
        }
    }

    // ========== MANEJO DE ERRORES ==========

    private void manejarErrorCarga(VolleyError error, boolean esRefresh) {
        Log.e(Constantes.TAG_API, "Error cargando feed: " + error.toString());

        String mensajeError = getString(R.string.error_conexion);
        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
            preferencias.cerrarSesion();
            irALogin();
            return;
        }

        Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show();

        mostrarProgreso(false);
        swipeRefresh.setRefreshing(false);
        cargandoMas = false;

        actualizarVistaVacia();
    }

    // ========== INTERFAZ DE USUARIO ==========

    private void actualizarVistaVacia() {
        if (listaResenas.isEmpty()) {
            // ✅ MOSTRAR EL LAYOUT COMPLETO SIN RESEÑAS
            layoutSinResenas.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // ✅ MOSTRAR EL RECYCLERVIEW Y OCULTAR MENSAJE
            layoutSinResenas.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void abrirAgregarResena() {
        Intent intent = new Intent(this, AgregarResenaActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_AGREGAR_RESENA);
    }

    private void irALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // ========== LISTENERS DE CLICKS ==========

    // ✅ MÉTODO CORREGIDO: onResenaClick con validación null
    @Override
    public void onResenaClick(Resena resena) {
        Log.d(Constantes.TAG_API, "Click en reseña: " + (resena != null ? resena.getId() : "null"));

        if (resena == null) {
            Log.e(Constantes.TAG_API, "Reseña es null en onResenaClick");
            Toast.makeText(this, "Error: Reseña no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resena.getUsuario() == null) {
            Log.e(Constantes.TAG_API, "Usuario es null en reseña ID: " + resena.getId());
            Toast.makeText(this, "Error: Usuario no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resena.getUsuario().getNombreUsuario() == null ||
                resena.getUsuario().getNombreUsuario().isEmpty()) {
            Log.e(Constantes.TAG_API, "Nombre de usuario es null/vacío en reseña ID: " + resena.getId());
            Toast.makeText(this, "Reseña de usuario anónimo", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreUsuario = resena.getUsuario().getNombreUsuario();
        Toast.makeText(this, "Reseña de " + nombreUsuario, Toast.LENGTH_SHORT).show();

        // TODO: Implementar activity de detalles de reseña
        // Intent intent = new Intent(this, DetalleResenaActivity.class);
        // intent.putExtra(Constantes.EXTRA_RESENA, resena);
        // startActivity(intent);
    }

    @Override
    public void onUsuarioClick(Usuario usuario) {
        Log.d(Constantes.TAG_API, "Click en usuario: " + (usuario != null ? usuario.getId() : "null"));

        if (usuario == null) {
            Log.e(Constantes.TAG_API, "Usuario es null en onUsuarioClick");
            Toast.makeText(this, "Error: Usuario no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreUsuario = usuario.getNombreUsuario();
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            nombreUsuario = "Usuario anónimo";
        }

        Toast.makeText(this, "Perfil de " + nombreUsuario, Toast.LENGTH_SHORT).show();

        // TODO: Implementar activity de perfil de usuario
        // Intent intent = new Intent(this, PerfilUsuarioActivity.class);
        // intent.putExtra(Constantes.EXTRA_USUARIO, usuario);
        // startActivity(intent);
    }

    @Override
    public void onPeliculaClick(Pelicula pelicula) {
        Log.d(Constantes.TAG_API, "Click en película: " + (pelicula != null ? pelicula.getId() : "null"));

        if (pelicula == null) {
            Log.e(Constantes.TAG_API, "Película es null en onPeliculaClick");
            Toast.makeText(this, "Error: Película no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String titulo = pelicula.getTitulo();
        if (titulo == null || titulo.isEmpty()) {
            titulo = "Película sin título";
        }

        Toast.makeText(this, "Detalles de " + titulo, Toast.LENGTH_SHORT).show();

        // TODO: Implementar activity de detalles de película
        // Intent intent = new Intent(this, DetallePeliculaActivity.class);
        // intent.putExtra(Constantes.EXTRA_PELICULA, pelicula);
        // startActivity(intent);
    }

    // ========== CICLO DE VIDA ==========

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constantes.REQUEST_AGREGAR_RESENA && resultCode == RESULT_OK) {
            Log.d(Constantes.TAG_API, "Nueva reseña agregada, refrescando feed");
            refrescarFeed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refrescar) {
            refrescarFeed();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (colaRequestQueue != null) {
            colaRequestQueue.cancelAll(this);
        }
        Log.d(Constantes.TAG_API, "=== FINALIZANDO FeedActivity ===");
    }
}