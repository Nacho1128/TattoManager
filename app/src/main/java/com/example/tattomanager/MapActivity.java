package com.example.tattomanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * actividad encargada de la geolocalizacion e integracion del GPS en TattoManager
 *
 * solicita y verifica permisos de ubicacion en tiempo de ejecucion (ACCESS_FINE_LOCATION)
 * y utiliza FusedLocationProviderClient para obtener las cordenadas geograficas (latitud y longitud)
 * en tiempo real o presentar las coordenadas fijas del estudio de tatuajes
 **/

public class MapActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView txtCoordenadas;

    /**
     * inicializa los componentes graficos de la vista, obtiene la instancia de
     * FusedLocationProviderClient e inicia la verificación de permisos de ubicación
     *
     * @param savedInstanceState Estado previamente guardado de la actividad
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        txtCoordenadas = findViewById(R.id.txtCoordenadas);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        verificarPermisoUbicacion();
    }

    /**
     * Comprueba si el permiso ACCESS\_FINE\_LOCATION ha sido otorgado por el usuario
     * En caso afirmativo, procede a obtener la posición; de lo contrario, solicita el permiso en runtime
     **/
    private void verificarPermisoUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            obtenerUbicacionActual();
        }
    }

    /**
     * Captura la última ubicación conocida mediante FusedLocationProviderClient
     * Actualiza la vista con las coordenadas de latitud y longitud obtenidas
     * o despliega la ubicación por defecto del estudio si el dispositivo no retorna ubicación
     **/
    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double latitud = location.getLatitude();
                    double longitud = location.getLongitude();
                    txtCoordenadas.setText("Latitud: " + latitud + "\nLongitud: " + longitud);
                } else {
                    txtCoordenadas.setText("Estudio de Tatuajes:\nLatitud: -33.4489\nLongitud: -70.6693");
                }
            });
        }
    }

    /**
     * Procesa la respuesta del usuario ante la solicitud de permisos en tiempo de ejecución
     * Si el permiso es concedido, llama a obtenerUbicacionActual(); de lo contrario, notifica al usuario mediante Toast
     *
     * @param requestCode Código de solicitud del permiso
     * @param permissions Arreglo con los permisos solicitados
     * @param grantResults Resultado de la concesión para cada permiso
     **/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            } else {
                Toast.makeText(this, "Permiso de ubicacion denegado", Toast.LENGTH_SHORT).show();
                txtCoordenadas.setText("Permiso de GPS denegado por el usuario.");
            }
        }
    }
}