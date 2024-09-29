package com.example.atu_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class confirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        // Retrasar el regreso a HomeActivity después de 5 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val monto = intent.getDoubleExtra("monto", 0.0)
            val tarjeta = intent.getStringExtra("tarjeta")

            // Volver a HomeActivity con los datos del depósito
            val intentHome = Intent(this, saldoActivity::class.java)
            intentHome.putExtra("monto", monto)
            intentHome.putExtra("tarjeta", tarjeta)
            startActivity(intentHome)
            finish()
        }, 5000) // 5 segundos
    }
}