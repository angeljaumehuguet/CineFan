package com.example.cinefan.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import com.example.cinefan.actividades.GestionActivity;
import com.example.cinefan.adaptadores.AdaptadorPeliculas;
import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.PreferenciasUsuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fragmento para mostrar las peliculas del usuario actual
 */
public class MisPeliculasFragment extends Fragment implements AdaptadorPeliculas.OnPeliculaClickListener {

    // vistas
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView txtSinPeliculas;

    // adaptador y datos
    private AdaptadorPeliculas adaptador;
    private List<Pelicula> listaPeliculas;

    // utilidades
    private RequestQueue colaRequestQueue;
    private PreferenciasUsuario preferencias;

    public MisPeliculasFragment() {
        // constructor vacio requerido
    }

    public static MisPeliculasFragment newInstance() {
        return new MisPeliculasFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inicializar utilidades
        if (getContext() != null) {
            colaRequestQueue = Volley.newRequestQueue(getContext());
            preferencias = new PreferenciasUsuario(getContext());
        }

        // inicializar lista
        listaPeliculas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_peliculas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // enlazar vistas
        enlazarVistas(view);

        // configurar recyclerview
        configurarRecyclerView();

        // configurar eventos
        configurarEventos();

        // cargar datos
        cargarPeliculas();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas(View view) {
        recyclerView = view.findViewById(R.id.recycler_peliculas);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);
        txtSinPeliculas = view.findViewById(R.id.txt_sin_peliculas);
    }

    // configurar recyclerview
    private void configurarRecyclerView() {
        // configurar layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // crear y configurar adaptador - CORREGIDO
        adaptador = new AdaptadorPeliculas(getContext(), listaPeliculas, this);
        recyclerView.setAdapter(adaptador);
    }

    // configurar eventos
    private void configurarEventos() {
        // swipe to refresh
        swipeRefresh.setOnRefreshListener(this::recargarDatos);
    }

