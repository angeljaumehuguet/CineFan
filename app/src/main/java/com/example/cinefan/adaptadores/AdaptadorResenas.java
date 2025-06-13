package com.example.cinefan.adaptadores;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinefan.R;
import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.modelos.Resena;
import com.example.cinefan.modelos.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * adaptador para mostrar lista de resenas en recyclerview
 */
public class AdaptadorResenas extends RecyclerView.Adapter<AdaptadorResenas.ViewHolderResena> {

    private Context contexto;
    private List<Resena> listaResenas;
    private OnResenaClickListener listener;

    // formateadores de fecha
    private SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    // interfaz para manejar clicks
    public interface OnResenaClickListener {
        void onResenaClick(Resena resena);
        void onUsuarioClick(Usuario usuario);
        void onPeliculaClick(Pelicula pelicula);
    }

    // constructor
    public AdaptadorResenas(Context contexto, List<Resena> listaResenas, OnResenaClickListener listener) {
        this.contexto = contexto;
        this.listaResenas = listaResenas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderResena onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_resena, parent, false);
        return new ViewHolderResena(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderResena holder, int position) {
        Resena resena = listaResenas.get(position);
        holder.enlazarDatos(resena);
    }

    @Override
    public int getItemCount() {
        return listaResenas.size();
    }

    // view holder para resenas
    public class ViewHolderResena extends RecyclerView.ViewHolder {

        // vistas del item
        private ImageView imgPelicula;
        private TextView txtNombreUsuario;
        private TextView txtFechaResena;
        private TextView txtTituloPelicula;
        private TextView txtDirectorAno;
        private TextView txtGenero;
        private RatingBar ratingBar;
        private TextView txtPuntuacion;
        private TextView txtTextoResena;

        // constructor
        public ViewHolderResena(@NonNull View itemView) {
            super(itemView);

            // enlazar vistas
            imgPelicula = itemView.findViewById(R.id.img_pelicula);
            txtNombreUsuario = itemView.findViewById(R.id.txt_nombre_usuario);
            txtFechaResena = itemView.findViewById(R.id.txt_fecha_resena);
            txtTituloPelicula = itemView.findViewById(R.id.txt_titulo_pelicula);
            txtDirectorAno = itemView.findViewById(R.id.txt_director_ano);
            txtGenero = itemView.findViewById(R.id.txt_genero);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            txtPuntuacion = itemView.findViewById(R.id.txt_puntuacion);
            txtTextoResena = itemView.findViewById(R.id.txt_texto_resena);

            // configurar eventos de click
            configurarEventos();
        }

