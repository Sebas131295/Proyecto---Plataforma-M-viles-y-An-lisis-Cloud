package com.example.atu_app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class depositarActivity : AppCompatActivity() {
    private lateinit var montoEditText: EditText
    private lateinit var tarjetasSpinner: Spinner
    private lateinit var depositarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depositar)

        montoEditText = findViewById(R.id.ingresoInput)
        tarjetasSpinner = findViewById(R.id.tarjetasSpinner)
        depositarButton = findViewById(R.id.depositarButton)

        // Configurar la lista de tarjetas
        val tarjetas = listOf("Tarjeta 1", "Tarjeta 2", "Tarjeta 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tarjetas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tarjetasSpinner.adapter = adapter

        // Configurar botón de depósito
        depositarButton.setOnClickListener {
            val monto = montoEditText.text.toString().toDoubleOrNull()
            val tarjetaSeleccionada = tarjetasSpinner.selectedItem.toString()

            if (monto != null) {
                // Ir a ConfirmationActivity
                val intent = Intent(this, confirmationActivity::class.java)
                intent.putExtra("monto", monto)
                intent.putExtra("tarjeta", tarjetaSeleccionada)
                startActivity(intent)
            }
        }
    }
}