package com.example.tattomanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class CotizarActivity extends AppCompatActivity {

    private EditText edtCliente, edtTamano;
    private RadioGroup rgEstilos;
    private RadioButton rbTradicional;
    private CheckBox chkAbono;
    private Button btnSeleccionarFecha, btnSeleccionarHora, btnCalcular;
    private TextView txtFechaHoraSeleccionada;

    private String fechaSeleccionada = "Por definir";
    private String horaSeleccionada = "Por definir";

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

        edtCliente = findViewById(R.id.edtCliente);
        edtTamano = findViewById(R.id.edtTamano);
        rgEstilos = findViewById(R.id.rgEstilos);
        rbTradicional = findViewById(R.id.rbTradicional);
        chkAbono = findViewById(R.id.chkAbono);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        txtFechaHoraSeleccionada = findViewById(R.id.txtFechaHoraSeleccionada);
        btnCalcular = findViewById(R.id.btnCalcular);

        // Selector de Fecha con DatePickerDialog
        btnSeleccionarFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(CotizarActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        actualizarTextoCita();
                    }, anio, mes, dia);
            datePickerDialog.show();
        });

        // Selector de Hora con TimePickerDialog
        btnSeleccionarHora.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int minuto = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(CotizarActivity.this,
                    (view, hourOfDay, minute) -> {
                        horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d hrs", hourOfDay, minute);
                        actualizarTextoCita();
                    }, hora, minuto, true);
            timePickerDialog.show();
        });

        btnCalcular.setOnClickListener(v -> {
            ocultarTeclado();

            String cliente = edtCliente.getText().toString().trim();
            String tamanoStr = edtTamano.getText().toString().trim();

            if (cliente.isEmpty()) {
                edtCliente.setError("Escribe el nombre del cliente");
                return;
            }
            if (tamanoStr.isEmpty()) {
                edtTamano.setError("Escribe los centímetros");
                return;
            }

            int tamano;
            try {
                tamano = Integer.parseInt(tamanoStr);
                if (tamano <= 0 || tamano > 150) {
                    edtTamano.setError("El tamaño debe ser entre 1 y 150 cm");
                    return;
                }
            } catch (NumberFormatException e) {
                edtTamano.setError("Número inválido");
                return;
            }

            int valorBase = rbTradicional.isChecked() ? 5000 : 8000;
            int total = tamano * valorBase;
            String estilo = rbTradicional.isChecked() ? "Tradicional / Blackwork" : "Realismo / Color";

            int abono = 0;
            int saldo = total;
            if (chkAbono.isChecked()) {
                abono = (int) (total * 0.20);
                saldo = total - abono;
            }

            NumberFormat formatCL = NumberFormat.getInstance(new Locale("es", "CL"));

            Intent intent = new Intent(CotizarActivity.this, ConfirmacionActivity.class);
            intent.putExtra("CLIENTE", cliente);
            intent.putExtra("ESTILO", estilo);
            intent.putExtra("TAMANO", tamanoStr);
            intent.putExtra("FECHA_HORA", fechaSeleccionada + " a las " + horaSeleccionada);
            intent.putExtra("TOTAL", "$" + formatCL.format(total) + " CLP");
            intent.putExtra("ABONO", "$" + formatCL.format(abono) + " CLP");
            intent.putExtra("SALDO", "$" + formatCL.format(saldo) + " CLP");
            startActivity(intent);
        });
    }

    private void actualizarTextoCita() {
        txtFechaHoraSeleccionada.setText("Cita: " + fechaSeleccionada + " - " + horaSeleccionada);
    }

    private void ocultarTeclado() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}