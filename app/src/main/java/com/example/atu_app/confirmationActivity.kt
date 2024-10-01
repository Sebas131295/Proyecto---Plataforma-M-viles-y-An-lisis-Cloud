package com.example.atu_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class confirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val monto = intent.getDoubleExtra("monto", 0.0)
        val metodoSeleccionado = intent.getStringExtra("metodo")

        if (monto != null && metodoSeleccionado != null) {
            // Pasar datos a SaldoActivity
            Handler().postDelayed({
                val intent = Intent(this, saldoActivity::class.java)
                intent.putExtra("monto", monto)
                intent.putExtra("metodo", metodoSeleccionado)
                startActivity(intent)
                finish() // Finaliza ConfirmationActivity
            }, 3000) // Espera 3 segundos
        }
    }
}