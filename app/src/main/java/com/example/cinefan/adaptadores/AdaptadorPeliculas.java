package com.example.cinefan.adaptadores;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinefan.R;
import com.example.cinefan.modelos.Pelicula;

import java.util.List;

/**
 * adaptador para mostrar lista de peliculas en recyclerview
 * con control contextual de opciones
 */
public class AdaptadorPeliculas extends RecyclerView.Adapter<AdaptadorPeliculas.ViewHolderPelicula> {

    private Context contexto;
    private List<Pelicula> listaPeliculas;
    private OnPeliculaClickListener listener;
    private boolean mostrarOpciones; // Nueva variable para controlar visibilidad de opciones

    // interfaz para manejar clicks
    public interface OnPeliculaClickListener {
        void onPeliculaClick(Pelicula pelicula);
        void onEditarPeliculaClick(Pelicula pelicula);
        void onEliminarPeliculaClick(Pelicula pelicula, int posicion);
    }

    // constructor principal con control de opciones
    public AdaptadorPeliculas(Context contexto, List<Pelicula> listaPeliculas, OnPeliculaClickListener listener, boolean mostrarOpciones) {
        this.contexto = contexto;
        this.listaPeliculas = listaPeliculas;
        this.listener = listener;
        this.mostrarOpciones = mostrarOpciones;
    }

    // constructor de compatibilidad (por defecto muestra opciones)
    public AdaptadorPeliculas(Context contexto, List<Pelicula> listaPeliculas, OnPeliculaClickListener listener) {
        this(contexto, listaPeliculas, listener, true);
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

    // métodos públicos para gestión del adaptador
    public void actualizarDatos(List<Pelicula> nuevaLista) {
        this.listaPeliculas = nuevaLista;
        notifyDataSetChanged();
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

    // nuevo método para cambiar visibilidad de opciones dinámicamente
    public void setMostrarOpciones(boolean mostrarOpciones) {
        this.mostrarOpciones = mostrarOpciones;
        notifyDataSetChanged();
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

            // configurar click del boton opciones - CORREGIDO con PopupMenu
            btnOpciones.setOnClickListener(v -> {
                int posicion = getAdapterPosition();
                if (listener != null && posicion != RecyclerView.NO_POSITION) {
                    mostrarMenuOpciones(v, listaPeliculas.get(posicion), posicion);
                }
            });
        }

        // ========== MÉTODO MEJORADO: Mostrar menú de opciones ==========
        private void mostrarMenuOpciones(View vista, Pelicula pelicula, int posicion) {
            // Crear el PopupMenu
            PopupMenu popupMenu = new PopupMenu(contexto, vista);

            // Crear opciones del menú dinámicamente
            popupMenu.getMenu().add(0, 1, 0, contexto.getString(R.string.editar_pelicula));
            popupMenu.getMenu().add(0, 2, 1, contexto.getString(R.string.eliminar_pelicula));

            // Configurar iconos para las opciones (solo en Android Q+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
                try {
                    // Intentar cargar iconos si existen
                    popupMenu.getMenu().findItem(1).setIcon(ContextCompat.getDrawable(contexto, R.drawable.ic_edit));
                    popupMenu.getMenu().findItem(2).setIcon(ContextCompat.getDrawable(contexto, R.drawable.ic_delete));
                } catch (Exception e) {
                    // Si no existen los iconos, continuar sin ellos
                }
            }

            // Manejar los clicks del menú
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case 1: // Editar película
                        if (listener != null) {
                            listener.onEditarPeliculaClick(pelicula);
                        }
                        return true;

                    case 2: // Eliminar película
                        if (listener != null) {
                            listener.onEliminarPeliculaClick(pelicula, posicion);
                        }
                        return true;

                    default:
                        return false;
                }
            });

            // Mostrar el menú
            popupMenu.show();
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
            if (pelicula.getDuracionMinutos() > 0) {
                txtDuracion.setText(pelicula.getDuracionMinutos() + " min");
                txtDuracion.setVisibility(View.VISIBLE);
            } else {
                txtDuracion.setVisibility(View.GONE);
            }

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

            // ========== CONTROL CONTEXTUAL DE OPCIONES ==========
            // Mostrar u ocultar botón de opciones según el contexto
            if (mostrarOpciones) {
                btnOpciones.setVisibility(View.VISIBLE);
            } else {
                btnOpciones.setVisibility(View.GONE);
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
                                .placeholder(R.drawable.ic_movie_placeholder) // Usar placeholder genérico
                                .error(R.drawable.ic_movie_placeholder) // Y error genérico
                                .transform(new RoundedCorners(8))
                        )
                        .into(imgPelicula);
            } else {
                // mostrar imagen por defecto
                imgPelicula.setImageResource(R.drawable.ic_movie_placeholder);
            }
        }
    }
}