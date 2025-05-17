package com.cinefan.actividades;

import android.content.Intent;
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
import com.cinefan.models.RegisterRequest;
import com.cinefan.models.RegisterResponse;
import com.cinefan.utils.PreferenceManager;
import com.cinefan.utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para el registro de nuevos usuarios
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editFullName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin;
    private ProgressBar progressBar;
    
    private CineFanApi apiService;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Inicializar vistas
        editUsername = findViewById(R.id.editUsername);
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
        progressBar = findViewById(R.id.progressBar);
        
        // Inicializar API y preferencias
        apiService = ApiClient.getClient().create(CineFanApi.class);
        preferenceManager = new PreferenceManager(this);
        
        // Configurar eventos
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volver a la pantalla de login
            }
        });
    }
    
    private void registrar() {
        String username = editUsername.getText().toString().trim();
        String fullName = editFullName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        
        // Validar campos
        if (!validarCampos(username, fullName, email, password, confirmPassword)) {
            return;
        }
        
        // Mostrar progreso
        showProgress(true);
        
        // Crear request
        RegisterRequest request = new RegisterRequest(username, email, password, fullName);
        
        // Llamar a la API
        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                showProgress(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    
                    if ("success".equals(registerResponse.getStatus())) {
                        // Guardar datos de sesión
                        preferenceManager.setLoggedIn(true);
                        preferenceManager.setUserData(
                                registerResponse.getUser().getId(),
                                registerResponse.getUser().getUsername(),
                                registerResponse.getUser().getEmail(),
                                registerResponse.getUser().getFullName(),
                                registerResponse.getUser().getToken()
                        );
                        
                        // Configurar token para futuras peticiones
                        ApiClient.setToken(registerResponse.getUser().getToken());
                        
                        // Ir a pantalla principal
                        irAPantallaPrincipal();
                    } else {
                        Toast.makeText(RegisterActivity.this, 
                                registerResponse.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, 
                            "Error al registrar usuario", 
                            Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showProgress(false);
                Toast.makeText(RegisterActivity.this, 
                        "Error de conexión: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private boolean validarCampos(String username, String fullName, String email, 
                                 String password, String confirmPassword) {
        // Validar nombre de usuario
        if (username.isEmpty()) {
            editUsername.setError(getString(R.string.error_empty_username));
            return false;
        }
        
        // Validar nombre completo
        if (fullName.isEmpty()) {
            editFullName.setError(getString(R.string.error_empty_fullname));
            return false;
        }
        
        // Validar correo
        if (email.isEmpty()) {
            editEmail.setError(getString(R.string.error_empty_email));
            return false;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            editEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }
        
        // Validar contraseña
        if (password.isEmpty()) {
            editPassword.setError(getString(R.string.error_empty_password));
            return false;
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            editPassword.setError(getString(R.string.error_password_requirements));
            return false;
        }
        
        // Validar confirmación de contraseña
        if (confirmPassword.isEmpty()) {
            editConfirmPassword.setError(getString(R.string.error_empty_password));
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError(getString(R.string.error_password_mismatch));
            return false;
        }
        
        return true;
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
    
    private void irAPantallaPrincipal() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
