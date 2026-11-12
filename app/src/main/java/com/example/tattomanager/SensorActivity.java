package com.example.tattomanager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Actividad encargada de la lectura de sensores de hardware (acelerómetro) y procesamiento multihilo en TattoManager
 *
 * Implementa {@link SensorEventListener} para la captura de eventos del sensor de movimiento y utiliza
 * {@link ExecutorService} junto con {@link Handler} para realizar tareas asíncronas en segundo plano sin bloquear
 * el hilo principal de la interfaz de usuario (Main Thread / UI Thread), evitando así fallos de ANR (Application Not Responding)
 **/

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private TextView txtSensorData;
    private TextView txtEstadoHilo;
    private ExecutorService executorService;
    private Handler mainHandler;

    /**
     * Inicializa las vistas, configura el servicio de sensores {@link SensorManager},
     * obtiene la instancia del acelerómetro predeterminado e inicia el pool de hilos secundarios
     *
     * @param savedInstanceState Estado previamente guardado de la actividad
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor); txtSensorData = findViewById(R.id.txtSensorData);
        txtEstadoHilo = findViewById(R.id.txtEstadoHilo);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        ejecutarTareaEnSegundoPlano();
    }

    /**
     * Ejecuta un proceso pesado simulado en un hilo de fondo (Background Thread) utilizando ExecutorService
     * Al finalizar, utiliza Handler y Looper para publicar los resultados de forma segura en la interfaz de usuario (Main Thread)
     **/
    private void ejecutarTareaEnSegundoPlano() {
        txtEstadoHilo.setText("Estado: Iniciando tarea asincrona...");
        executorService.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainHandler.post(() -> {
                txtEstadoHilo.setText("Estado: Tarea en segundo plano completada con exito.");
                Toast.makeText(SensorActivity.this, "Procesamiento multihilo finalizado", Toast.LENGTH_SHORT).show();
            });
        });
    }

    /**
     * Registra el listener del acelerómetro cuando la actividad vuelve al primer plano (onResume),
     * optimizando la escucha de eventos de movimiento
     **/

    @Override
    protected void onResume() {
        super.onResume();
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * Desregistra el listener del sensor cuando la actividad pasa a segundo plano o se pausa (onPause)
     * para mitigar el consumo innecesario de batería en el dispositivo
     **/

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    /**
     * Recibe y procesa los eventos en tiempo real transmitidos por el acelerómetro
     * Captura y da formato a las coordenadas de aceleración en los tres ejes espaciales (X, Y, Z)
     *
     * @param event Objeto {@link SensorEvent} que contiene los valores de lectura de los ejes físicos
     **/

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            String datos = "Acelerometro:\\nX: " + String.format("%.2f", x) + "\\nY: " + String.format("%.2f", y) + "\\nZ: " + String.format("%.2f", z);
            txtSensorData.setText(datos);
        }
    }

    /**
     * Se invoca cuando cambia la precisión del sensor de hardware registrado
     *
     * @param sensor Objeto {@link Sensor} que modificó su nivel de precisión
     * @param accuracy Nuevo nivel de precisión del sensor
     **/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Libera los recursos del ejecutor de hilos {@link ExecutorService} al destruir la actividad
     **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}