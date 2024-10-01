package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class depositarActivity : AppCompatActivity() {
    private lateinit var montoEditText: EditText
    private lateinit var radioGroupMetodo: RadioGroup
    private lateinit var depositarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depositar)

        montoEditText = findViewById(R.id.ingresoInput)
        radioGroupMetodo = findViewById(R.id.metodoRadio)
        depositarButton = findViewById(R.id.depositarButton)

        // Configurar botón de depósito
        depositarButton.setOnClickListener {
            val monto = montoEditText.text.toString().toDoubleOrNull()

            // Obtener el método de pago seleccionado
            val metodoSeleccionadoId = radioGroupMetodo.checkedRadioButtonId
            val metodoSeleccionado = when (metodoSeleccionadoId) {
                R.id.yapeRadio -> "Yape"
                R.id.plinRadio -> "Plin"
                else -> null
            }

            if (monto != null && metodoSeleccionado != null) {
                // Ir a ConfirmationActivity
                val intent = Intent(this, confirmationActivity::class.java)
                intent.putExtra("monto", monto)
                intent.putExtra("metodo", metodoSeleccionado)
                startActivity(intent)
            }
        }
    }
}