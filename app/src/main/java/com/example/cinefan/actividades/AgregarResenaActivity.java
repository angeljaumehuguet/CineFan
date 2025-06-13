package com.example.cinefan.actividades;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.cinefan.R;
import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.modelos.Resena;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.PreferenciasUsuario;
import com.example.cinefan.utilidades.ValidadorFormularios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * actividad para agregar nueva resena
 */
public class AgregarResenaActivity extends AppCompatActivity {

    // vistas
    private AutoCompleteTextView editPelicula;
    private RatingBar ratingBar;
    private TextView txtPuntuacion;
    private TextInputEditText editTextoResena;
    private TextView txtContadorCaracteres;
    private Button btnPublicar;
    private ProgressBar progressBar;

    // layouts para errores
    private TextInputLayout layoutPelicula;
    private TextInputLayout layoutTextoResena;

    // datos
    private List<Pelicula> listaPeliculas;
    private Pelicula peliculaSeleccionada;
    private ArrayAdapter<String> adaptadorPeliculas;
    private List<String> nombresPeliculas;

    // utilidades
    private RequestQueue colaRequestQueue;
    private PreferenciasUsuario preferencias;
    private ValidadorFormularios validador;

    // datos para edicion
    private Resena resenaEditando; // null si es nueva, objeto si esta editando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_resena);

        // configurar titulo
        setTitle(R.string.titulo_agregar_resena);

        // habilitar boton de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // inicializar utilidades
        colaRequestQueue = Volley.newRequestQueue(this);
        preferencias = new PreferenciasUsuario(this);
        validador = new ValidadorFormularios();

        // verificar sesion
        if (!preferencias.haySesionActiva()) {
            finish();
            return;
        }

        // inicializar listas
        listaPeliculas = new ArrayList<>();
        nombresPeliculas = new ArrayList<>();

        // enlazar vistas
        enlazarVistas();

        // configurar eventos
        configurarEventos();

        // verificar si esta editando
        verificarModoEdicion();

        // cargar peliculas
        cargarPeliculas();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas() {
        editPelicula = findViewById(R.id.edit_pelicula);
        ratingBar = findViewById(R.id.rating_bar);
        txtPuntuacion = findViewById(R.id.txt_puntuacion);
        editTextoResena = findViewById(R.id.edit_texto_resena);
        txtContadorCaracteres = findViewById(R.id.txt_contador_caracteres);
        btnPublicar = findViewById(R.id.btn_publicar);
        progressBar = findViewById(R.id.progress_bar);

        // layouts
        layoutPelicula = findViewById(R.id.layout_pelicula);
        layoutTextoResena = findViewById(R.id.layout_texto_resena);
    }

