package com.example.tattomanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfirmacionActivity extends AppCompatActivity {

    private TextView txtResumen;
    private Button btnVolver;
    private DatabaseHelper dbHelper;

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

        txtResumen = findViewById(R.id.txtResumen);
        btnVolver = findViewById(R.id.btnVolver);
        dbHelper = new DatabaseHelper(this);

        Intent intentRecibido = getIntent();
        String cliente = intentRecibido.getStringExtra("CLIENTE");
        String estilo = intentRecibido.getStringExtra("ESTILO");
        String tamano = intentRecibido.getStringExtra("TAMANO");
        String fechaHora = intentRecibido.getStringExtra("FECHA_HORA");
        String total = intentRecibido.getStringExtra("TOTAL");
        String abono = intentRecibido.getStringExtra("ABONO");
        String saldo = intentRecibido.getStringExtra("SALDO");

        String resumenTexto = "• Cliente: " + cliente + "\n" +
                "• Estilo: " + estilo + "\n" +
                "• Tamaño: " + tamano + " cm\n" +
                "• Cita: " + fechaHora + "\n\n" +
                "• Valor Total: " + total + "\n" +
                "• Abono (20%): " + abono + "\n" +
                "• Saldo Pendiente: " + saldo;

        txtResumen.setText(resumenTexto);

        // Guardado persistente en la base de datos SQLite de la app
        boolean guardadoExitoso = dbHelper.guardarCotizacion(cliente, estilo, tamano, fechaHora, total);
        if (guardadoExitoso) {
            Toast.makeText(this, "Cotización guardada en BD local", Toast.LENGTH_SHORT).show();
        }

        btnVolver.setOnClickListener(v -> {
            Intent intentHome = new Intent(ConfirmacionActivity.this, MainActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentHome);
        });
    }
}