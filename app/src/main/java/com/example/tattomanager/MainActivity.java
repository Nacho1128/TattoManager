package com.example.tattomanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * actividad principal y panel de control (dashboard) de la aplicacion TattoManager
 *
 * coordina el acceso general a la aplicacion, permitiendo al usuario navegar hacia
 * el formulario de cotizacion, la visualizacion del mapa con ubicacion GPS y la lectura
 * de sensores del dispositivo mediante botones dea acceso rapido y la barra de navegacion
 * inferior ButtomNavigationView
 * **/


public class MainActivity extends AppCompatActivity {

    /**
     * inicializa la vista principal, vincula los botones del panel de control
     * y configura la navegacion por pestañas mediante ButtomNavigationView
     *
     * @param savedInstanceState Estado previamente guardado de la actividad
     * **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnVerMapa = findViewById(R.id.btnVerMapa);
        Button btnVerSensores = findViewById(R.id.btnVerSensores);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
            bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_cotizar) {
                    startActivity(new Intent(MainActivity.this, CotizarActivity.class));
                    return true;
                } else if (itemId == R.id.nav_resumen) {
                    startActivity(new Intent(MainActivity.this, ConfirmacionActivity.class));
                    return true;
                }
                return itemId == R.id.nav_home;
            });
        }
        if (btnIniciar != null) {
            btnIniciar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CotizarActivity.class);
                startActivity(intent);
            });
        }
        if (btnVerMapa != null) {
            btnVerMapa.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            });
        }
        if (btnVerSensores != null) {
            btnVerSensores.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, SensorActivity.class));
            });
        }
    }
}