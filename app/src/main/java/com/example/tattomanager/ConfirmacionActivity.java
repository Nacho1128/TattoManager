package com.example.tattomanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfirmacionActivity extends AppCompatActivity {

    private TextView txtResumen;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmacion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_confirmacion), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enlazamos componentes de la vista
        txtResumen = findViewById(R.id.txtResumen);
        btnVolver = findViewById(R.id.btnVolver);

        // Rescatamos los datos recibidos de la actividad anterior
        Intent intentRecibido = getIntent();
        String cliente = intentRecibido.getStringExtra("CLIENTE");
        String estilo = intentRecibido.getStringExtra("ESTILO");
        String tamano = intentRecibido.getStringExtra("TAMANO");
        String total = intentRecibido.getStringExtra("TOTAL");

        // Formateamos los datos para mostrarlos en la tarjeta
        String resumenTexto = "• Cliente: " + cliente + "\n" +
                "• Estilo: " + estilo + "\n" +
                "• Tamaño: " + tamano + " cm\n\n" +
                "• Total Estimado: $" + total + " CLP";

        txtResumen.setText(resumenTexto);

        // Volver al menu de inicio limpiando la pila de actividades (Back Stack)
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(ConfirmacionActivity.this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentHome);
            }
        });
    }
}