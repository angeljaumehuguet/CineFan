package com.cinefan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinefan.R;
import com.cinefan.actividades.AddListaActivity;
import com.cinefan.adapters.ListasAdapter;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.Lista;
import com.cinefan.models.ListaResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento para mostrar las listas del usuario
 */
public class ListasFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ListasAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private TextView txtEmpty;
    
    private CineFanApi apiService;
    private List<Lista> listas = new ArrayList<>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listas, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar vistas
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        fabAdd = view.findViewById(R.id.fabAdd);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        
        // Configurar RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        
        adapter = new ListasAdapter(getContext(), listas);
        recyclerView.setAdapter(adapter);
        
        // Inicializar API
        apiService = ApiClient.getClient().create(CineFanApi.class);
        
        // Configurar eventos
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarListas();
            }
        });
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir actividad para agregar lista
                Intent intent = new Intent(getActivity(), AddListaActivity.class);
                startActivity(intent);
            }
        });
        
        // Cargar datos iniciales
        cargarListas();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recargar listas al volver al fragmento
        cargarListas();
    }
    
    private void cargarListas() {
        mostrarProgreso(true);
        
        apiService.getListas().enqueue(new Callback<ListaResponse>() {
            @Override
            public void onResponse(Call<ListaResponse> call, Response<ListaResponse> response) {
                mostrarProgreso(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ListaResponse listaResponse = response.body();
                    
                    if ("success".equals(listaResponse.getStatus())) {
                        listas.clear();
                        if (listaResponse.getData() != null) {
                            listas.addAll(listaResponse.getData());
                        }
                        adapter.notifyDataSetChanged();
                        
                        actualizarVistaVacia();
                    } else {
                        mostrarError(listaResponse.getMessage());
                    }
                } else {
                    mostrarError("Error al cargar listas");
                }
            }
            
            @Override
            public void onFailure(Call<ListaResponse> call, Throwable t) {
                mostrarProgreso(false);
                mostrarError("Error de conexión: " + t.getMessage());
            }
        });
    }
    
    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        swipeRefresh.setRefreshing(mostrar);
    }
    
    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void actualizarVistaVacia() {
        if (listas.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
