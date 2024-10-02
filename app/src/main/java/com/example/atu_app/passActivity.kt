package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class passActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)

        val dniInput = findViewById<EditText>(R.id.dniPassInput)
        val correctPassword = findViewById<EditText>(R.id.newPassInput) // Mueve la referencia de la contraseña aquí
        val sendButton = findViewById<Button>(R.id.newPassButton)
        val backToLoginButton = findViewById<Button>(R.id.backToLoginButton)
        val dbHelper = databaseHelper(this)

        sendButton.setOnClickListener {
            val dni = dniInput.text.toString()
            val newPassword = correctPassword.text.toString() // Obtener el valor de la nueva contraseña al hacer clic

            // Validar si el DNI y la contraseña no están vacíos
            if (dni.isNotEmpty() && newPassword.isNotEmpty()) {
                val filasActualizadas = dbHelper.actualizarUsuario(
                    dni,
                    newPassword // Actualizamos solo la contraseña
                )

                if (filasActualizadas > 0) {
                    Toast.makeText(
                        this,
                        "Contraseña restablecida para el usuario con DNI $dni. Su nueva contraseña es: $newPassword",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "No se encontró ningún usuario con ese DNI",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Mensaje de error si el DNI o la nueva contraseña están vacíos
                if (dni.isEmpty()) {
                    Toast.makeText(this, "Por favor, ingrese un DNI válido", Toast.LENGTH_SHORT).show()
                } else if (newPassword.isEmpty()) {
                    Toast.makeText(this, "Por favor, ingrese una nueva contraseña", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Acción para el botón "Volver al login"
        backToLoginButton.setOnClickListener {
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
        }
    }
}