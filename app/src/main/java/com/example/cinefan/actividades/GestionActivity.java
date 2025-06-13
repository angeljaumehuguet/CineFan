package com.example.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.cinefan.R;
import com.example.cinefan.fragmentos.MisPeliculasFragment;
import com.example.cinefan.fragmentos.MisResenasFragment;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.PreferenciasUsuario;

/**
 * actividad para gestionar el contenido del usuario (peliculas y resenas)
 */
public class GestionActivity extends AppCompatActivity {

    // vistas
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton fabAgregar;

    // adaptador
    private AdaptadorPaginasGestion adaptadorPaginas;

    // fragmentos
    private MisPeliculasFragment fragmentPeliculas;
    private MisResenasFragment fragmentResenas;

    // utilidades
    private PreferenciasUsuario preferencias;

    // constantes de tabs
    private static final int TAB_PELICULAS = 0;
    private static final int TAB_RESENAS = 1;
    private static final String[] TITULOS_TABS = {"Mis Películas", "Mis Reseñas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion);

        // configurar titulo
        setTitle(R.string.titulo_gestion);

        // habilitar boton de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // inicializar utilidades
        preferencias = new PreferenciasUsuario(this);

        // verificar sesion
        if (!preferencias.haySesionActiva()) {
            finish();
            return;
        }

        // enlazar vistas
        enlazarVistas();

        // configurar tabs y viewpager
        configurarTabs();

        // configurar eventos
        configurarEventos();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fabAgregar = findViewById(R.id.fab_agregar);
    }

    // configurar tabs y viewpager
    private void configurarTabs() {
        // crear fragmentos
        fragmentPeliculas = MisPeliculasFragment.newInstance();
        fragmentResenas = MisResenasFragment.newInstance();

        // crear adaptador
        adaptadorPaginas = new AdaptadorPaginasGestion();
        viewPager.setAdapter(adaptadorPaginas);

        // conectar tablayout con viewpager
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(TITULOS_TABS[position]);
                    }
                }
        ).attach();
    }

    // configurar eventos
    private void configurarEventos() {
        // fab agregar
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manejarFabClick();
            }
        });

        // cambio de tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                actualizarFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // manejar click del fab segun tab actual
    private void manejarFabClick() {
        int tabActual = viewPager.getCurrentItem();

        switch (tabActual) {
            case TAB_PELICULAS:
                abrirAgregarPelicula();
                break;
            case TAB_RESENAS:
                abrirAgregarResena();
                break;
        }
    }

    // actualizar icono del fab segun tab
    private void actualizarFab(int posicionTab) {
        switch (posicionTab) {
            case TAB_PELICULAS:
                fabAgregar.setImageResource(R.drawable.ic_add_movie);
                fabAgregar.setContentDescription(getString(R.string.agregar_pelicula));
                break;
            case TAB_RESENAS:
                fabAgregar.setImageResource(R.drawable.ic_add_review);
                fabAgregar.setContentDescription(getString(R.string.agregar_resena));
                break;
        }
    }

    // abrir actividad para agregar pelicula
    private void abrirAgregarPelicula() {
        Intent intent = new Intent(this, AgregarPeliculaActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_AGREGAR_PELICULA);
    }

    // abrir actividad para agregar resena
    private void abrirAgregarResena() {
        Intent intent = new Intent(this, AgregarResenaActivity.class);
        startActivityForResult(intent, Constantes.REQUEST_AGREGAR_RESENA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constantes.REQUEST_AGREGAR_PELICULA:
                case Constantes.REQUEST_EDITAR_PELICULA:
                    // notificar al fragmento de peliculas para que recargue
                    if (fragmentPeliculas != null) {
                        fragmentPeliculas.recargarDatos();
                    }
                    break;

                case Constantes.REQUEST_AGREGAR_RESENA:
                case Constantes.REQUEST_EDITAR_RESENA:
                    // notificar al fragmento de resenas para que recargue
                    if (fragmentResenas != null) {
                        fragmentResenas.recargarDatos();
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gestion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refrescar) {
            refrescarTabActual();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // refrescar tab actual
    private void refrescarTabActual() {
        int tabActual = viewPager.getCurrentItem();

        switch (tabActual) {
            case TAB_PELICULAS:
                if (fragmentPeliculas != null) {
                    fragmentPeliculas.recargarDatos();
                }
                break;
            case TAB_RESENAS:
                if (fragmentResenas != null) {
                    fragmentResenas.recargarDatos();
                }
                break;
        }

        Toast.makeText(this, getString(R.string.actualizando), Toast.LENGTH_SHORT).show();
    }

    // adaptador para las paginas del viewpager
    private class AdaptadorPaginasGestion extends FragmentStateAdapter {

        public AdaptadorPaginasGestion() {
            super(GestionActivity.this);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case TAB_PELICULAS:
                    return fragmentPeliculas;
                case TAB_RESENAS:
                    return fragmentResenas;
                default:
                    return fragmentPeliculas;
            }
        }

        @Override
        public int getItemCount() {
            return TITULOS_TABS.length;
        }
    }

    // metodos para que los fragmentos puedan comunicarse con la actividad
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void abrirEditarPelicula(com.example.cinefan.modelos.Pelicula pelicula) {
        Intent intent = new Intent(this, AgregarPeliculaActivity.class);
        intent.putExtra(Constantes.EXTRA_PELICULA, pelicula);
        startActivityForResult(intent, Constantes.REQUEST_EDITAR_PELICULA);
    }

    public void abrirEditarResena(com.example.cinefan.modelos.Resena resena) {
        Intent intent = new Intent(this, AgregarResenaActivity.class);
        intent.putExtra(Constantes.EXTRA_RESENA, resena);
        startActivityForResult(intent, Constantes.REQUEST_EDITAR_RESENA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // actualizar fab al regresar
        actualizarFab(viewPager.getCurrentItem());
    }
}