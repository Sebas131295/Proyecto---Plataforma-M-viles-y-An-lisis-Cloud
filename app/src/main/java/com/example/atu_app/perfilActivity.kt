package com.example.atu_app

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class perfilActivity : AppCompatActivity() {
    private lateinit var nombreTextView: TextView
    private lateinit var apellidoTextView: TextView
    private lateinit var dniTextView: TextView
    private lateinit var telefonoTextView: TextView
    private lateinit var correoTextView: TextView
    private lateinit var contrasenaTextView: TextView
    private lateinit var revelarButton: Button

    private var contrasenaVisible = false
    private val usuario = Usuario("Juan", "Pérez", "12345678", "987654321", "juan.perez@mail.com", "mi_contraseña")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar vistas
        nombreTextView = findViewById(R.id.nombreTextView)
        apellidoTextView = findViewById(R.id.apellidoTextView)
        dniTextView = findViewById(R.id.dniTextView)
        telefonoTextView = findViewById(R.id.telefonoTextView)
        correoTextView = findViewById(R.id.correoTextView)
        contrasenaTextView = findViewById(R.id.contrasenaTextView)
        revelarButton = findViewById(R.id.revelarButton)

        // Configurar datos del usuario
        nombreTextView.text = "Nombre: ${usuario.nombre}"
        apellidoTextView.text = "Apellido: ${usuario.apellido}"
        dniTextView.text = "DNI: ${usuario.dni}"
        telefonoTextView.text = "Teléfono: ${usuario.telefono}"
        correoTextView.text = "Correo: ${usuario.correo}"

        // Mostrar la contraseña ofuscada
        contrasenaTextView.text = usuario.contrasena
        contrasenaTextView.transformationMethod = PasswordTransformationMethod.getInstance()

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
    }
}

// Modelo Usuario
data class Usuario(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)