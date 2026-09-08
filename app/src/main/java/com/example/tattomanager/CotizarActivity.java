package com.example.tattomanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class CotizarActivity extends AppCompatActivity {

    private EditText edtCliente;
    private EditText edtTamano;
    private RadioGroup rgEstilos;
    private RadioButton rbTradicional;
    private CheckBox chkAbono; // Nueva variable para el CheckBox
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

        // Vinculación segura de los componentes visuales
        edtCliente = findViewById(R.id.edtCliente);
        edtTamano = findViewById(R.id.edtTamano);
        rgEstilos = findViewById(R.id.rgEstilos);
        rbTradicional = findViewById(R.id.rbTradicional);
        chkAbono = findViewById(R.id.chkAbono); // Vinculamos la casilla
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ocultar el teclado automáticamente
                View viewFocus = CotizarActivity.this.getCurrentFocus();
                if (viewFocus != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
                    }
                }

                String cliente = edtCliente.getText().toString().trim();
                String tamanoStr = edtTamano.getText().toString().trim();

                // Validación de campos obligatorios
                if (cliente.isEmpty()) {
                    edtCliente.setError("Escribe el nombre del cliente");
                    return;
                }
                if (tamanoStr.isEmpty()) {
                    edtTamano.setError("Escribe los centímetros");
                    return;
                }

                // Prevención de caídas con try-catch
                int tamano;
                try {
                    tamano = Integer.parseInt(tamanoStr);
                    if (tamano <= 0 || tamano > 150) {
                        edtTamano.setError("El tamaño debe ser entre 1 y 150 cm");
                        return;
                    }
                } catch (NumberFormatException e) {
                    edtTamano.setError("Número demasiado grande");
                    return;
                }

                // Cálculo de valor base por centímetros
                int valorBase = rbTradicional.isChecked() ? 5000 : 8000;
                int total = tamano * valorBase;
                String estilo = rbTradicional.isChecked() ? "Tradicional / Blackwork" : "Realismo / Color";

                // --- NUEVA LÓGICA DE ABONO ---
                int abono = 0;
                int saldoPendiente = total;

                if (chkAbono.isChecked()) {
                    abono = (int) (total * 0.20); // Abono del 20%
                    saldoPendiente = total - abono;
                }

                // Formateador de moneda de Chile (puntos de miles)
                NumberFormat formatCL = NumberFormat.getInstance(new Locale("es", "CL"));
                String totalFormateado = "$" + formatCL.format(total) + " CLP";
                String abonoFormateado = "$" + formatCL.format(abono) + " CLP";
                String saldoFormateado = "$" + formatCL.format(saldoPendiente) + " CLP";

                // Enviar datos ordenados a la tercera pantalla mediante Intent explicito
                Intent intent = new Intent(CotizarActivity.this, ConfirmacionActivity.class);
                intent.putExtra("CLIENTE", cliente);
                intent.putExtra("ESTILO", estilo);
                intent.putExtra("TAMANO", tamanoStr);
                intent.putExtra("TOTAL", totalFormateado);
                intent.putExtra("ABONO", abonoFormateado);
                intent.putExtra("SALDO", saldoFormateado);
                startActivity(intent);
            }
        });
    }
}