        // configurar eventos de click
        private void configurarEventos() {
            // click en toda la resena
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onResenaClick(listaResenas.get(position));
                    }
                }
            });

            // click en nombre del usuario
            txtNombreUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Resena resena = listaResenas.get(position);
                        if (resena.getUsuario() != null) {
                            listener.onUsuarioClick(resena.getUsuario());
                        }
                    }
                }
            });

            // click en titulo de pelicula
            txtTituloPelicula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Resena resena = listaResenas.get(position);
                        if (resena.getPelicula() != null) {
                            listener.onPeliculaClick(resena.getPelicula());
                        }
                    }
                }
            });

            // click en imagen de pelicula
            imgPelicula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Resena resena = listaResenas.get(position);
                        if (resena.getPelicula() != null) {
                            listener.onPeliculaClick(resena.getPelicula());
                        }
                    }
                }
            });
        }

        // enlazar datos con las vistas
        public void enlazarDatos(Resena resena) {
            // datos del usuario
            if (resena.getUsuario() != null) {
                txtNombreUsuario.setText(resena.getUsuario().getNombreCompleto());
            } else {
                txtNombreUsuario.setText("Usuario desconocido");
            }

            // fecha de la resena
            txtFechaResena.setText(formatearFecha(resena.getFechaResena()));

            // datos de la pelicula
            if (resena.getPelicula() != null) {
                Pelicula pelicula = resena.getPelicula();

                // titulo
                txtTituloPelicula.setText(pelicula.getTitulo());

                // director y ano
                String directorAno = pelicula.getDirector() + " • " + pelicula.getAnoLanzamiento();
                txtDirectorAno.setText(directorAno);

                // genero
                txtGenero.setText(pelicula.getGenero());

                // imagen de la pelicula
                cargarImagenPelicula(pelicula.getImagenUrl());
            } else {
                txtTituloPelicula.setText("Película desconocida");
                txtDirectorAno.setText("");
                txtGenero.setText("");
                imgPelicula.setImageResource(R.drawable.ic_movie);
            }

            // puntuacion
            float puntuacion = resena.getPuntuacion();
            ratingBar.setRating(puntuacion);
            txtPuntuacion.setText(String.format(Locale.getDefault(), "%.1f", puntuacion));

            // texto de la resena
            txtTextoResena.setText(resena.getTextoResena());

            // expandir/contraer texto largo
            configurarTextoExpandible(txtTextoResena, resena.getTextoResena());
        }

        // formatear fecha para mostrar
        private String formatearFecha(String fechaString) {
            try {
                Date fecha = formatoEntrada.parse(fechaString);
                if (fecha != null) {
                    // verificar si es hoy
                    Date hoy = new Date();
                    long diferencia = hoy.getTime() - fecha.getTime();
                    long diasDiferencia = diferencia / (24 * 60 * 60 * 1000);

                    if (diasDiferencia == 0) {
                        return "Hoy";
                    } else if (diasDiferencia == 1) {
                        return "Ayer";
                    } else if (diasDiferencia < 7) {
                        return "Hace " + diasDiferencia + " días";
                    } else {
                        return formatoSalida.format(fecha);
                    }
                }
            } catch (ParseException e) {
                // si hay error, mostrar fecha original
                return fechaString;
            }
            return fechaString;
        }

        // cargar imagen de la pelicula con glide
        private void cargarImagenPelicula(String imagenUrl) {
            if (!TextUtils.isEmpty(imagenUrl)) {
                Glide.with(contexto)
                        .load(imagenUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_movie)
                                .error(R.drawable.ic_movie)
                                .transform(new RoundedCorners(12))
                        )
                        .into(imgPelicula);
            } else {
                // mostrar imagen por defecto
                imgPelicula.setImageResource(R.drawable.ic_movie);
            }
        }

        // configurar texto expandible para resenas largas
        private void configurarTextoExpandible(TextView textView, String textoCompleto) {
            final int MAX_LINEAS = 3;

            if (textoCompleto.length() > 150) { // si es texto largo
                textView.setMaxLines(MAX_LINEAS);
                textView.setEllipsize(android.text.TextUtils.TruncateAt.END);

                textView.setOnClickListener(new View.OnClickListener() {
                    private boolean expandido = false;

                    @Override
                    public void onClick(View v) {
                        if (expandido) {
                            // contraer
                            textView.setMaxLines(MAX_LINEAS);
                            textView.setEllipsize(android.text.TextUtils.TruncateAt.END);
                        } else {
                            // expandir
                            textView.setMaxLines(Integer.MAX_VALUE);
                            textView.setEllipsize(null);
                        }
                        expandido = !expandido;
                    }
                });
            } else {
                // texto corto - no necesita expansion
                textView.setMaxLines(Integer.MAX_VALUE);
                textView.setEllipsize(null);
                textView.setOnClickListener(null);
            }
        }
    }

    // metodo para actualizar lista
    public void actualizarLista(List<Resena> nuevaLista) {
        this.listaResenas = nuevaLista;
        notifyDataSetChanged();
    }

    // metodo para agregar resenas (paginacion)
    public void agregarResenas(List<Resena> nuevasResenas) {
        int posicionInicial = listaResenas.size();
        listaResenas.addAll(nuevasResenas);
        notifyItemRangeInserted(posicionInicial, nuevasResenas.size());
    }

    // metodo para agregar una resena al inicio
    public void agregarResenaAlInicio(Resena nuevaResena) {
        listaResenas.add(0, nuevaResena);
        notifyItemInserted(0);
    }

    // metodo para eliminar resena
    public void eliminarResena(int posicion) {
        if (posicion >= 0 && posicion < listaResenas.size()) {
            listaResenas.remove(posicion);
            notifyItemRemoved(posicion);
        }
    }

    // metodo para obtener resena en posicion
    public Resena obtenerResena(int posicion) {
        if (posicion >= 0 && posicion < listaResenas.size()) {
            return listaResenas.get(posicion);
        }
        return null;
    }
}