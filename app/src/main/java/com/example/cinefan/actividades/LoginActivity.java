package com.example.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinefan.R;
import com.example.cinefan.modelos.Usuario;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.NotificacionHelper;
import com.example.cinefan.utilidades.PreferenciasUsuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * actividad para el login de usuarios
 */
public class LoginActivity extends AppCompatActivity {

    // vistas
    private EditText editUsuario;
    private EditText editPassword;
    private Button btnLogin;
    private TextView txtRegistrarse;
    private ProgressBar progressBar;

    // utilidades
    private PreferenciasUsuario preferencias;
    private RequestQueue colaRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // inicializar preferencias
        preferencias = new PreferenciasUsuario(this);
        colaRequestQueue = Volley.newRequestQueue(this);

        // verificar si ya hay sesion activa
        if (preferencias.haySesionActiva()) {
            irAMain();
            return;
        }

        // crear canal de notificaciones
        NotificacionHelper.crearCanalNotificaciones(this);

        // enlazar vistas
        enlazarVistas();

        // configurar eventos
        configurarEventos();

        // verificar si viene de registro
        verificarUsuarioRegistrado();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas() {
        editUsuario = findViewById(R.id.edit_usuario);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegistrarse = findViewById(R.id.txt_registrarse);
        progressBar = findViewById(R.id.progress_bar);
    }

    // configurar eventos de los controles
    private void configurarEventos() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentarLogin();
            }
        });

        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irARegistro();
            }
        });
    }

    // verificar si viene de registro exitoso
    private void verificarUsuarioRegistrado() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("usuario_registrado")) {
            String usuario = intent.getStringExtra("usuario_registrado");
            editUsuario.setText(usuario);
            editPassword.requestFocus();
        }
    }

    // intentar hacer login
    private void intentarLogin() {
        // limpiar errores previos
        editUsuario.setError(null);
        editPassword.setError(null);

        // obtener datos de los campos
        String usuario = editUsuario.getText().toString().trim();
        String password = editPassword.getText().toString();

        // validar campos
        if (!validarCampos(usuario, password)) {
            return;
        }

        // realizar peticion de login
        hacerLogin(usuario, password);
    }

    // validar campos del formulario
    private boolean validarCampos(String usuario, String password) {
        boolean esValido = true;

        // validar usuario
        if (TextUtils.isEmpty(usuario)) {
            editUsuario.setError(getString(R.string.error_usuario_vacio));
            editUsuario.requestFocus();
            esValido = false;
        }

        // validar password
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.error_password_vacio));
            if (esValido) { // solo hacer focus si no hay otros errores
                editPassword.requestFocus();
            }
            esValido = false;
        }

        return esValido;
    }

    // hacer login en la api
    private void hacerLogin(String usuario, String password) {
        // mostrar progreso
        mostrarProgreso(true);

        // preparar datos para la peticion
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("usuario", usuario);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            Log.e(Constantes.TAG_API, "Error creando JSON: " + e.getMessage());
            mostrarProgreso(false);
            return;
        }

        // crear peticion
        String url = Constantes.URL_BASE_API + Constantes.ENDPOINT_LOGIN;
        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mostrarProgreso(false);
                        procesarRespuestaLogin(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarProgreso(false);
                        Log.e(Constantes.TAG_API, "Error en login: " + error.getMessage());

                        String mensajeError = getString(R.string.error_conexion);
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 401) {
                                mensajeError = getString(R.string.error_credenciales_incorrectas);
                            } else if (statusCode == 404) {
                                mensajeError = getString(R.string.error_usuario_no_encontrado);
                            }
                        }

                        Toast.makeText(LoginActivity.this, mensajeError, Toast.LENGTH_LONG).show();
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

    // procesar respuesta del login
    private void procesarRespuestaLogin(JSONObject response) {
        try {
            boolean exito = response.getBoolean("exito");
            String mensaje = response.getString("mensaje");

            if (exito) {
                // pillamos primero el objeto "datos"
                JSONObject datos = response.getJSONObject("datos");

                // ahora obtener "usuario" y "token" desde el objeto "datos"
                JSONObject datosUsuario = datos.getJSONObject("usuario");
                String token = datos.getString("token");

                // crear objeto usuario
                Usuario usuario = new Usuario();
                usuario.setId(datosUsuario.getInt("id"));
                usuario.setNombreUsuario(datosUsuario.getString("nombre_usuario"));
                usuario.setEmail(datosUsuario.getString("email"));
                usuario.setNombreCompleto(datosUsuario.getString("nombre_completo"));
                usuario.setFechaRegistro(datosUsuario.getString("fecha_registro"));

                // guardar datos en preferencias
                preferencias.guardarDatosUsuario(
                        token,
                        usuario.getId(),
                        usuario.getNombreUsuario(),
                        usuario.getEmail(),
                        usuario.getNombreCompleto()
                );

                // mostrar mensaje de bienvenida
                Toast.makeText(this,
                        getString(R.string.bienvenida_login, usuario.getNombreCompleto()),
                        Toast.LENGTH_SHORT).show();

                Log.d(Constantes.TAG_API, "Login exitoso para: " + usuario.getNombreUsuario());

                // ir a actividad principal
                irAMain();

            } else {
                // error en el login
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

                // verificar errores especificos
                if (response.has("errores")) {
                    JSONObject errores = response.getJSONObject("errores");

                    if (errores.has("usuario")) {
                        editUsuario.setError(errores.getString("usuario"));
                    }
                    if (errores.has("password")) {
                        editPassword.setError(errores.getString("password"));
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
        btnLogin.setEnabled(!mostrar);

        // deshabilitar campos durante el login
        editUsuario.setEnabled(!mostrar);
        editPassword.setEnabled(!mostrar);
        txtRegistrarse.setEnabled(!mostrar);
    }

    // ir a pantalla principal
    private void irAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // cerrar login para que no se pueda volver con back
    }

    // ir a pantalla de registro
    private void irARegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cancelar peticiones pendientes
        if (colaRequestQueue != null) {
            colaRequestQueue.cancelAll(this);
        }
    }

    @Override
    public void onBackPressed() {
        // preguntar si quiere salir de la app
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.titulo_salir_app))
                .setMessage(getString(R.string.mensaje_salir_app))
                .setPositiveButton(getString(R.string.si), (dialog, which) -> {
                    finishAffinity(); // cerrar toda la app
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}