package com.cinefan.actividades;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinefan.R;
import com.cinefan.adapters.PeliculasAdapter;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.Lista;
import com.cinefan.models.ListaDetailResponse;
import com.cinefan.models.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para mostrar los detalles de una lista de películas
 */
public class ListaDetailActivity extends BaseActivity {
    
    private Toolbar toolbar;
    private TextView txtNombre;
    private TextView txtDescripcion;
    private TextView txtCreador;
    private TextView txtFechaCreacion;
    private TextView txtEmpty;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    
    private PeliculasAdapter adapter;
    private List<Pelicula> peliculas = new ArrayList<>();
    
    private CineFanApi apiService;
    private int listaId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_detail);
        
        // Obtener ID de la lista
        if (getIntent().hasExtra("lista_id")) {
            listaId = getIntent().getIntExtra("lista_id", -1);
        }
        
        if (listaId == -1) {
            Toast.makeText(this, "Error al cargar la lista", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Inicializar API
        apiService = ApiClient.getClient().create(CineFanApi.class);
        
        // Inicializar vistas
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.list_details);
        }
        
        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtCreador = findViewById(R.id.txtCreador);
        txtFechaCreacion = findViewById(R.id.txtFechaCreacion);
        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        
        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PeliculasAdapter(this, peliculas);
        recyclerView.setAdapter(adapter);
        
        // Cargar datos
        cargarLista();
    }
    
    private void cargarLista() {
        mostrarProgreso(true);
        
        apiService.getLista(listaId).enqueue(new Callback<ListaDetailResponse>() {
            @Override
            public void onResponse(Call<ListaDetailResponse> call, Response<ListaDetailResponse> response) {
                mostrarProgreso(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ListaDetailResponse listaResponse = response.body();
                    
                    if ("success".equals(listaResponse.getStatus())) {
                        mostrarDetallesLista(listaResponse.getLista());
                        mostrarPeliculas(listaResponse.getPeliculas());
                    } else {
                        mostrarError(listaResponse.getMessage());
                    }
                } else {
                    mostrarError("Error al cargar detalles de la lista");
                }
            }
            
            @Override
            public void onFailure(Call<ListaDetailResponse> call, Throwable t) {
                mostrarProgreso(false);
                mostrarError("Error de conexión: " + t.getMessage());
            }
        });
    }
    
    private void mostrarDetallesLista(Lista lista) {
        if (lista != null) {
            txtNombre.setText(lista.getNombre());
            txtDescripcion.setText(lista.getDescripcion());
            
            // La información de creador podría no estar disponible en el modelo actual
            // txtCreador.setText(lista.getCreador());
            
            txtFechaCreacion.setText(getString(R.string.created_on) + " " + lista.getFechaCreacion());
        }
    }
    
    private void mostrarPeliculas(List<Pelicula> listaPeliculas) {
        if (listaPeliculas != null && !listaPeliculas.isEmpty()) {
            peliculas.clear();
            peliculas.addAll(listaPeliculas);
            adapter.notifyDataSetChanged();
            
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    
    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }
    
    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}