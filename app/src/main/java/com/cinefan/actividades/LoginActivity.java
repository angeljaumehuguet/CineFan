package com.cinefan.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cinefan.R;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.LoginRequest;
import com.cinefan.models.LoginResponse;
import com.cinefan.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad de Login
 */
public class LoginActivity extends AppCompatActivity {
    
    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private ProgressBar progressBar;
    
    private CineFanApi apiService;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Verificar si ya está logueado
        preferenceManager = new PreferenceManager(this);
        if (preferenceManager.isLoggedIn()) {
            irAPantallaPrincipal();
            return;
        }
        
        // Inicializar vistas
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        progressBar = findViewById(R.id.progressBar);
        
        // Inicializar API
        apiService = ApiClient.getClient().create(CineFanApi.class);
        
        // Configurar eventos
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void login() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString();
        
        // Validar campos
        if (username.isEmpty()) {
            editUsername.setError("Ingrese el usuario o correo");
            return;
        }
        
        if (password.isEmpty()) {
            editPassword.setError("Ingrese la contraseña");
            return;
        }
        
        // Mostrar progreso
        showProgress(true);
        
        // Crear request
        LoginRequest request = new LoginRequest(username, password);
        
        // Llamar a la API
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showProgress(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    if ("success".equals(loginResponse.getStatus())) {
                        // Guardar datos de sesión
                        preferenceManager.setLoggedIn(true);
                        preferenceManager.setUserData(
                                loginResponse.getUser().getId(),
                                loginResponse.getUser().getUsername(),
                                loginResponse.getUser().getEmail(),
                                loginResponse.getUser().getFullName(),
                                loginResponse.getUser().getToken()
                        );
                        
                        // Configurar token para futuras peticiones
                        ApiClient.setToken(loginResponse.getUser().getToken());
                        
                        // Ir a pantalla principal
                        irAPantallaPrincipal();
                    } else {
                        Toast.makeText(LoginActivity.this, 
                                loginResponse.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, 
                            "Error al iniciar sesión", 
                            Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, 
                        "Error de conexión: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
    
    private void irAPantallaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
