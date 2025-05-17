package com.cinefan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinefan.R;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.utils.PreferenceManager;

/**
 * Fragmento para la pantalla de inicio
 */
public class HomeFragment extends Fragment {
    
    private TextView txtWelcome;
    private TextView txtDescription;

    private CineFanApi apiService;
    private PreferenceManager preferenceManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar vistas
        txtWelcome = view.findViewById(R.id.txtWelcome);
        txtDescription = view.findViewById(R.id.txtDescription);
        
        // Inicializar preferencias
        preferenceManager = new PreferenceManager(requireContext());
        
        // Inicializar API
        apiService = ApiClient.getClient().create(CineFanApi.class);
        
        // Actualizar texto de bienvenida
        updateWelcomeText();
    }
    
    private void updateWelcomeText() {
        String fullName = preferenceManager.getFullName();
        if (fullName != null && !fullName.isEmpty()) {
            txtWelcome.setText(getString(R.string.welcome_user, fullName));
        } else {
            txtWelcome.setText(R.string.welcome);
        }
    }
}
