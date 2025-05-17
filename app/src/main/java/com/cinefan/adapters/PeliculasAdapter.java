package com.cinefan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinefan.R;
import com.cinefan.actividades.PeliculaDetailActivity;
import com.cinefan.models.Pelicula;

import java.util.List;

/**
 * Adaptador para mostrar películas en un RecyclerView
 */
public class PeliculasAdapter extends RecyclerView.Adapter<PeliculasAdapter.ViewHolder> {
    
    private Context context;
    private List<Pelicula> peliculas;
    
    public PeliculasAdapter(Context context, List<Pelicula> peliculas) {
        this.context = context;
        this.peliculas = peliculas;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pelicula_grid, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pelicula pelicula = peliculas.get(position);
        
        holder.txtTitulo.setText(pelicula.getTitulo());
        holder.txtAnio.setText(String.valueOf(pelicula.getAnio()));
        holder.txtGenero.setText(pelicula.getGenero());
        holder.ratingBar.setRating(pelicula.getValoracion() / 2); // Convertir de 0-10 a 0-5
        
        // Cargar imagen con Glide
        Glide.with(context)
                .load(pelicula.getImagenUrl())
                .placeholder(R.drawable.placeholder_movie)
                .error(R.drawable.error_movie)
                .centerCrop()
                .into(holder.imgPoster);
        
        // Click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PeliculaDetailActivity.class);
                intent.putExtra("pelicula_id", pelicula.getId());
                context.startActivity(intent);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return peliculas.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtTitulo;
        TextView txtAnio;
        TextView txtGenero;
        RatingBar ratingBar;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtAnio = itemView.findViewById(R.id.txtAnio);
            txtGenero = itemView.findViewById(R.id.txtGenero);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
