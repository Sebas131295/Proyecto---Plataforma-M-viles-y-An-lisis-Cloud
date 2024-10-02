package com.example.atu_app

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class tarjetaActivity : AppCompatActivity() {
    private lateinit var numeroTarjetaEditText: EditText
    private lateinit var dniTitularEditText: EditText
    private lateinit var tarjetaImageView: TextView
    private lateinit var registrarButton: Button
    private lateinit var actualizarButton: Button

    // Inicializar el helper de base de datos
    private lateinit var dbHelper: databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarjeta)

        // Inicializar el databaseHelper
        dbHelper = databaseHelper(this)

        // Inicializar vistas
        numeroTarjetaEditText = findViewById(R.id.numeroTarjetaEditText)
        dniTitularEditText = findViewById(R.id.dniTitularEditText)
        tarjetaImageView = findViewById(R.id.tarjetaImageView)
        registrarButton = findViewById(R.id.registrarButton)
        actualizarButton = findViewById(R.id.actualizarButton)

        // Ocultar el botón de actualizar por defecto
        actualizarButton.visibility = View.GONE

        // Obtener el DNI del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val dniUsuario = sharedPreferences.getString("dniUsuario", "")

        // Verificar si ya existe una tarjeta para este usuario
        val tarjetaExistente = dbHelper.obtenerTarjetaPorDni(dniUsuario!!)

        if (tarjetaExistente != null) {
            // Si existe una tarjeta, mostrar los datos
            numeroTarjetaEditText.setText(tarjetaExistente.numeroTarjeta)
            dniTitularEditText.setText(tarjetaExistente.dni)

            // Mostrar el botón de actualizar y ocultar el de registrar
            actualizarButton.visibility = View.VISIBLE
            registrarButton.visibility = View.GONE

            // Configurar acción del botón de actualizar
            actualizarButton.setOnClickListener {
                val nuevoNumeroTarjeta = numeroTarjetaEditText.text.toString()
                actualizarTarjeta(tarjetaExistente.id, nuevoNumeroTarjeta)
            }

        } else {
            // Si no existe la tarjeta, permitir registrar una nueva
            registrarButton.setOnClickListener {
                val numeroTarjeta = numeroTarjetaEditText.text.toString()
                val dniTitular = dniTitularEditText.text.toString()

                if (numeroTarjeta.isNotEmpty() && dniTitular.isNotEmpty()) {
                    registrarTarjeta(dniTitular, numeroTarjeta)
                } else {
                    Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Actualizar imagen de la tarjeta al escribir el número
        numeroTarjetaEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tarjetaImageView.text = s.toString() // Mostrar el número en la imagen simulada
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Función para registrar una nueva tarjeta
    private fun registrarTarjeta(dni: String, numeroTarjeta: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_DNI, dni)
            put(COL_NUMERO_TARJETA, numeroTarjeta)
            put(COL_MONTO, 0.0) // Iniciar con saldo cero
        }

        val newRowId = db.insert(TABLE_TARJETA, null, values)

        if (newRowId != -1L) {
            Toast.makeText(this, "Tarjeta registrada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al registrar la tarjeta", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    // Función para actualizar la tarjeta
    private fun actualizarTarjeta(id: Int, nuevoNumeroTarjeta: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COL_NUMERO_TARJETA, nuevoNumeroTarjeta)
        }

        val rowsAffected = db.update(TABLE_TARJETA, values, "$COL_TARJETA_ID = ?", arrayOf(id.toString()))

        if (rowsAffected > 0) {
            Toast.makeText(this, "Tarjeta actualizada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al actualizar la tarjeta", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.manu_deplegable, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_estaciones -> {
                val intent = Intent(this, estacionesActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_deposito -> {
                val intent = Intent(this, depositarActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_saldo -> {
                val intent = Intent(this, saldoActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_perfil -> {
                val intent = Intent(this, perfilActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item_tarjeta -> {
                val intent = Intent(this, tarjetaActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}