package com.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cinefan.R;
import com.cinefan.api.ApiClient;
import com.cinefan.fragments.HomeFragment;
import com.cinefan.fragments.ListasFragment;
import com.cinefan.fragments.PeliculasFragment;
import com.cinefan.fragments.PerfilFragment;
import com.cinefan.utils.NavigationUtils;
import com.cinefan.utils.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Actividad principal que gestiona los fragmentos principales de la aplicación
 */
public class MainActivity extends BaseActivity {
    
    private BottomNavigationView bottomNavigation;
    private PreferenceManager preferenceManager;
    private int currentFragmentId = R.id.navigation_home;
    private boolean doubleBackToExitPressedOnce = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferenceManager = new PreferenceManager(this);
        
        // Configurar token para las peticiones
        String token = preferenceManager.getToken();
        if (!token.isEmpty()) {
            ApiClient.setToken(token);
        }
        
        // Configurar Bottom Navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == currentFragmentId) {
                    // Si ya estamos en ese fragmento, no hacemos nada
                    return true;
                }
                
                Fragment fragment = getFragmentForNavItem(item.getItemId());
                
                if (fragment != null) {
                    // Guardar el ID del fragmento actual
                    currentFragmentId = item.getItemId();
                    
                    // Cargar el fragmento sin añadirlo a la pila de retroceso
                    NavigationUtils.navigateTo(
                            getSupportFragmentManager(),
                            fragment,
                            R.id.frame_container,
                            false);
                    
                    return true;
                }
                
                return false;
            }
        });
        
        // Cargar el fragmento inicial si estamos iniciando la actividad
        if (savedInstanceState == null) {
            NavigationUtils.navigateTo(
                    getSupportFragmentManager(),
                    new HomeFragment(),
                    R.id.frame_container,
                    false);
        } else {
            // Restaurar el estado del fragmento seleccionado
            currentFragmentId = savedInstanceState.getInt("currentFragmentId", R.id.navigation_home);
            bottomNavigation.setSelectedItemId(currentFragmentId);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragmentId", currentFragmentId);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Abrir búsqueda
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
                
            case R.id.action_settings:
                // Abrir configuración
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
                
            case R.id.action_logout:
                logout();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Obtiene un fragmento según el elemento del menú de navegación
     * @param itemId ID del elemento de menú
     * @return Fragmento correspondiente o null si no existe
     */
    private Fragment getFragmentForNavItem(int itemId) {
        if (itemId == R.id.navigation_home) {
            return new HomeFragment();
        } else if (itemId == R.id.navigation_movies) {
            return new PeliculasFragment();
        } else if (itemId == R.id.navigation_lists) {
            return new ListasFragment();
        } else if (itemId == R.id.navigation_profile) {
            return new PerfilFragment();
        }
        return null;
    }
    
    /**
     * Cierra la sesión y muestra la pantalla de login
     */
    private void logout() {
        // Limpiar token y preferencias
        ApiClient.clearToken();
        preferenceManager.clearSession();
        
        // Ir a pantalla de login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // Si no estamos en el fragmento inicial (Home), volvemos a él
        if (currentFragmentId != R.id.navigation_home) {
            bottomNavigation.setSelectedItemId(R.id.navigation_home);
        } else {
            // Implementamos la funcionalidad de doble pulsación para salir
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            
            this.doubleBackToExitPressedOnce = true;
            UiUtils.showToast(this, R.string.press_back_again_to_exit);
            
            // Resetear el flag después de 2 segundos
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    },
                    2000
            );
        }
    }
}