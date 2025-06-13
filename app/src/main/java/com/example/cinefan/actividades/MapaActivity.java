package com.example.cinefan.actividades;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.cinefan.R;
import com.example.cinefan.utilidades.Constantes;

/**
 * Actividad que muestra el mapa con la ubicación de la sede de CineFan
 * VERSIÓN CORREGIDA COMPLETA - Soluciona todos los problemas de Google Maps
 */
public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // Vistas
    private GoogleMap mapa;
    private Marker marcadorSede;
    private ProgressBar progressBar;
    private androidx.cardview.widget.CardView overlayPermisos;
    private LinearLayout overlayCarga;
    private FloatingActionButton fabCentrarSede;
    private FloatingActionButton fabTipoMapa;
    private ImageButton btnNavegacion;

    // Constantes para permisos
    private static final int CODIGO_PERMISO_UBICACION = 1001;

    // Ubicación de la sede
    private final LatLng ubicacionSede = new LatLng(Constantes.LATITUD_SEDE, Constantes.LONGITUD_SEDE);

    // Estado actual del tipo de mapa
    private int tipoMapaActual = GoogleMap.MAP_TYPE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Log.d(Constantes.TAG_MAPA, "Iniciando MapaActivity");

        // Configurar título
        setTitle(getString(R.string.mapa_cinefan));

        // Habilitar botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Enlazar vistas
        enlazarVistas();

        // Configurar eventos
        configurarEventos();

        // Inicializar mapa
        inicializarMapa();
    }

    private void enlazarVistas() {
        try {
            progressBar = findViewById(R.id.progress_bar);

            // VERIFICAR TIPO ANTES DEL CAST
            View overlayPermisosView = findViewById(R.id.overlay_permisos);
            if (overlayPermisosView instanceof CardView) {
                overlayPermisos = (CardView) overlayPermisosView;
            } else {
                Log.e(Constantes.TAG_MAPA, "overlay_permisos no es CardView");
            }

            View overlayCargaView = findViewById(R.id.overlay_carga);
            if (overlayCargaView instanceof LinearLayout) {
                overlayCarga = (LinearLayout) overlayCargaView;
            } else {
                Log.e(Constantes.TAG_MAPA, "overlay_carga no es LinearLayout");
            }

            fabCentrarSede = findViewById(R.id.fab_centrar_sede);
            fabTipoMapa = findViewById(R.id.fab_tipo_mapa);
            btnNavegacion = findViewById(R.id.btn_navegacion);

            // Mostrar overlay de carga inicialmente
            if (overlayCarga != null) {
                overlayCarga.setVisibility(View.VISIBLE);
            }

            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }

            Log.d(Constantes.TAG_MAPA, "Vistas enlazadas correctamente");
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error enlazando vistas: " + e.getMessage());
            e.printStackTrace(); // Mostrar stack trace completo
        }
    }

    private void configurarEventos() {
        try {
            if (fabCentrarSede != null) {
                fabCentrarSede.setOnClickListener(v -> centrarCamaraEnSede());
            }

            if (fabTipoMapa != null) {
                fabTipoMapa.setOnClickListener(v -> cambiarTipoMapa());
            }

            if (btnNavegacion != null) {
                btnNavegacion.setOnClickListener(v -> abrirNavegacion());
            }

            Log.d(Constantes.TAG_MAPA, "Eventos configurados correctamente");
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error configurando eventos: " + e.getMessage());
        }
    }

    private void inicializarMapa() {
        try {
            // Obtener fragmento del mapa
            SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapa);

            if (fragmentoMapa != null) {
                fragmentoMapa.getMapAsync(this);
                Log.d(Constantes.TAG_MAPA, "Solicitando mapa asíncrono");
            } else {
                Log.e(Constantes.TAG_MAPA, "Error: Fragmento de mapa no encontrado");
                Toast.makeText(this, "Error inicializando mapa", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error inicializando mapa: " + e.getMessage());
            Toast.makeText(this, "Error crítico del mapa: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(Constantes.TAG_MAPA, "Mapa listo para usar");
        mapa = googleMap;

        try {
            // Configurar mapa
            configurarMapa();

            // Agregar marcador de la sede
            agregarMarcadorSede();

            // Centrar cámara en la sede
            centrarCamaraEnSede();

            // Configurar listener de clicks en marcadores
            mapa.setOnMarkerClickListener(this);

            // Verificar permisos de ubicación
            verificarPermisosUbicacion();

            // Ocultar overlay de carga
            ocultarOverlayCarga();

            Log.d(Constantes.TAG_MAPA, "Mapa configurado completamente");
            
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error configurando mapa: " + e.getMessage());
            Toast.makeText(this, "Error configurando el mapa", Toast.LENGTH_LONG).show();
        }
    }

    private void configurarMapa() {
        if (mapa == null) return;

        try {
            // Configuración básica
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setMyLocationButtonEnabled(false); // Usar nuestro FAB
            mapa.getUiSettings().setCompassEnabled(true);
            mapa.getUiSettings().setMapToolbarEnabled(true);
            mapa.getUiSettings().setZoomGesturesEnabled(true);
            mapa.getUiSettings().setScrollGesturesEnabled(true);
            mapa.getUiSettings().setTiltGesturesEnabled(true);
            mapa.getUiSettings().setRotateGesturesEnabled(true);

            // Aplicar estilo personalizado solo en modo normal
            aplicarEstiloMapa();

            Log.d(Constantes.TAG_MAPA, "Configuración básica del mapa aplicada");
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error en configuración del mapa: " + e.getMessage());
        }
    }

    private void aplicarEstiloMapa() {
        if (mapa == null || tipoMapaActual != GoogleMap.MAP_TYPE_NORMAL) return;

        try {
            // JSON para estilo personalizado del mapa
            String estiloJson = "[\n" +
                    "  {\n" +
                    "    \"featureType\": \"poi\",\n" +
                    "    \"elementType\": \"labels\",\n" +
                    "    \"stylers\": [\n" +
                    "      {\n" +
                    "        \"visibility\": \"off\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"featureType\": \"transit\",\n" +
                    "    \"elementType\": \"labels\",\n" +
                    "    \"stylers\": [\n" +
                    "      {\n" +
                    "        \"visibility\": \"off\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]";

            MapStyleOptions estilo = new MapStyleOptions(estiloJson);
            boolean exitoso = mapa.setMapStyle(estilo);
            
            if (!exitoso) {
                Log.w(Constantes.TAG_MAPA, "No se pudo aplicar el estilo personalizado");
            } else {
                Log.d(Constantes.TAG_MAPA, "Estilo personalizado aplicado");
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error aplicando estilo: " + e.getMessage());
        }
    }

    private void agregarMarcadorSede() {
        if (mapa == null) return;

        try {
            // Crear marcador personalizado
            BitmapDescriptor iconoPersonalizado = crearIconoPersonalizado();
            
            marcadorSede = mapa.addMarker(new MarkerOptions()
                    .position(ubicacionSede)
                    .title(Constantes.NOMBRE_SEDE)
                    .snippet(Constantes.DIRECCION_SEDE)
                    .icon(iconoPersonalizado != null ? iconoPersonalizado : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );

            if (marcadorSede != null) {
                marcadorSede.setTag("sede");
                Log.d(Constantes.TAG_MAPA, "Marcador de sede agregado");
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error agregando marcador: " + e.getMessage());
            
            // Fallback con marcador por defecto
            try {
                marcadorSede = mapa.addMarker(new MarkerOptions()
                        .position(ubicacionSede)
                        .title(Constantes.NOMBRE_SEDE)
                        .snippet(Constantes.DIRECCION_SEDE)
                );
                if (marcadorSede != null) {
                    marcadorSede.setTag("sede");
                }
            } catch (Exception ex) {
                Log.e(Constantes.TAG_MAPA, "Error crítico agregando marcador: " + ex.getMessage());
            }
        }
    }

    private BitmapDescriptor crearIconoPersonalizado() {
        try {
            // Intentar crear icono personalizado
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_location_cinema);
            if (drawable != null) {
                drawable.setBounds(0, 0, 100, 100);
                
                Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.draw(canvas);
                
                return BitmapDescriptorFactory.fromBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.w(Constantes.TAG_MAPA, "No se pudo crear icono personalizado: " + e.getMessage());
        }
        return null;
    }

    private void centrarCamaraEnSede() {
        if (mapa == null) return;

        try {
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacionSede, 15f));
            Log.d(Constantes.TAG_MAPA, "Cámara centrada en la sede");
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error centrando cámara: " + e.getMessage());
        }
    }

    private void cambiarTipoMapa() {
        if (mapa == null) return;

        try {
            // Ciclar entre tipos de mapa
            switch (tipoMapaActual) {
                case GoogleMap.MAP_TYPE_NORMAL:
                    mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    tipoMapaActual = GoogleMap.MAP_TYPE_SATELLITE;
                    Toast.makeText(this, getString(R.string.mapa_satelite), Toast.LENGTH_SHORT).show();
                    break;
                case GoogleMap.MAP_TYPE_SATELLITE:
                    mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    tipoMapaActual = GoogleMap.MAP_TYPE_HYBRID;
                    Toast.makeText(this, getString(R.string.mapa_hibrido), Toast.LENGTH_SHORT).show();
                    break;
                case GoogleMap.MAP_TYPE_HYBRID:
                    mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    tipoMapaActual = GoogleMap.MAP_TYPE_NORMAL;
                    aplicarEstiloMapa(); // Reaplicar estilo personalizado
                    Toast.makeText(this, getString(R.string.mapa_normal), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error cambiando tipo de mapa: " + e.getMessage());
            Toast.makeText(this, "Error cambiando tipo de mapa", Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permiso concedido
            habilitarMiUbicacion();
        } else {
            // Solicitar permiso
            solicitarPermisoUbicacion();
        }
    }

    private void solicitarPermisoUbicacion() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Mostrar explicación
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permiso_ubicacion_titulo))
                    .setMessage(getString(R.string.permiso_ubicacion_explicacion))
                    .setPositiveButton(getString(R.string.conceder), (dialog, which) -> {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                CODIGO_PERMISO_UBICACION);
                    })
                    .setNegativeButton(getString(R.string.cancelar), null)
                    .show();
        } else {
            // Solicitar directamente
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PERMISO_UBICACION);
        }
    }

    private void habilitarMiUbicacion() {
        if (mapa == null) return;

        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mapa.setMyLocationEnabled(true);
                mapa.getUiSettings().setMyLocationButtonEnabled(false); // Usamos nuestro FAB
                Log.d(Constantes.TAG_MAPA, "Mi ubicación habilitada");
            }
        } catch (SecurityException e) {
            Log.e(Constantes.TAG_MAPA, "Error de seguridad habilitando ubicación: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISO_UBICACION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                habilitarMiUbicacion();
                Toast.makeText(this, getString(R.string.permiso_ubicacion_concedido), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.permiso_ubicacion_denegado), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String tag = (String) marker.getTag();

        if ("sede".equals(tag)) {
            mostrarInformacionSede();
            return true;
        }

        return false;
    }

    @SuppressLint("StringFormatInvalid")
    private void mostrarInformacionSede() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle(Constantes.NOMBRE_SEDE)
                    .setMessage(getString(R.string.informacion_sede, Constantes.DIRECCION_SEDE))
                    .setPositiveButton(getString(R.string.como_llegar), (dialog, which) -> abrirNavegacion())
                    .setNegativeButton(getString(R.string.cerrar), null)
                    .setNeutralButton(getString(R.string.llamar), (dialog, which) -> {
                        Toast.makeText(this, getString(R.string.numero_no_disponible), Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error mostrando información: " + e.getMessage());
        }
    }

    private void abrirNavegacion() {
        try {
            // Crear URI para Google Maps
            String uri = String.format("geo:%f,%f?q=%f,%f(%s)",
                    Constantes.LATITUD_SEDE, Constantes.LONGITUD_SEDE,
                    Constantes.LATITUD_SEDE, Constantes.LONGITUD_SEDE,
                    Uri.encode(Constantes.NOMBRE_SEDE));

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Si no tiene Google Maps, usar navegador
                String urlWeb = "https://www.google.com/maps/dir/?api=1&destination=" +
                        Constantes.LATITUD_SEDE + "," + Constantes.LONGITUD_SEDE;
                Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(urlWeb));
                startActivity(intentWeb);
            }

        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error abriendo navegación: " + e.getMessage());
            Toast.makeText(this, getString(R.string.error_abrir_navegacion), Toast.LENGTH_SHORT).show();
        }
    }

    private void ocultarOverlayCarga() {
        try {
            if (overlayCarga != null) {
                overlayCarga.setVisibility(View.GONE);
            }
            
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            
            Log.d(Constantes.TAG_MAPA, "Overlay de carga ocultado");
        } catch (Exception e) {
            Log.e(Constantes.TAG_MAPA, "Error ocultando overlay: " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_tipo_normal) {
            if (mapa != null) {
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                tipoMapaActual = GoogleMap.MAP_TYPE_NORMAL;
                aplicarEstiloMapa();
            }
            return true;
        } else if (id == R.id.action_tipo_satelite) {
            if (mapa != null) {
                mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                tipoMapaActual = GoogleMap.MAP_TYPE_SATELLITE;
            }
            return true;
        } else if (id == R.id.action_tipo_hibrido) {
            if (mapa != null) {
                mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                tipoMapaActual = GoogleMap.MAP_TYPE_HYBRID;
            }
            return true;
        } else if (id == R.id.action_centrar_sede) {
            centrarCamaraEnSede();
            return true;
        } else if (id == R.id.action_informacion) {
            mostrarInformacionSede();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constantes.TAG_MAPA, "MapaActivity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constantes.TAG_MAPA, "MapaActivity paused");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Constantes.TAG_MAPA, "MapaActivity destroyed");
        
        // Limpiar referencias
        mapa = null;
        marcadorSede = null;
    }
}