package com.example.atu_app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class saldoActivity : AppCompatActivity() {

    companion object {
        var montoActual: Double = 50.0
        private val movimientos = mutableListOf<movimiento>()
        private var movimientosIniciados = false
    }

    private lateinit var montoTextView: TextView
    private lateinit var movimientosListView: ListView
    private lateinit var adapter: ArrayAdapter<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saldo)

        // Inicializar vistas
        montoTextView = findViewById(R.id.montoTextView)
        movimientosListView = findViewById(R.id.movimientosListView)

        // Verificar si ya se agregaron los movimientos iniciales
        if (!movimientosIniciados) {
            agregarMovimientosIniciales()
            movimientosIniciados = true // Marcar que ya se agregaron los movimientos
        }

        // Actualizar la lista de movimientos
        actualizarListaMovimientos()
    }

    override fun onResume() {
        super.onResume()

        // Obtener datos de la transacción desde el Intent
        val monto = intent.getDoubleExtra("monto", 0.0)
        val metodo = intent.getStringExtra("metodo")

        // Si hay un monto y una tarjeta, agregar el movimiento y aumentar el monto actual
        if (monto != 0.0 && metodo != null) {
            montoActual += monto
            agregarMovimiento("Ingreso", monto, "Depósito con $metodo")
            actualizarListaMovimientos()
        }
    }

    // Función para agregar un movimiento a la lista
    private fun agregarMovimiento(tipo: String, cantidad: Double, descripcion: String) {
        val nuevoMovimiento = movimiento(tipo, cantidad, descripcion)
        movimientos.add(nuevoMovimiento)
        if (movimientos.size > 10) {
            movimientos.removeAt(0) // Limitar a los últimos 10 movimientos
        }
    }

    // Función para actualizar la lista de movimientos en la interfaz
    private fun actualizarListaMovimientos() {
        val movimientosString = movimientos.map { movimiento ->
            "${movimiento.tipo}: ${movimiento.monto} - ${movimiento.descripcion}"
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movimientosString)
        movimientosListView.adapter = adapter

        // Actualizar el monto actual en la interfaz
        montoTextView.text = "Monto actual: $montoActual"
    }

    // Función para agregar los 5 movimientos iniciales
    private fun agregarMovimientosIniciales() {
            movimientos.add(movimiento("Ingreso", 20.0, "Depóito plin"))
        movimientos.add(movimiento("Ingreso", 100.0, "Depósito yape"))
        movimientos.add(movimiento("Ingreso", 10.0, "Depóito plin"))
        movimientos.add(movimiento("Ingreso", 30.0, "Depóito plin"))
        movimientos.add(movimiento("Ingreso", 50.0, "Depóito plin"))
        movimientos.add(movimiento("Ingreso", 100.0, "Depósito yape"))
        movimientos.add(movimiento("Ingreso", 10.0, "Depóito plin"))
        movimientos.add(movimiento("Ingreso", 30.0, "Depóito yape"))
        movimientos.add(movimiento("Ingreso", 50.0, "Depóito yape"))
    }
}