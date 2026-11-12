package com.example.tattomanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Locale;

/**
 * actividad encargada del formulario de cotizacion de tatuajes en TattoManager
 *
 * permite ingresar el nombre del cliente, el tamaño del tatuaje en centimetros,
 * agendar la fecha y hora mediante DatePickerDialog y TimePickerDialog, seleccionar
 * el estilo tecnico ($5.000/cm en tradicional/blacwork o $8.000/cm en realismo/color)
 * y calcular el abono de reserva (20%) antes de enviar los datos mediante un Intent a ConfirmacionActivity
 * **/

public class CotizarActivity extends AppCompatActivity {
    private EditText edtNombreCliente, edtTamanoCm;
    private Button btnFecha, btnHora, btnCalcular;
    private TextView txtFechaHoraSelected;
    private RadioGroup rgEstiloTatuaje;
    private RadioButton rbTradicional, rbRealismo;
    private CheckBox chkAbono;
    private BottomNavigationView bottomNavigation;
    private String fechaSeleccionada = "";
    private String horaSeleccionada = "";

    /**
     * inicializa los componentes graficos de la vista, configura los listeners de seleccion
     * de fecha, hora y estilo, gestiona el calculo del presupuesto y administra la navegacion
     * mediante la barra inferior BottomNavigationView
     *
     * @param savedInstanceState Estado previamente guardado de la actividad
     * **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizar);
        edtNombreCliente = findViewById(R.id.edtNombreCliente);
        edtTamanoCm = findViewById(R.id.edtTamanoCm);
        btnFecha = findViewById(R.id.btnFecha);
        btnHora = findViewById(R.id.btnHora);
        btnCalcular = findViewById(R.id.btnCalcular);
        txtFechaHoraSelected = findViewById(R.id.txtFechaHoraSelected);
        rgEstiloTatuaje = findViewById(R.id.rgEstiloTatuaje);
        rbTradicional = findViewById(R.id.rbTradicional);
        rbRealismo = findViewById(R.id.rbRealismo);
        chkAbono = findViewById(R.id.chkAbono);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_cotizar);
            bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home){
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_resumen){
                    startActivity(new Intent(this, ConfirmacionActivity.class));
                    return true;
                }
                return itemId == R.id.nav_cotizar;
            });
        } btnFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) ->
            {
                fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                actualizarTextoCita();
            },anio, mes, dia);
            datePickerDialog.show();
        });
        btnHora.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY); int minuto = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) ->
            {
                horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d hrs", hourOfDay, minute);
                actualizarTextoCita();
            },hora, minuto, true);
            timePickerDialog.show();
        });
        btnCalcular.setOnClickListener(v -> {
            String cliente = edtNombreCliente.getText().toString().trim();
            String tamanoStr = edtTamanoCm.getText().toString().trim();
            if (cliente.isEmpty()) {
                edtNombreCliente.setError("Ingrese el nombre del cliente");
                return;
            }
            if (tamanoStr.isEmpty()) {
                edtTamanoCm.setError("Ingrese el tamaño en cm");
                return;
            }
            int tamanoCm = Integer.parseInt(tamanoStr);
            int precioPorCm = rbRealismo.isChecked() ? 8000 : 5000;
            String estilo = rbRealismo.isChecked() ? "Realismo / Color" : "Tradicional / Blackwork";
            int totalInt = tamanoCm *precioPorCm;
            int abonoInt = chkAbono.isChecked() ? (int) (totalInt *0.20) :0;
            int saldoInt = totalInt - abonoInt;
            String fechaHoraCita = (!fechaSeleccionada.isEmpty() & !horaSeleccionada.isEmpty()) ?fechaSeleccionada + " a las " + horaSeleccionada :
            "No agendada";
            Intent intent = new Intent(CotizarActivity.this, ConfirmacionActivity.class);
            intent.putExtra("CLIENTE", cliente);
            intent.putExtra("ESTILO", estilo);
            intent.putExtra("TAMANO", String.valueOf(tamanoCm));
            intent.putExtra("FECHA_HORA", fechaHoraCita);
            intent.putExtra("TOTAL", "$" + String.format(Locale.GERMANY, "%,d", totalInt) + " CLP");
            intent.putExtra("ABONO", "$" + String.format(Locale.GERMANY, "%,d", abonoInt) + " CLP");
            intent.putExtra("SALDO", "$" + String.format(Locale.GERMANY, "%,d", saldoInt) + " CLP");
            startActivity(intent);
        });
    }

    private void actualizarTextoCita() {
        if (!fechaSeleccionada.isEmpty() || !horaSeleccionada.isEmpty()) {
            txtFechaHoraSelected.setText("Cita: " + fechaSeleccionada + " " + horaSeleccionada);
        }
    }
}