package com.example.tattomanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CotizarActivity extends AppCompatActivity {

    // Declaramos variables
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

        // Enlazamos componentes de la vista (Siempre despues de setContentView)
        edtCliente = findViewById(R.id.edtCliente);
        edtTamano = findViewById(R.id.edtTamano);
        rgEstilos = findViewById(R.id.rgEstilos);
        rbTradicional = findViewById(R.id.rbTradicional);
        btnCalcular = findViewById(R.id.btnCalcular);

        // Click en Calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cliente = edtCliente.getText().toString().trim();
                String tamanoStr = edtTamano.getText().toString().trim();

                // Validamos vacios
                if (cliente.isEmpty()) {
                    edtCliente.setError("Ingresa el nombre del cliente");
                    return;
                }
                if (tamanoStr.isEmpty()) {
                    edtTamano.setError("Ingresa los centimetros");
                    return;
                }

                // Calculo de precio
                int tamano = Integer.parseInt(tamanoStr);
                int valorBase = rbTradicional.isChecked() ? 5000 : 8000;
                int total = tamano * valorBase;

                // Mostramos el calculo temporal en pantalla
                String mensaje = "Cliente: " + cliente + " | Total: $" + total + " CLP";
                Toast.makeText(CotizarActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }
}
