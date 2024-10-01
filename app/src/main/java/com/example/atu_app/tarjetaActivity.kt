package com.example.atu_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tarjetaActivity : AppCompatActivity() {
    private lateinit var numeroTarjetaEditText: EditText
    private lateinit var nombreTitularEditText: EditText
    private lateinit var tarjetaImageView: TextView
    private lateinit var registrarButton: Button
    private val tarjetasList = mutableListOf<tarjeta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarjeta)

        // Inicializar vistas
        numeroTarjetaEditText = findViewById(R.id.numeroTarjetaEditText)
        nombreTitularEditText = findViewById(R.id.nombreTitularEditText)
        tarjetaImageView = findViewById(R.id.tarjetaImageView)
        registrarButton = findViewById(R.id.registrarButton)

        // Actualizar imagen de la tarjeta al escribir el número
        numeroTarjetaEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tarjetaImageView.text = s.toString() // Mostrar el número en la imagen simulada
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Acción al hacer clic en el botón Registrar
        registrarButton.setOnClickListener {
            val numeroTarjeta = numeroTarjetaEditText.text.toString()
            val nombreTitular = nombreTitularEditText.text.toString()

            // Validar que los campos no estén vacíos
            if (numeroTarjeta.isNotEmpty() && nombreTitular.isNotEmpty()) {
                // Crear y añadir la tarjeta a la lista
                val nuevaTarjeta = tarjeta(numeroTarjeta, nombreTitular)
                tarjetasList.add(nuevaTarjeta)

                // Mostrar mensaje de éxito
                Toast.makeText(this, "Tarjeta registrada", Toast.LENGTH_SHORT).show()

                // Limpiar campos y la imagen simulada de la tarjeta
                numeroTarjetaEditText.text.clear()
                nombreTitularEditText.text.clear()
                tarjetaImageView.text = ""

            } else {
                // Mostrar mensaje de error
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}