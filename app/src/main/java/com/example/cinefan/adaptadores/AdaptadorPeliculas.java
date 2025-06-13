package com.example.cinefan.adaptadores;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinefan.R;
import com.example.cinefan.modelos.Pelicula;

import java.util.List;

/**
 * adaptador para mostrar lista de peliculas en recyclerview
 */
public class AdaptadorPeliculas extends RecyclerView.Adapter<AdaptadorPeliculas.ViewHolderPelicula> {

    private Context contexto;
    private List<Pelicula> listaPeliculas;
    private OnPeliculaClickListener listener;

    // interfaz para manejar clicks - CORREGIDA
    public interface OnPeliculaClickListener {
        void onPeliculaClick(Pelicula pelicula);
        void onEditarPeliculaClick(Pelicula pelicula);
        void onEliminarPeliculaClick(Pelicula pelicula, int posicion); // Añadimos la posición para facilitar la eliminación
    }

    // constructor - CORREGIDO
    public AdaptadorPeliculas(Context contexto, List<Pelicula> listaPeliculas, OnPeliculaClickListener listener) {
        this.contexto = contexto;
        this.listaPeliculas = listaPeliculas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderPelicula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_pelicula, parent, false);
        return new ViewHolderPelicula(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPelicula holder, int position) {
        Pelicula pelicula = listaPeliculas.get(position);
        holder.enlazarDatos(pelicula);
    }

    @Override
    public int getItemCount() {
        return listaPeliculas != null ? listaPeliculas.size() : 0;
    }
    

    public void eliminarPelicula(int posicion) {
        if (posicion >= 0 && posicion < listaPeliculas.size()) {
            listaPeliculas.remove(posicion);
            notifyItemRemoved(posicion);
            notifyItemRangeChanged(posicion, listaPeliculas.size());
        }
    }

    public int buscarPosicionPorId(int id) {
        for (int i = 0; i < listaPeliculas.size(); i++) {
            if (listaPeliculas.get(i).getId() == id) {
                return i;
            }
        }
        return -1; // No encontrado
    }


    // viewholder para peliculas
    public class ViewHolderPelicula extends RecyclerView.ViewHolder {

        // vistas del item
        private ImageView imgPelicula;
        private TextView txtTitulo;
        private TextView txtDirectorAno;
        private TextView txtGenero;
        private TextView txtDuracion;
        private TextView txtPuntuacion;
        private TextView txtTotalResenas;
        private ImageButton btnOpciones;

        public ViewHolderPelicula(@NonNull View itemView) {
            super(itemView);

            // enlazar vistas
            imgPelicula = itemView.findViewById(R.id.img_pelicula);
            txtTitulo = itemView.findViewById(R.id.txt_titulo);
            txtDirectorAno = itemView.findViewById(R.id.txt_director_ano);
            txtGenero = itemView.findViewById(R.id.txt_genero);
            txtDuracion = itemView.findViewById(R.id.txt_duracion);
            txtPuntuacion = itemView.findViewById(R.id.txt_puntuacion);
            txtTotalResenas = itemView.findViewById(R.id.txt_total_resenas);
            btnOpciones = itemView.findViewById(R.id.btn_opciones);

            // configurar click del item
            itemView.setOnClickListener(v -> {
                int posicion = getAdapterPosition();
                if (listener != null && posicion != RecyclerView.NO_POSITION) {
                    listener.onPeliculaClick(listaPeliculas.get(posicion));
                }
            });

            // configurar click del boton opciones
            btnOpciones.setOnClickListener(v -> {
                // Aquí puedes mostrar un menú emergente
                // Por ahora, lo simplificamos a dos acciones directas
                int posicion = getAdapterPosition();
                if (listener != null && posicion != RecyclerView.NO_POSITION) {
                    // Para simplificar, puedes tener dos botones en tu layout o un PopupMenu
                    // O delegar la acción al fragmento
                    listener.onEditarPeliculaClick(listaPeliculas.get(posicion));
                }
            });
        }

        // enlazar datos con las vistas
        public void enlazarDatos(Pelicula pelicula) {
            // titulo
            txtTitulo.setText(pelicula.getTitulo());

            // director y año
            String directorAno = pelicula.getDirector() + " • " + pelicula.getAnoLanzamiento();
            txtDirectorAno.setText(directorAno);

            // genero
            txtGenero.setText(pelicula.getGenero());

            // duracion
            //txtDuracion.setText(pelicula.getDuracionFormateada());

            // puntuacion promedio
            if (pelicula.getPuntuacionPromedio() > 0) {
                txtPuntuacion.setText(String.format("%.1f", pelicula.getPuntuacionPromedio()));
                txtPuntuacion.setVisibility(View.VISIBLE);
            } else {
                txtPuntuacion.setText("N/A");
            }

            // total de resenas
            int totalResenas = pelicula.getTotalResenas();
            if (totalResenas > 0) {
                String textoResenas = totalResenas == 1 ?
                        totalResenas + " reseña" : totalResenas + " reseñas";
                txtTotalResenas.setText(textoResenas);
            } else {
                txtTotalResenas.setText("Sin reseñas");
            }

            // imagen de la pelicula
            cargarImagenPelicula(pelicula.getImagenUrl());
        }

        // cargar imagen de la pelicula con glide
        private void cargarImagenPelicula(String imagenUrl) {
            if (!TextUtils.isEmpty(imagenUrl)) {
                Glide.with(contexto)
                        .load(imagenUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_movie) // Asegúrate de tener este drawable
                                .error(R.drawable.ic_movie) // Y este
                                .transform(new RoundedCorners(8))
                        )
                        .into(imgPelicula);
            } else {
                // mostrar imagen por defecto
                imgPelicula.setImageResource(R.drawable.ic_movie);
            }
        }
    }
}
