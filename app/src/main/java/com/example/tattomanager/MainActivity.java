package com.example.tattomanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast; // Importamos la herramienta para mensajes rápidos [4]

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declaramos el boton de la interfaz
    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Carga la interfaz visual en memoria RAM [7, 8]
        setContentView(R.layout.activity_main);

        // Control de bordes de pantalla del celular (dejar por defecto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vinculamos el boton estrictamente despues de setContentView [6, 7]
        btnIniciar = findViewById(R.id.btnIniciar);

        // Programamos el evento de clic en el hilo principal [9, 10]
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostramos un mensaje emergente para probar que el boton responde [4]
                Toast.makeText(MainActivity.this, "¡Excelente! El botón funciona impecable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}