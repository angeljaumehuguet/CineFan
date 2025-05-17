package com.cinefan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinefan.R;
import com.cinefan.adapters.PeliculasAdapter;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.Pelicula;
import com.cinefan.models.PeliculaResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento para mostrar el listado de películas
 */
public class PeliculasFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private PeliculasAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    
    private CineFanApi apiService;
    private List<Pelicula> peliculas = new ArrayList<>();
    
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peliculas, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar vistas
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        fabAdd = view.findViewById(R.id.fabAdd);
        
        // Configurar RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        
        adapter = new PeliculasAdapter(getContext(), peliculas);
        recyclerView.setAdapter(adapter);
        
        // Inicializar API
        apiService = ApiClient.getClient().create(CineFanApi.class);
        
        // Configurar eventos
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetList();
            }
        });
        
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir actividad para añadir película
                // Intent intent = new Intent(getActivity(), AddPeliculaActivity.class);
                // startActivity(intent);
            }
        });
        
        // Configurar scroll infinito
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMoreItems();
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        loadPeliculas();
    }
    
    private void loadPeliculas() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getPeliculas(currentPage, 20, null, null, null)
                .enqueue(new Callback<PeliculaResponse>() {
                    @Override
                    public void onResponse(Call<PeliculaResponse> call, Response<PeliculaResponse> response) {
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                        
                        if (response.isSuccessful() && response.body() != null) {
                            PeliculaResponse peliculaResponse = response.body();
                            
                            if ("success".equals(peliculaResponse.getStatus())) {
                                peliculas.addAll(peliculaResponse.getData());
                                adapter.notifyDataSetChanged();
                                
                                // Verificar si es la última página
                                if (currentPage >= peliculaResponse.getPagination().getTotalPages()) {
                                    isLastPage = true;
                                }
                            }
                        } else {
                            showError("Error al cargar películas");
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<PeliculaResponse> call, Throwable t) {
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                        showError("Error de conexión: " + t.getMessage());
                    }
                });
    }
    
    private void loadMoreItems() {
        currentPage++;
        loadPeliculas();
    }
    
    private void resetList() {
        currentPage = 1;
        isLastPage = false;
        peliculas.clear();
        adapter.notifyDataSetChanged();
        loadPeliculas();
    }
    
    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
