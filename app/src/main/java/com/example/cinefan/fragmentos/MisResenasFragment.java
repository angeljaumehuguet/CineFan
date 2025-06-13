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
import com.example.cinefan.adaptadores.AdaptadorResenas;
import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.modelos.Resena;
import com.example.cinefan.modelos.Usuario;
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
 * fragmento para mostrar las resenas del usuario actual
 */
public class MisResenasFragment extends Fragment implements AdaptadorResenas.OnResenaClickListener {

    // vistas
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView txtSinResenas;

    // adaptador y datos
    private AdaptadorResenas adaptador;
    private List<Resena> listaResenas;

    // utilidades
    private RequestQueue colaRequestQueue;
    private PreferenciasUsuario preferencias;

    public MisResenasFragment() {
        // constructor vacio requerido
    }

    public static MisResenasFragment newInstance() {
        return new MisResenasFragment();
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
        listaResenas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_resenas, container, false);
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
        cargarResenas();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas(View view) {
        recyclerView = view.findViewById(R.id.recycler_resenas);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);
        txtSinResenas = view.findViewById(R.id.txt_sin_resenas);
    }

    // configurar recyclerview
    private void configurarRecyclerView() {
        // configurar layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // crear y configurar adaptador
        adaptador = new AdaptadorResenas(getContext(), listaResenas, this);
        recyclerView.setAdapter(adaptador);
    }

    // configurar eventos
    private void configurarEventos() {
        // swipe to refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recargarDatos();
            }
        });
    }

    // cargar resenas del usuario
    private void cargarResenas() {
        mostrarProgreso(true);

        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_RESENAS_LISTAR +
                "?usuario=" + preferencias.obtenerIdUsuario();

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaResenas(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        manejarErrorCarga(error);
                    }
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

    // procesar respuesta de resenas
    private void procesarRespuestaResenas(JSONObject response) {
        try {
            boolean exito = response.getBoolean("exito");

            if (exito) {
                JSONArray arrayResenas = response.getJSONArray("datos");

                // limpiar lista actual
                listaResenas.clear();

                // convertir json a objetos resena
                for (int i = 0; i < arrayResenas.length(); i++) {
                    JSONObject jsonResena = arrayResenas.getJSONObject(i);
                    Resena resena = parsearResena(jsonResena);

                    if (resena != null) {
                        listaResenas.add(resena);
                    }
                }

                // actualizar vista
                adaptador.notifyDataSetChanged();
                actualizarVistaVacia();

                Log.d(Constantes.TAG_API, "Cargadas " + listaResenas.size() + " resenas");

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

    // parsear resena desde json
    private Resena parsearResena(JSONObject jsonResena) {
        try {
            Resena resena = new Resena();
            resena.setId(jsonResena.getInt("id"));
            resena.setPuntuacion(jsonResena.getInt("puntuacion"));
            resena.setTextoResena(jsonResena.getString("texto_resena"));
            resena.setFechaResena(jsonResena.getString("fecha_resena"));

            // datos del usuario (el usuario actual)
            Usuario usuario = new Usuario();
            usuario.setId(preferencias.obtenerIdUsuario());
            usuario.setNombreUsuario(preferencias.obtenerNombreUsuario());
            usuario.setNombreCompleto(preferencias.obtenerNombreCompleto());
            resena.setUsuario(usuario);

            // datos de la pelicula
            if (jsonResena.has("pelicula")) {
                JSONObject jsonPelicula = jsonResena.getJSONObject("pelicula");
                Pelicula pelicula = new Pelicula();
                pelicula.setId(jsonPelicula.getInt("id"));
                pelicula.setTitulo(jsonPelicula.getString("titulo"));
                pelicula.setDirector(jsonPelicula.getString("director"));
                pelicula.setAnoLanzamiento(jsonPelicula.getInt("ano_lanzamiento"));
                pelicula.setGenero(jsonPelicula.getString("genero"));

                if (jsonPelicula.has("imagen_url") && !jsonPelicula.isNull("imagen_url")) {
                    pelicula.setImagenUrl(jsonPelicula.getString("imagen_url"));
                }

                resena.setPelicula(pelicula);
            }

            return resena;

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error parseando resena: " + e.getMessage());
            return null;
        }
    }

    // manejar error en carga
    private void manejarErrorCarga(VolleyError error) {
        Log.e(Constantes.TAG_API, "Error cargando resenas: " + error.getMessage());

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
        if (listaResenas.isEmpty()) {
            txtSinResenas.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtSinResenas.setVisibility(View.GONE);
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
        cargarResenas();
    }

    // eliminar resena de la api
    private void eliminarResena(Resena resena, int posicion) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", resena.getId());
        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error creando JSON: " + e.getMessage());
            return;
        }

        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_RESENAS_ELIMINAR;
        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean exito = response.getBoolean("exito");
                            String mensaje = response.getString("mensaje");

                            if (exito) {
                                // eliminar de la lista
                                adaptador.eliminarResena(posicion);
                                actualizarVistaVacia();
                                mostrarMensaje(mensaje);
                            } else {
                                mostrarMensaje(mensaje);
                            }

                        } catch (JSONException e) {
                            mostrarMensaje(getString(R.string.error_respuesta_servidor));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Constantes.TAG_API, "Error eliminando resena: " + error.getMessage());
                        mostrarMensaje(getString(R.string.error_conexion));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + preferencias.obtenerToken());
                return headers;
            }
        };

        colaRequestQueue.add(peticion);
    }

    // buscar posicion de resena por id
    private int buscarPosicionPorId(int idResena) {
        for (int i = 0; i < listaResenas.size(); i++) {
            if (listaResenas.get(i).getId() == idResena) {
                return i;
            }
        }
        return -1;
    }

    // implementar OnResenaClickListener
    @Override
    public void onResenaClick(Resena resena) {
        // mostrar opciones para la resena
        mostrarOpcionesResena(resena);
    }

    @Override
    public void onUsuarioClick(Usuario usuario) {
        // no hacer nada ya que es el usuario actual
        mostrarMensaje("Esta es tu reseña");
    }

    @Override
    public void onPeliculaClick(Pelicula pelicula) {
        // mostrar detalles de la pelicula
        // TODO: implementar activity de detalles
        mostrarMensaje("Detalles de " + pelicula.getTitulo());
    }

    // mostrar opciones para una resena
    private void mostrarOpcionesResena(Resena resena) {
        String[] opciones = {
                getString(R.string.editar_resena),
                getString(R.string.eliminar_resena),
                getString(R.string.ver_pelicula)
        };

        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.opciones_resena))
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // editar
                            editarResena(resena);
                            break;
                        case 1: // eliminar
                            confirmarEliminarResena(resena);
                            break;
                        case 2: // ver pelicula
                            if (resena.getPelicula() != null) {
                                onPeliculaClick(resena.getPelicula());
                            }
                            break;
                    }
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    // editar resena
    private void editarResena(Resena resena) {
        if (getActivity() instanceof GestionActivity) {
            ((GestionActivity) getActivity()).abrirEditarResena(resena);
        }
    }

    // confirmar eliminacion de resena
    private void confirmarEliminarResena(Resena resena) {
        int posicion = buscarPosicionPorId(resena.getId());

        String mensaje = getString(R.string.mensaje_eliminar_resena,
                resena.getPelicula() != null ? resena.getPelicula().getTitulo() : "esta película");

        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.confirmar_eliminacion))
                .setMessage(mensaje)
                .setPositiveButton(getString(R.string.eliminar), (dialog, which) -> {
                    eliminarResena(resena, posicion);
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