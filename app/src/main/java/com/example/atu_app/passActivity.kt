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

class passActivity : AppCompatActivity() {

    private val correctPassword = "1234" // La contraseña que se va a "enviar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val backToLoginButton = findViewById<Button>(R.id.backToLoginButton)

        sendButton.setOnClickListener {
            val email = emailInput.text.toString()

            if (email.isNotEmpty()) {
                Toast.makeText(this, "Correo enviado a $email. Su nueva contraseña es: $correctPassword", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Por favor, ingrese un correo válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Al hacer clic en el botón de volver al login
        backToLoginButton.setOnClickListener {
            val intent = Intent(this, loginActivity::class.java)
            startActivity(intent)
        }
    }
}