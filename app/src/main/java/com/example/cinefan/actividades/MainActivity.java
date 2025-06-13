package com.example.cinefan.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinefan.R;
import com.example.cinefan.servicios.ServicioMusica;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.NotificacionHelper;
import com.example.cinefan.utilidades.PreferenciasUsuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * actividad principal de la aplicacion
 */
public class MainActivity extends AppCompatActivity {

    // vistas
    private TextView txtBienvenida;
    private TextView txtDescripcion;
    private TextView txtFechaHora;
    private Button btnFeed;
    private Button btnGestion;
    private Button btnMapa;
    private ImageButton btnMusica;

    // utilidades
    private PreferenciasUsuario preferencias;
    private boolean musicaActiva;

    // intent del servicio de musica
    private Intent intentServicioMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // configurar titulo de la app
        setTitle(R.string.app_name);

        // inicializar preferencias
        preferencias = new PreferenciasUsuario(this);

        // verificar sesion
        if (!preferencias.haySesionActiva()) {
            irALogin();
            return;
        }

        // crear canal de notificaciones
        NotificacionHelper.crearCanalNotificaciones(this);

        // enlazar vistas
        enlazarVistas();

        // configurar contenido
        configurarContenido();

        // configurar eventos
        configurarEventos();

        // configurar musica
        configurarMusica();

