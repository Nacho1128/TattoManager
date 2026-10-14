package com.example.tattomanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfirmacionActivity extends AppCompatActivity {
    private TextView txtResumen;
    private Button btnVolver;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);
        txtResumen = findViewById(R.id.txtResumen);
        btnVolver = findViewById(R.id.btnVolver);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        dbHelper = new DatabaseHelper(this);
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_resumen);
            bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_cotizar) {
                    startActivity(new Intent(this, CotizarActivity.class));
                    return true;
                }
                return itemId == R.id.nav_resumen;
            });
        }
        Intent intentRecibido = getIntent();
        String cliente = intentRecibido.getStringExtra("CLIENTE");
        String estilo = intentRecibido.getStringExtra("ESTILO");
        String tamano = intentRecibido.getStringExtra("TAMANO");
        String fechaHora = intentRecibido.getStringExtra("FECHA_HORA");
        String total = intentRecibido.getStringExtra("TOTAL");
        String abono = intentRecibido.getStringExtra("ABONO");
        String saldo = intentRecibido.getStringExtra("SALDO");
        if (cliente == null || cliente.trim().isEmpty()) cliente = "No especificado";
        if (estilo == null || estilo.trim().isEmpty()) estilo = "No especificado";
        if (tamano == null || tamano.trim().isEmpty()) tamano = "0";
        if (fechaHora == null || fechaHora.trim().isEmpty()) fechaHora = "No agendada";
        if (total == null || total.trim().isEmpty()) total = "$0 CLP";
        if (abono == null || abono.trim().isEmpty()) abono = "$0 CLP";
        if (saldo == null || saldo.trim().isEmpty()) saldo = "$0 CLP";
        String resumenTexto = "👤 Cliente:\n" + cliente + "\n\n" +
                "📐 Tamaño:\n" + tamano + " cm\n\n" +
                "🎨 Estilo:\n" + estilo + "\n\n" +
                "📅 Cita Agendada:\n" + fechaHora + "\n\n" +
                "💰 Presupuesto Estimado:\n" + total + "\n" +
                "• Abono (20%): " + abono + "\n" +
                "• Saldo Pendiente: " + saldo;
        txtResumen.setText(resumenTexto);
        if (!cliente.equalsIgnoreCase("No especificado")) {
            boolean guardadoExitoso = dbHelper.guardarCotizacion(cliente, estilo, tamano, fechaHora, total);
            if (guardadoExitoso) {
                Toast.makeText(this, "Cotización guardada en BD local", Toast.LENGTH_SHORT).show();
            }
        }
        btnVolver.setOnClickListener(v -> {
            Intent intentHome = new Intent(ConfirmacionActivity.this, MainActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentHome);
        });
    }
}