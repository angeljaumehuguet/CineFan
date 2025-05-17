package com.cinefan.actividades;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinefan.R;
import com.cinefan.adapters.PeliculasAdapter;
import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.Pelicula;
import com.cinefan.models.PeliculaDetailResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para mostrar los detalles de una película
 */
public class PeliculaDetailActivity extends BaseActivity {
    
    private Toolbar toolbar;
    private ImageView imgPoster;
    private TextView txtTitulo;
    private TextView txtDirector;
    private TextView txtAnio;
    private TextView txtGenero;
    private TextView txtDuracion;
    private RatingBar ratingBar;
    private TextView txtValoraciones;
    private TextView txtSinopsis;
    private Button btnTrailer;
    private Button btnValorar;
    private Button btnAnadirLista;
    private RecyclerView recyclerSimilares;
    private ProgressBar progressBar;
    
    private PeliculasAdapter similarAdapter;
    private List<Pelicula> similarMovies = new ArrayList<>();
    
    private CineFanApi apiService;
    private int peliculaId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula_detail);
        
        // Obtener ID de la película
        if (getIntent().hasExtra("pelicula_id")) {
            peliculaId = getIntent().getIntExtra("pelicula_id", -1);
        }
        
        if (peliculaId == -1) {
            Toast.makeText(this, "Error al cargar la película", Toast.LENGTH_SHORT).show();
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
            actionBar.setTitle(R.string.movie_details);
        }
        
        imgPoster = findViewById(R.id.imgPoster);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDirector = findViewById(R.id.txtDirector);
        txtAnio = findViewById(R.id.txtAnio);
        txtGenero = findViewById(R.id.txtGenero);
        txtDuracion = findViewById(R.id.txtDuracion);
        ratingBar = findViewById(R.id.ratingBar);
        txtValoraciones = findViewById(R.id.txtValoraciones);
        txtSinopsis = findViewById(R.id.txtSinopsis);
        btnTrailer = findViewById(R.id.btnTrailer);
        btnValorar = findViewById(R.id.btnValorar);
        btnAnadirLista = findViewById(R.id.btnAnadirLista);
        recyclerSimilares = findViewById(R.id.recyclerSimilares);
        progressBar = findViewById(R.id.progressBar);
        
        // Configurar RecyclerView para películas similares
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSimilares.setLayoutManager(horizontalLayout);
        similarAdapter = new PeliculasAdapter(this, similarMovies);
        recyclerSimilares.setAdapter(similarAdapter);
        
        // Configurar eventos
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar visualización de tráiler
                Toast.makeText(PeliculaDetailActivity.this, 
                        "Ver tráiler (por implementar)", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnValorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar valoración
                Toast.makeText(PeliculaDetailActivity.this, 
                        "Valorar película (por implementar)", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnAnadirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar añadir a lista
                Toast.makeText(PeliculaDetailActivity.this, 
                        "Añadir a lista (por implementar)", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Cargar datos
        cargarPelicula();
    }
    
    private void cargarPelicula() {
        mostrarProgreso(true);
        
        apiService.getPelicula(peliculaId).enqueue(new Callback<PeliculaDetailResponse>() {
            @Override
            public void onResponse(Call<PeliculaDetailResponse> call, Response<PeliculaDetailResponse> response) {
                mostrarProgreso(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    PeliculaDetailResponse peliculaResponse = response.body();
                    
                    if ("success".equals(peliculaResponse.getStatus())) {
                        mostrarDetallesPelicula(peliculaResponse.getPelicula());
                        mostrarPeliculasSimilares(peliculaResponse.getSimilares());
                    } else {
                        mostrarError(peliculaResponse.getMessage());
                    }
                } else {
                    mostrarError("Error al cargar detalles de la película");
                }
            }
            
            @Override
            public void onFailure(Call<PeliculaDetailResponse> call, Throwable t) {
                mostrarProgreso(false);
                mostrarError("Error de conexión: " + t.getMessage());
            }
        });
    }
    
    private void mostrarDetallesPelicula(Pelicula pelicula) {
        if (pelicula != null) {
            // Mostrar datos básicos
            txtTitulo.setText(pelicula.getTitulo());
            txtDirector.setText(pelicula.getDirector());
            txtAnio.setText(String.valueOf(pelicula.getAnio()));
            txtGenero.setText(pelicula.getGenero());
            txtDuracion.setText(pelicula.getDuracion() + " " + getString(R.string.minutes));
            txtSinopsis.setText(pelicula.getSinopsis());
            
            // Configurar rating
            float rating = pelicula.getValoracion() / 2; // Convertir de 0-10 a 0-5
            ratingBar.setRating(rating);
            txtValoraciones.setText(String.format("%.1f/5 (%d)", rating, pelicula.getTotalValoraciones()));
            
            // Cargar imagen
            Glide.with(this)
                    .load(pelicula.getImagenUrl())
                    .placeholder(R.drawable.placeholder_movie)
                    .error(R.drawable.error_movie)
                    .into(imgPoster);
        }
    }
    
    private void mostrarPeliculasSimilares(List<Pelicula> similares) {
        if (similares != null && !similares.isEmpty()) {
            similarMovies.clear();
            similarMovies.addAll(similares);
            similarAdapter.notifyDataSetChanged();
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