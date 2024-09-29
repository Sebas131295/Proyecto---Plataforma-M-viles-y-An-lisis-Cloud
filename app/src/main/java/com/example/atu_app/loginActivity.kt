package com.example.atu_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity() {

    private val correctUsername = "user"
    private val correctPassword = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val passButton = findViewById<Button>(R.id.passButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username == correctUsername && password == correctPassword) {
                val intent = Intent(this, estacionesActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        passButton.setOnClickListener {
            val intent = Intent(this, passActivity::class.java)
            startActivity(intent)
        }
    }
}