package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class perfilActivity : AppCompatActivity() {
    private lateinit var dbHelper: databaseHelper
    private lateinit var nombreTextView: TextView
    private lateinit var apellidoTextView: TextView
    private lateinit var telefonoTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var dniTextView: TextView
    private lateinit var contrasenaTextView: TextView
    private lateinit var revelarButton: Button
    private lateinit var numeroTarjetaTextView: TextView
    private var contrasenaVisible = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        nombreTextView = findViewById(R.id.nombreTextView)
        apellidoTextView = findViewById(R.id.apellidoTextView)
        telefonoTextView = findViewById(R.id.telefonoTextView)
        correoTextView = findViewById(R.id.correoTextView)
        dniTextView = findViewById(R.id.dniTextView)
        contrasenaTextView = findViewById(R.id.contrasenaTextView)
        revelarButton = findViewById(R.id.revelarButton)
        numeroTarjetaTextView = findViewById(R.id.numeroTarjetaTextView)


        // Obtener el DNI del usuario del contexto
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val dniUsuario = sharedPreferences.getString("dniUsuario", null)

        if (dniUsuario != null) {
            mostrarDatosUsuario(dniUsuario)
        } else {
            Toast.makeText(this, "No se encontró el DNI del usuario", Toast.LENGTH_SHORT).show()
        }


        // Configurar el botón de revelar
        revelarButton.setOnClickListener {
            contrasenaVisible = !contrasenaVisible
            if (contrasenaVisible) {
                contrasenaTextView.transformationMethod = HideReturnsTransformationMethod.getInstance()
                revelarButton.text = "Ocultar"
            } else {
                contrasenaTextView.transformationMethod = PasswordTransformationMethod.getInstance()
                revelarButton.text = "Revelar"
            }
        }

        // Verificar si el usuario tiene tarjeta registrada
        if (dniUsuario != null) {
            val tarjeta = dbHelper.obtenerTarjetaPorDni(dniUsuario)

            if (tarjeta != null) {
                numeroTarjetaTextView.text = "Número de tarjeta: ${tarjeta.numeroTarjeta}"
            } else {
                numeroTarjetaTextView.text = "Tarjeta no registrada"
            }
        } else {
            numeroTarjetaTextView.text = "DNI no disponible"
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


    @SuppressLint("SetTextI18n")
    private fun mostrarDatosUsuario(dni: String) {
        dbHelper = databaseHelper(this)

        val usuario = dbHelper.obtenerUsuarioPorDni(dni)

        if (usuario != null) {
            nombreTextView.text = "Nombre: ${usuario.nombre}"
            apellidoTextView.text = "Apellido: ${usuario.apellido}"
            telefonoTextView.text = "Telefono: ${usuario.telefono}"
            correoTextView.text = "Correo: ${usuario.correo}"
            dniTextView.text = "Dni: ${usuario.dni}"
            contrasenaTextView.text = usuario.contrasena
            contrasenaTextView.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            Toast.makeText(this, "No se encontraron datos para el usuario con DNI $dni", Toast.LENGTH_SHORT).show()
        }
    }
}