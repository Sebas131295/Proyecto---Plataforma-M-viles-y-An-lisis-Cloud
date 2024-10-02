package com.example.atu_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class depositarActivity : AppCompatActivity() {
    private lateinit var montoEditText: EditText
    private lateinit var radioGroupMetodo: RadioGroup
    private lateinit var depositarButton: Button
    private lateinit var dbHelper: databaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depositar)

        montoEditText = findViewById(R.id.ingresoInput)
        radioGroupMetodo = findViewById(R.id.metodoRadio)
        depositarButton = findViewById(R.id.depositarButton)
        dbHelper = databaseHelper(this)  // Inicializar el helper de la base de datos

        // Configurar botón de depósito
        depositarButton.setOnClickListener {
            // Obtener el DNI del usuario actual desde SharedPreferences
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val dniUsuario = sharedPreferences.getString("dniUsuario", null)

            // Verificar si hay una tarjeta registrada para el usuario
            if (dniUsuario != null && !dbHelper.existeTarjeta(dniUsuario)) {
                // Si no hay tarjeta registrada, mostrar un mensaje
                Toast.makeText(this, "Debe registrar una tarjeta antes de hacer un depósito.", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Salir del bloque de código sin continuar
            }

            // Procesar el depósito solo si se ha verificado la tarjeta
            val monto = montoEditText.text.toString().toDoubleOrNull()

            // Obtener el método de pago seleccionado
            val metodoSeleccionadoId = radioGroupMetodo.checkedRadioButtonId
            val metodoSeleccionado = when (metodoSeleccionadoId) {
                R.id.yapeRadio -> "Yape"
                R.id.plinRadio -> "Plin"
                R.id.cardRadio -> "Tarjeta de Débito/Crédito"
                else -> null
            }

            val calendar = Calendar.getInstance()
            val currentDate = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(calendar.time)

            if (monto != null && metodoSeleccionado != null) {
                // Ir a ConfirmationActivity
                val intent = Intent(this, confirmationActivity::class.java)
                intent.putExtra("monto", monto)
                intent.putExtra("fechaActual", currentDate)
                intent.putExtra("metodo", metodoSeleccionado)
                startActivity(intent)
            }
        }
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