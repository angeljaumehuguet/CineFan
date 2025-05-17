package com.cinefan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cinefan.R;
import com.cinefan.actividades.EditProfileActivity;
import com.cinefan.actividades.SettingsActivity;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.utils.PreferenceManager;

/**
 * Fragmento para mostrar el perfil del usuario
 */
public class PerfilFragment extends Fragment {
    
    private ImageView imgPerfil;
    private TextView txtNombre;
    private TextView txtUsuario;
    private TextView txtCorreo;
    private TextView txtFechaRegistro;
    private Button btnEditarPerfil;
    private Button btnAjustes;
    
    private CineFanApi apiService;
    private PreferenceManager preferenceManager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar vistas
        imgPerfil = view.findViewById(R.id.imgPerfil);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtUsuario = view.findViewById(R.id.txtUsuario);
        txtCorreo = view.findViewById(R.id.txtCorreo);
        txtFechaRegistro = view.findViewById(R.id.txtFechaRegistro);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnAjustes = view.findViewById(R.id.btnAjustes);
        
        // Inicializar API y preferencias
        apiService = ApiClient.getClient().create(CineFanApi.class);
        preferenceManager = new PreferenceManager(requireContext());
        
        // Cargar datos del perfil
        cargarDatosPerfil();
        
        // Configurar eventos
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        
        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Actualizar datos del perfil al volver al fragmento
        cargarDatosPerfil();
    }
    
    private void cargarDatosPerfil() {
        // Cargar datos almacenados en preferencias
        String fullName = preferenceManager.getFullName();
        String username = preferenceManager.getUsername();
        String email = preferenceManager.getEmail();
        
        // Mostrar datos en la UI
        txtNombre.setText(fullName);
        txtUsuario.setText(username);
        txtCorreo.setText(email);
        
        // Cargar imagen de perfil (por ahora usamos una imagen por defecto)
        Glide.with(this)
                .load(R.drawable.default_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(imgPerfil);
        
        // Podríamos obtener datos adicionales del perfil desde la API
        // apiService.getProfile()...
    }
}