        // actualizar fecha y hora
        actualizarFechaHora();
    }

    // enlazar vistas con sus ids
    private void enlazarVistas() {
        txtBienvenida = findViewById(R.id.txt_bienvenida);
        txtDescripcion = findViewById(R.id.txt_descripcion);
        txtFechaHora = findViewById(R.id.txt_fecha_hora);
        btnFeed = findViewById(R.id.btn_feed);
        btnGestion = findViewById(R.id.btn_gestion);
        btnMapa = findViewById(R.id.btn_mapa);
        btnMusica = findViewById(R.id.btn_musica);
    }

    // configurar contenido de la pantalla
    private void configurarContenido() {
        // personalizar mensaje de bienvenida
        String nombreCompleto = preferencias.obtenerNombreCompleto();
        if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
            txtBienvenida.setText(getString(R.string.bienvenida_usuario, nombreCompleto));
        } else {
            String nombreUsuario = preferencias.obtenerNombreUsuario();
            txtBienvenida.setText(getString(R.string.bienvenida_usuario, nombreUsuario));
        }

        // configurar descripcion
        txtDescripcion.setText(R.string.descripcion_main);
    }

    // configurar eventos de los botones
    private void configurarEventos() {
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFeed();
            }
        });

        btnGestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGestion();
            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa();
            }
        });

        btnMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alternarMusica();
            }
        });
    }

    // configurar musica de fondo
    private void configurarMusica() {
        // obtener estado actual de la musica
        musicaActiva = preferencias.obtenerEstadoMusica();

        // actualizar icono del boton
        actualizarIconoMusica();

        // crear intent del servicio
        intentServicioMusica = new Intent(this, ServicioMusica.class);

        // iniciar musica si esta activa
        if (musicaActiva) {
            iniciarServicioMusica();
        }
    }

    // alternar estado de la musica
    private void alternarMusica() {
        musicaActiva = !musicaActiva;

        // guardar nuevo estado en preferencias
        preferencias.guardarEstadoMusica(musicaActiva);

        // actualizar icono
        actualizarIconoMusica();

        if (musicaActiva) {
            // iniciar musica
            iniciarServicioMusica();
            Toast.makeText(this, getString(R.string.musica_activada), Toast.LENGTH_SHORT).show();
        } else {
            // detener musica
            detenerServicioMusica();
            Toast.makeText(this, getString(R.string.musica_desactivada), Toast.LENGTH_SHORT).show();
        }
    }

    // actualizar icono del boton de musica
    private void actualizarIconoMusica() {
        if (musicaActiva) {
            btnMusica.setImageResource(R.drawable.ic_music_on);
            btnMusica.setContentDescription(getString(R.string.desactivar_musica));
        } else {
            btnMusica.setImageResource(R.drawable.ic_music_off);
            btnMusica.setContentDescription(getString(R.string.activar_musica));
        }
    }

    // iniciar servicio de musica
    private void iniciarServicioMusica() {
        try {
            startService(intentServicioMusica);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_iniciar_musica), Toast.LENGTH_SHORT).show();
        }
    }

    // detener servicio de musica
    private void detenerServicioMusica() {
        try {
            stopService(intentServicioMusica);
        } catch (Exception e) {
            // no hay problema si el servicio no estaba iniciado
        }
    }

    // actualizar fecha y hora actual
    private void actualizarFechaHora() {
        SimpleDateFormat formato = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy",
                new Locale("es", "ES"));
        String fechaActual = formato.format(new Date());

        // capitalizar primera letra
        fechaActual = fechaActual.substring(0, 1).toUpperCase() + fechaActual.substring(1);

        txtFechaHora.setText(fechaActual);
    }

    // abrir feed de resenas
    private void abrirFeed() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    // abrir gestion de contenido
    private void abrirGestion() {
        Intent intent = new Intent(this, GestionActivity.class);
        startActivity(intent);
    }

    // abrir mapa
    private void abrirMapa() {
        Intent intent = new Intent(this, MapaActivity.class);
        startActivity(intent);
    }

    // ir a login
    private void irALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // cerrar main para que no se pueda volver
    }

    // mostrar informacion de la app
    private void mostrarInformacionApp() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.acerca_de_cinefan))
                .setMessage(getString(R.string.informacion_app))
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(getString(R.string.entendido), null)
                .show();
    }

    // mostrar dialogo de configuracion
    private void mostrarConfiguracion() {
        String[] opciones = {
                getString(R.string.configurar_notificaciones),
                getString(R.string.configurar_cuenta),
                getString(R.string.ayuda_soporte)
        };

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.configuracion))
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // configurar notificaciones
                            Toast.makeText(this, getString(R.string.proximamente), Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            // configurar cuenta
                            Toast.makeText(this, getString(R.string.proximamente), Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            // ayuda y soporte
                            mostrarAyuda();
                            break;
                    }
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    // mostrar ayuda
    private void mostrarAyuda() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ayuda))
                .setMessage(getString(R.string.mensaje_ayuda))
                .setPositiveButton(getString(R.string.entendido), null)
                .show();
    }

    // confirmar cierre de sesion
    private void confirmarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmar_cerrar_sesion))
                .setMessage(getString(R.string.mensaje_cerrar_sesion))
                .setPositiveButton(getString(R.string.cerrar_sesion), (dialog, which) -> {
                    cerrarSesion();
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    // cerrar sesion del usuario
    private void cerrarSesion() {
        // detener musica si esta activa
        if (musicaActiva) {
            detenerServicioMusica();
        }

        // limpiar sesion
        preferencias.cerrarSesion();

        // mostrar mensaje
        Toast.makeText(this, getString(R.string.sesion_cerrada), Toast.LENGTH_SHORT).show();

        // ir a login
        irALogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_configuracion) {
            mostrarConfiguracion();
            return true;
        } else if (id == R.id.action_acerca_de) {
            mostrarInformacionApp();
            return true;
        } else if (id == R.id.action_cerrar_sesion) {
            confirmarCerrarSesion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // verificar sesion por si acaso
        if (!preferencias.haySesionActiva()) {
            irALogin();
            return;
        }

        // actualizar fecha/hora
        actualizarFechaHora();

        // restaurar estado de musica si es necesario
        boolean musicaPreferida = preferencias.obtenerEstadoMusica();
        if (musicaPreferida != musicaActiva) {
            musicaActiva = musicaPreferida;
            actualizarIconoMusica();

            if (musicaActiva) {
                iniciarServicioMusica();
            } else {
                detenerServicioMusica();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // la musica puede seguir reproduciendose en segundo plano
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // detener servicio de musica si la actividad se destruye completamente
        if (isFinishing()) {
            detenerServicioMusica();
        }
    }

    @Override
    public void onBackPressed() {
        // preguntar si quiere salir de la app
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.titulo_salir_app))
                .setMessage(getString(R.string.mensaje_salir_app))
                .setPositiveButton(getString(R.string.salir), (dialog, which) -> {
                    // detener musica antes de salir
                    if (musicaActiva) {
                        detenerServicioMusica();
                    }
                    finishAffinity(); // cerrar toda la app
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }
}