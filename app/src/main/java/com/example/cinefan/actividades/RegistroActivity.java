package com.example.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinefan.R;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.ValidadorFormularios;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * actividad para registro de nuevos usuarios
 */
public class RegistroActivity extends AppCompatActivity {

    // vistas
    private EditText editNombreCompleto;
    private EditText editUsuario;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editConfirmarPassword;
    private Button btnRegistrar;
    private TextView txtIniciarSesion;
    private ProgressBar progressBar;

    // utilidades
    private RequestQueue colaRequestQueue;
    private ValidadorFormularios validador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // configurar titulo
        setTitle(R.string.titulo_registro);

        // habilitar boton de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // inicializar utilidades
        colaRequestQueue = Volley.newRequestQueue(this);
        validador = new ValidadorFormularios();

        // enlazar vistas
        enlazarVistas();

        // configurar eventos
        configurarEventos();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas() {
        editNombreCompleto = findViewById(R.id.edit_nombre_completo);
        editUsuario = findViewById(R.id.edit_usuario);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editConfirmarPassword = findViewById(R.id.edit_confirmar_password);
        btnRegistrar = findViewById(R.id.btn_registrar);
        txtIniciarSesion = findViewById(R.id.txt_iniciar_sesion);
        progressBar = findViewById(R.id.progress_bar);
    }

    // configurar eventos de los controles
    private void configurarEventos() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentarRegistro();
            }
        });

        txtIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irALogin();
            }
        });
    }

    // intentar registrar usuario
    private void intentarRegistro() {
        // limpiar errores previos
        limpiarErrores();

        // obtener datos de los campos
        String nombreCompleto = editNombreCompleto.getText().toString().trim();
        String usuario = editUsuario.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();
        String confirmarPassword = editConfirmarPassword.getText().toString();

        // validar formulario
        if (!validarFormulario(nombreCompleto, usuario, email, password, confirmarPassword)) {
            return;
        }

        // realizar peticion de registro
        registrarUsuario(nombreCompleto, usuario, email, password);
    }

    // validar formulario de registro
    private boolean validarFormulario(String nombreCompleto, String usuario, String email,
                                      String password, String confirmarPassword) {
        boolean esValido = true;

        // validar nombre completo
        if (TextUtils.isEmpty(nombreCompleto)) {
            editNombreCompleto.setError(getString(R.string.error_nombre_vacio));
            esValido = false;
        } else if (nombreCompleto.length() < 2) {
            editNombreCompleto.setError(getString(R.string.error_nombre_muy_corto));
            esValido = false;
        }

        // validar nombre de usuario
        String errorUsuario = validador.validarNombreUsuario(usuario);
        if (errorUsuario != null) {
            editUsuario.setError(errorUsuario);
            esValido = false;
        }

        // validar email
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.error_email_vacio));
            esValido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError(getString(R.string.error_email_invalido));
            esValido = false;
        }

        // validar password
        String errorPassword = validador.validarPassword(password);
        if (errorPassword != null) {
            editPassword.setError(errorPassword);
            esValido = false;
        }

        // validar confirmacion de password
        if (TextUtils.isEmpty(confirmarPassword)) {
            editConfirmarPassword.setError(getString(R.string.error_confirmar_password_vacio));
            esValido = false;
        } else if (!password.equals(confirmarPassword)) {
            editConfirmarPassword.setError(getString(R.string.error_passwords_no_coinciden));
            esValido = false;
        }

        return esValido;
    }

    // limpiar errores de validacion
    private void limpiarErrores() {
        editNombreCompleto.setError(null);
        editUsuario.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        editConfirmarPassword.setError(null);
    }

    // registrar usuario en la api
    private void registrarUsuario(String nombreCompleto, String usuario, String email, String password) {
        // mostrar progreso
        mostrarProgreso(true);

        // preparar datos para la peticion
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nombre_completo", nombreCompleto);
            jsonBody.put("nombre_usuario", usuario);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error creando JSON: " + e.getMessage());
            mostrarProgreso(false);
            return;
        }

        // crear peticion
        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_REGISTRO;
        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mostrarProgreso(false);
                        procesarRespuestaRegistro(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarProgreso(false);
                        Log.e(Constantes.TAG_API, "Error en registro: " + error.getMessage());

                        String mensajeError = getString(R.string.error_conexion);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                            mensajeError = getString(R.string.error_usuario_existente);
                        }

                        Toast.makeText(RegistroActivity.this, mensajeError, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // agregar peticion a la cola
        colaRequestQueue.add(peticion);
    }

    // procesar respuesta del registro
    private void procesarRespuestaRegistro(JSONObject response) {
        try {
            boolean exito = response.getBoolean("exito");
            String mensaje = response.getString("mensaje");

            if (exito) {
                // registro exitoso
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

                // regresar a login
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("usuario_registrado", editUsuario.getText().toString().trim());
                startActivity(intent);
                finish();

            } else {
                // error en el registro
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

                // verificar errores especificos
                if (response.has("errores")) {
                    JSONObject errores = response.getJSONObject("errores");

                    if (errores.has("nombre_usuario")) {
                        editUsuario.setError(errores.getString("nombre_usuario"));
                    }
                    if (errores.has("email")) {
                        editEmail.setError(errores.getString("email"));
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error procesando respuesta: " + e.getMessage());
            Toast.makeText(this, getString(R.string.error_respuesta_servidor), Toast.LENGTH_SHORT).show();
        }
    }

    // mostrar u ocultar progreso
    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        btnRegistrar.setEnabled(!mostrar);

        // deshabilitar campos durante el registro
        editNombreCompleto.setEnabled(!mostrar);
        editUsuario.setEnabled(!mostrar);
        editEmail.setEnabled(!mostrar);
        editPassword.setEnabled(!mostrar);
        editConfirmarPassword.setEnabled(!mostrar);
    }

    // ir a pantalla de login
    private void irALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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