    // cargar peliculas del usuario
    private void cargarPeliculas() {
        mostrarProgreso(true);

        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_PELICULAS_LISTAR +
                "?usuario=" + preferencias.obtenerIdUsuario();

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::procesarRespuestaPeliculas,
                this::manejarErrorCarga
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + preferencias.obtenerToken());
                return headers;
            }
        };

        colaRequestQueue.add(peticion);
    }

    // procesar respuesta de peliculas
    private void procesarRespuestaPeliculas(JSONObject response) {
        try {
            boolean exito = response.getBoolean("exito");

            if (exito) {
                JSONArray arrayPeliculas = response.getJSONArray("datos");

                // limpiar lista actual
                listaPeliculas.clear();

                // convertir json a objetos pelicula
                for (int i = 0; i < arrayPeliculas.length(); i++) {
                    JSONObject jsonPelicula = arrayPeliculas.getJSONObject(i);
                    Pelicula pelicula = parsearPelicula(jsonPelicula);

                    if (pelicula != null) {
                        listaPeliculas.add(pelicula);
                    }
                }

                // actualizar vista
                adaptador.notifyDataSetChanged();
                actualizarVistaVacia();

                Log.d(Constantes.TAG_API, "Cargadas " + listaPeliculas.size() + " peliculas");

            } else {
                String mensaje = response.getString("mensaje");
                mostrarMensaje(mensaje);
            }

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error procesando respuesta: " + e.getMessage());
            mostrarMensaje(getString(R.string.error_respuesta_servidor));
        }

        // ocultar progreso
        mostrarProgreso(false);
        swipeRefresh.setRefreshing(false);
    }

    // parsear pelicula desde json
    private Pelicula parsearPelicula(JSONObject jsonPelicula) {
        try {
            Pelicula pelicula = new Pelicula();
            pelicula.setId(jsonPelicula.getInt("id"));
            pelicula.setTitulo(jsonPelicula.getString("titulo"));
            pelicula.setDirector(jsonPelicula.getString("director"));
            pelicula.setAnoLanzamiento(jsonPelicula.getInt("ano_lanzamiento"));
            pelicula.setDuracionMinutos(jsonPelicula.optInt("duracion_minutos"));
            pelicula.setGenero(jsonPelicula.getString("genero"));

            if (jsonPelicula.has("imagen_url") && !jsonPelicula.isNull("imagen_url")) {
                pelicula.setImagenUrl(jsonPelicula.getString("imagen_url"));
            }

            // estadisticas de resenas
            pelicula.setPuntuacionPromedio(jsonPelicula.optDouble("puntuacion_promedio"));
            pelicula.setTotalResenas(jsonPelicula.optInt("total_resenas"));

            return pelicula;

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error parseando pelicula: " + e.getMessage());
            return null;
        }
    }

    // manejar error en carga
    private void manejarErrorCarga(VolleyError error) {
        Log.e(Constantes.TAG_API, "Error cargando peliculas: " + error.getMessage());

        String mensajeError = getString(R.string.error_conexion);
        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
            // token expirado
            preferencias.cerrarSesion();
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }

        mostrarMensaje(mensajeError);

        // ocultar progreso
        mostrarProgreso(false);
        swipeRefresh.setRefreshing(false);

        // actualizar vista
        actualizarVistaVacia();
    }

    // actualizar vista cuando no hay datos
    private void actualizarVistaVacia() {
        if (listaPeliculas.isEmpty()) {
            txtSinPeliculas.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtSinPeliculas.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // mostrar u ocultar progreso
    private void mostrarProgreso(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    // mostrar mensaje
    private void mostrarMensaje(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    // recargar datos (publico para que lo llame la actividad)
    public void recargarDatos() {
        cargarPeliculas();
    }

    // eliminar pelicula de la api
    private void eliminarPelicula(Pelicula pelicula, int posicion) {

        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_PELICULAS_ELIMINAR + "/" + pelicula.getId();
        
        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    try {
                        boolean exito = response.getBoolean("exito");
                        String mensaje = response.getString("mensaje");

                        if (exito) {
                            // eliminar de la lista
                            adaptador.eliminarPelicula(posicion);
                            actualizarVistaVacia();
                            mostrarMensaje(mensaje);
                        } else {
                            mostrarMensaje(mensaje);
                        }

                    } catch (JSONException e) {
                        mostrarMensaje(getString(R.string.error_respuesta_servidor));
                    }
                },
                error -> {
                    Log.e(Constantes.TAG_API, "Error eliminando pelicula: " + error.getMessage());
                    mostrarMensaje(getString(R.string.error_conexion));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + preferencias.obtenerToken());
                return headers;
            }
        };

        colaRequestQueue.add(peticion);
    }

    // implementar OnPeliculaClickListener - CORREGIDO
    @Override
    public void onPeliculaClick(Pelicula pelicula) {
        // mostrar detalles de la pelicula
        // TODO: implementar activity de detalles
        mostrarMensaje("Detalles de " + pelicula.getTitulo());
    }
    
    @Override
    public void onEditarPeliculaClick(Pelicula pelicula) {
        // delegar a la actividad para editar
        if (getActivity() instanceof GestionActivity) {
            ((GestionActivity) getActivity()).abrirEditarPelicula(pelicula);
        }
    }

    @Override
    public void onEliminarPeliculaClick(Pelicula pelicula, int posicion) {
        // confirmar eliminacion
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.confirmar_eliminacion))
                .setMessage(getString(R.string.mensaje_eliminar_pelicula, pelicula.getTitulo()))
                .setPositiveButton(getString(R.string.eliminar), (dialog, which) -> {
                    eliminarPelicula(pelicula, posicion);
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // cancelar peticiones pendientes
        if (colaRequestQueue != null) {
            colaRequestQueue.cancelAll(this);
        }
    }
}