    // configurar eventos de los controles
    private void configurarEventos() {
        // rating bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    txtPuntuacion.setText(String.format("%.1f", rating));
                }
            }
        });

        // autocompletado de peliculas
        editPelicula.setOnItemClickListener((parent, view, position, id) -> {
            String nombreSeleccionado = (String) parent.getItemAtPosition(position);
            buscarPeliculaPorNombre(nombreSeleccionado);
        });

        // contador de caracteres
        editTextoResena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int longitud = s.length();
                String contador = longitud + "/" + Constantes.MAX_LONGITUD_RESENA;
                txtContadorCaracteres.setText(contador);

                // cambiar color si se acerca al limite
                if (longitud > Constantes.MAX_LONGITUD_RESENA * 0.9) {
                    txtContadorCaracteres.setTextColor(getResources().getColor(R.color.error, null));
                } else {
                    txtContadorCaracteres.setTextColor(getResources().getColor(R.color.cinefan_text_secondary, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // boton publicar
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentarPublicar();
            }
        });
    }

    // verificar si esta en modo edicion
    private void verificarModoEdicion() {
        if (getIntent().hasExtra(Constantes.EXTRA_RESENA)) {
            resenaEditando = (Resena) getIntent().getSerializableExtra(Constantes.EXTRA_RESENA);
            if (resenaEditando != null) {
                setTitle(R.string.titulo_editar_resena);
                btnPublicar.setText(R.string.actualizar);
                llenarFormulario(resenaEditando);
            }
        }
    }

    // llenar formulario con datos de resena existente
    private void llenarFormulario(Resena resena) {
        // esperar a que carguen las peliculas para seleccionar la correcta
        if (resena.getPelicula() != null) {
            peliculaSeleccionada = resena.getPelicula();
            editPelicula.setText(resena.getPelicula().getTitulo());
        }

        // puntuacion
        ratingBar.setRating(resena.getPuntuacion());
        txtPuntuacion.setText(String.format("%.1f", (float) resena.getPuntuacion()));

        // texto
        editTextoResena.setText(resena.getTextoResena());
    }

    // cargar lista de peliculas
    private void cargarPeliculas() {
        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_PELICULAS_LISTAR;

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuestaPeliculas(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Constantes.TAG_API, "Error cargando peliculas: " + error.getMessage());
                        Toast.makeText(AgregarResenaActivity.this,
                                getString(R.string.error_cargar_peliculas), Toast.LENGTH_SHORT).show();
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

    // procesar respuesta de peliculas
    private void procesarRespuestaPeliculas(JSONObject response) {
        try {
            boolean exito = response.getBoolean("exito");

            if (exito) {
                JSONArray arrayPeliculas = response.getJSONArray("datos");

                // limpiar listas
                listaPeliculas.clear();
                nombresPeliculas.clear();

                // procesar peliculas
                for (int i = 0; i < arrayPeliculas.length(); i++) {
                    JSONObject jsonPelicula = arrayPeliculas.getJSONObject(i);

                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(jsonPelicula.getInt("id"));
                    pelicula.setTitulo(jsonPelicula.getString("titulo"));
                    pelicula.setDirector(jsonPelicula.getString("director"));
                    pelicula.setAnoLanzamiento(jsonPelicula.getInt("ano_lanzamiento"));
                    pelicula.setGenero(jsonPelicula.getString("genero"));

                    listaPeliculas.add(pelicula);
                    nombresPeliculas.add(pelicula.getTitulo());
                }

                // configurar autocompletado
                configurarAutocompletado();

                Log.d(Constantes.TAG_API, "Cargadas " + listaPeliculas.size() + " peliculas");

            } else {
                String mensaje = response.getString("mensaje");
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error procesando peliculas: " + e.getMessage());
            Toast.makeText(this, getString(R.string.error_respuesta_servidor), Toast.LENGTH_SHORT).show();
        }
    }

    // configurar autocompletado de peliculas
    private void configurarAutocompletado() {
        adaptadorPeliculas = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, nombresPeliculas);
        editPelicula.setAdapter(adaptadorPeliculas);
        editPelicula.setThreshold(1); // mostrar sugerencias desde el primer caracter

        // si esta editando, seleccionar pelicula
        if (resenaEditando != null && peliculaSeleccionada != null) {
            editPelicula.setText(peliculaSeleccionada.getTitulo());
        }
    }

    // buscar pelicula por nombre seleccionado
    private void buscarPeliculaPorNombre(String nombre) {
        peliculaSeleccionada = null;

        for (Pelicula pelicula : listaPeliculas) {
            if (pelicula.getTitulo().equals(nombre)) {
                peliculaSeleccionada = pelicula;
                break;
            }
        }

        if (peliculaSeleccionada != null) {
            layoutPelicula.setError(null); // limpiar error si habia
            Log.d(Constantes.TAG_API, "Pelicula seleccionada: " + peliculaSeleccionada.getTitulo());
        }
    }

    // intentar publicar resena
    private void intentarPublicar() {
        // limpiar errores previos
        limpiarErrores();

        // obtener datos
        String textoPelicula = editPelicula.getText().toString().trim();
        float puntuacion = ratingBar.getRating();
        String textoResena = editTextoResena.getText().toString().trim();

        // validar formulario
        if (!validarFormulario(textoPelicula, puntuacion, textoResena)) {
            return;
        }

        // publicar resena
        if (resenaEditando != null) {
            // actualizar resena existente
            actualizarResena((int) puntuacion, textoResena);
        } else {
            // crear nueva resena
            crearResena((int) puntuacion, textoResena);
        }
    }

    // validar formulario
    private boolean validarFormulario(String textoPelicula, float puntuacion, String textoResena) {
        boolean esValido = true;

        // validar pelicula seleccionada
        if (TextUtils.isEmpty(textoPelicula)) {
            layoutPelicula.setError("Selecciona una película");
            esValido = false;
        } else if (peliculaSeleccionada == null) {
            layoutPelicula.setError("Selecciona una película de la lista");
            esValido = false;
        }

        // validar puntuacion
        String errorPuntuacion = validador.validarPuntuacion(puntuacion);
        if (errorPuntuacion != null) {
            Toast.makeText(this, errorPuntuacion, Toast.LENGTH_SHORT).show();
            esValido = false;
        }

        // validar texto de resena
        String errorTexto = validador.validarTextoResena(textoResena);
        if (errorTexto != null) {
            layoutTextoResena.setError(errorTexto);
            esValido = false;
        }

        return esValido;
    }

    // limpiar errores
    private void limpiarErrores() {
        layoutPelicula.setError(null);
        layoutTextoResena.setError(null);
    }

    // crear nueva resena
    private void crearResena(int puntuacion, String textoResena) {
        // mostrar progreso
        mostrarProgreso(true);

        // preparar datos
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_pelicula", peliculaSeleccionada.getId());
            jsonBody.put("puntuacion", puntuacion);
            jsonBody.put("texto_resena", textoResena);
        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error creando JSON: " + e.getMessage());
            mostrarProgreso(false);
            return;
        }

        // hacer peticion
        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_RESENAS_CREAR;
        hacerPeticion(Request.Method.POST, url, jsonBody, false);
    }

    // actualizar resena existente
    private void actualizarResena(int puntuacion, String textoResena) {
        // mostrar progreso
        mostrarProgreso(true);

        // preparar datos
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", resenaEditando.getId());
            jsonBody.put("puntuacion", puntuacion);
            jsonBody.put("texto_resena", textoResena);
        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error creando JSON: " + e.getMessage());
            mostrarProgreso(false);
            return;
        }

        // hacer peticion
        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_RESENAS_ACTUALIZAR;
        hacerPeticion(Request.Method.PUT, url, jsonBody, true);
    }

    // hacer peticion a la api
    private void hacerPeticion(int metodo, String url, JSONObject jsonBody, boolean esActualizacion) {
        JsonObjectRequest peticion = new JsonObjectRequest(
                metodo,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mostrarProgreso(false);
                        procesarRespuesta(response, esActualizacion);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarProgreso(false);
                        Log.e(Constantes.TAG_API, "Error en peticion: " + error.getMessage());

                        String mensajeError = getString(R.string.error_conexion);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            preferencias.cerrarSesion();
                            finish();
                            return;
                        }

                        Toast.makeText(AgregarResenaActivity.this, mensajeError, Toast.LENGTH_LONG).show();
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

    // procesar respuesta
    private void procesarRespuesta(JSONObject response, boolean esActualizacion) {
        try {
            boolean exito = response.getBoolean("exito");
            String mensaje = response.getString("mensaje");

            if (exito) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

                // establecer resultado y cerrar
                setResult(RESULT_OK);
                finish();

            } else {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error procesando respuesta: " + e.getMessage());
            Toast.makeText(this, getString(R.string.error_respuesta_servidor), Toast.LENGTH_SHORT).show();
        }
    }

    // mostrar u ocultar progreso
    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        btnPublicar.setEnabled(!mostrar);

        // deshabilitar campos
        editPelicula.setEnabled(!mostrar);
        ratingBar.setIsIndicator(mostrar);
        editTextoResena.setEnabled(!mostrar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cancelar peticiones pendientes
        if (colaRequestQueue != null) {
            colaRequestQueue.cancelAll(this);
        }
    }
}