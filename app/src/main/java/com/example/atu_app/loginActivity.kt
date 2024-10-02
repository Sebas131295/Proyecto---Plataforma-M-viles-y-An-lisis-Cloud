package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity() {

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dbHelper = databaseHelper(this)
        val usuarioId = dbHelper.insertarUsuario("12345678", "Joseph", "Coronel", "971455516","joseph@gmail.com", "1234")

        if (usuarioId != -1L) {
            // Usuario creado exitosamente
        } else {
            // Error al crear el usuario
        }

        val dniInput = findViewById<EditText>(R.id.dniInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val passButton = findViewById<Button>(R.id.passButton)
        val createButton = findViewById<Button>(R.id.createButton)

        loginButton.setOnClickListener {
            val dni = dniInput.text.toString()
            val password = passwordInput.text.toString()

            val cursor = dbHelper.readableDatabase.query(
                TABLE_USUARIO,
                null,
                "$COL_DNI = ?",
                arrayOf(dni),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                val contrasenaGuardada = cursor.getString(cursor.getColumnIndex(COL_CONTRASEÑA))
                if (password == contrasenaGuardada) {
                    // Iniciar sesión
                    val intent = Intent(this, estacionesActivity::class.java)
                    startActivity(intent)
                } else {
                    // Contraseña incorrecta
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Usuario no encontrado
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }

            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("dniUsuario", dni) // Guardar el DNI en SharedPreferences
            editor.apply()
        }

        passButton.setOnClickListener {
            val intent = Intent(this, passActivity::class.java)
            startActivity(intent)
        }

        createButton.setOnClickListener {
            val intent = Intent(this, createUserActivity::class.java)
            startActivity(intent)
        }
    }
}