package com.cinefan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinefan.R;
import com.cinefan.actividades.ListaDetailActivity;
import com.cinefan.models.Lista;

import java.util.List;

/**
 * Adaptador para mostrar listas en un RecyclerView
 */
public class ListasAdapter extends RecyclerView.Adapter<ListasAdapter.ViewHolder> {
    
    private Context context;
    private List<Lista> listas;
    
    public ListasAdapter(Context context, List<Lista> listas) {
        this.context = context;
        this.listas = listas;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lista lista = listas.get(position);
        
        holder.txtNombre.setText(lista.getNombre());
        holder.txtDescripcion.setText(lista.getDescripcion());
        holder.txtPeliculas.setText(
                context.getString(R.string.movies_count, lista.getTotalPeliculas()));
        
        // Click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListaDetailActivity.class);
                intent.putExtra("lista_id", lista.getId());
                context.startActivity(intent);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return listas.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtDescripcion;
        TextView txtPeliculas;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPeliculas = itemView.findViewById(R.id.txtPeliculas);
        }
    }
}