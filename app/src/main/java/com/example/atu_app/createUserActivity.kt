package com.example.atu_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class createUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val crearUsuarioButton = findViewById<Button>(R.id.createButton)
        val dniText = findViewById<EditText>(R.id.dniEditText)
        val nombreText = findViewById<EditText>(R.id.nombreEditText)
        val apellidoText = findViewById<EditText>(R.id.apellidoEditText)
        val correoText = findViewById<EditText>(R.id.correoEditText)
        val telefonoText = findViewById<EditText>(R.id.telefonoEditText)
        val contrasenaText = findViewById<EditText>(R.id.contrasenaEditText)
        val backToLoginButton = findViewById<Button>(R.id.backToLoginButton)

        crearUsuarioButton.setOnClickListener {
            val dni = dniText.text.toString()
            val nombre = nombreText.text.toString()
            val apellido = apellidoText.text.toString()
            val correo = correoText.text.toString()
            val telefono = telefonoText.text.toString()
            val contrasena = contrasenaText.text.toString()


            val dbHelper = databaseHelper(this)
            val usuarioId = dbHelper.insertarUsuario(dni, nombre, apellido, telefono, correo, contrasena)

            if (usuarioId != -1L) {
                Toast.makeText(this, "El usuario $nombre fue creado exitosamente", Toast.LENGTH_SHORT).show()
                // Puedes limpiar los campos del formulario o navegar a otra pantalla
            } else {
                Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
            }
        }

        // Acción para el botón "Volver al login"
        backToLoginButton.setOnClickListener {
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
        }
    }
}