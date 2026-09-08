package com.example.tattomanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CotizarActivity extends AppCompatActivity {

    private EditText edtCliente;
    private EditText edtTamano;
    private RadioGroup rgEstilos;
    private RadioButton rbTradicional;
    private Button btnCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cotizar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_cotizar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enlazamos componentes despues de setContentView
        edtCliente = findViewById(R.id.edtCliente);
        edtTamano = findViewById(R.id.edtTamano);
        rgEstilos = findViewById(R.id.rgEstilos);
        rbTradicional = findViewById(R.id.rbTradicional);
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cliente = edtCliente.getText().toString().trim();
                String tamanoStr = edtTamano.getText().toString().trim();

                // Validamos campos vacios
                if (cliente.isEmpty()) {
                    edtCliente.setError("Escribe el nombre del cliente");
                    return;
                }
                if (tamanoStr.isEmpty()) {
                    edtTamano.setError("Escribe los centímetros");
                    return;
                }

                // --- PREVENCIÓN DE CAÍDAS (TRY-CATCH) ---
                int tamano;
                try {
                    // Convertimos el texto a numero entero
                    tamano = Integer.parseInt(tamanoStr);

                    // Evitamos numeros negativos o absurdamente gigantes para un tatuaje
                    if (tamano <= 0 || tamano > 150) {
                        edtTamano.setError("El tamaño debe ser entre 1 y 150 cm");
                        return;
                    }
                } catch (NumberFormatException e) {
                    // Si el numero causa desbordamiento de memoria, evitamos el crash catastrófico
                    edtTamano.setError("Número inválido o demasiado grande");
                    return;
                }
                // ----------------------------------------

                // Calculamos el total usando tu logica
                int valorBase = rbTradicional.isChecked() ? 5000 : 8000;
                int total = tamano * valorBase;
                String estilo = rbTradicional.isChecked() ? "Tradicional / Blackwork" : "Realismo / Color";

                // Pasamos los datos usando tus mismas claves que ya funcionan
                Intent intent = new Intent(CotizarActivity.this, ConfirmacionActivity.class);
                intent.putExtra("CLIENTE", cliente);
                intent.putExtra("ESTILO", estilo);
                intent.putExtra("TAMANO", tamanoStr);
                intent.putExtra("TOTAL", String.valueOf(total));
                startActivity(intent);
            }
        });
    }
}