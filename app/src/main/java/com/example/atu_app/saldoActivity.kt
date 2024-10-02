package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class saldoActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: databaseHelper
        var montoActual: Double = 0.00
        private val movimientos = mutableListOf<movimiento>()
    }

    private lateinit var montoTextView: TextView
    private lateinit var movimientosListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var movimientosIniciados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saldo)

        // Inicializar vistas
        montoTextView = findViewById(R.id.montoTextView)
        movimientosListView = findViewById(R.id.movimientosListView)

        // Inicializar el helper de la base de datos
        dbHelper = databaseHelper(this)

        // Obtener el DNI del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val dniUsuario = sharedPreferences.getString("dniUsuario", null)

        if (dniUsuario != null) {
            // Obtener movimientos solo para este DNI
            val movimientos = dbHelper.obtenerMovimientosPorDNI(dniUsuario)

            // Mostrar los movimientos en la lista
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movimientos)
            movimientosListView.adapter = adapter

            // Obtener y mostrar la suma total de montos para este usuario
            val sumaMontos = dbHelper.obtenerSumaMontosPorDNI(dniUsuario)
            montoTextView.text = "Monto total: $sumaMontos"
        } else {
            // Si el DNI no está disponible
            Toast.makeText(this, "DNI no disponible", Toast.LENGTH_SHORT).show()
        }

        // Verificar si ya se agregaron los movimientos iniciales
        if (!movimientosIniciados) {
            agregarMovimientoDeRelleno()
            movimientosIniciados = true // Marcar que ya se agregaron los movimientos
        }

        // Cargar los movimientos desde la base de datos
        cargarMovimientosDesdeBD()

        // Actualizar la lista de movimientos
        actualizarListaMovimientos()

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

    override fun onResume() {
        super.onResume()

        // Obtener datos de la transacción desde el Intent
        val monto = intent.getDoubleExtra("monto", 0.0)
        val fechaAct = intent.getStringExtra("fecha")
        val metodo = intent.getStringExtra("metodo")

        // Si hay un monto y un método, agregar el movimiento
        if (monto != 0.0 && metodo != null && fechaAct != null) {
            montoActual += monto // Solo sumar los nuevos montos
            agregarMovimiento("Ingreso", monto, "Depósito $metodo", fechaAct)
        }
    }

    private fun cargarMovimientosDesdeBD() {
        // Limpiar la lista de movimientos antes de cargar nuevos datos
        movimientos.clear()

        // Obtener el DNI del usuario actual desde SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val dniUsuario = sharedPreferences.getString("dniUsuario", null)

        // Si el DNI del usuario actual está disponible
        if (dniUsuario != null) {
            // Leer los movimientos del usuario actual y movimientos de relleno
            val cursor = dbHelper.leerMovimientosPorDNI(dniUsuario)

            if (cursor.moveToFirst()) {
                do {
                    val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad"))
                    val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                    val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))

                    // Agregar el movimiento a la lista local
                    movimientos.add(movimiento("Ingreso", monto, descripcion, fecha))

                } while (cursor.moveToNext())
            }
            cursor.close()

            // Agregar movimientos de relleno
            agregarMovimientoDeRelleno()

            // Limitar la lista a un máximo de 15 movimientos
            if (movimientos.size > 15) {
                val movimientosParaEliminar = movimientos.size - 15
                for (i in 0 until movimientosParaEliminar) {
                    movimientos.removeAt(0) // Eliminar el movimiento más antiguo
                }
            }
        } else {
            // Si el DNI no está disponible, mostrar mensaje de error
            Toast.makeText(this, "DNI no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    private fun agregarMovimiento(tipo: String, cantidad: Double, descripcion: String, fecha: String) {
        // Insertar en la base de datos
        dbHelper.insertarMovimiento(cantidad, descripcion, fecha)

        // Agregar el movimiento a la lista local
        val nuevoMovimiento = movimiento(tipo, cantidad, descripcion, fecha)
        movimientos.add(0, nuevoMovimiento)

        // Limitar la lista a 15 movimientos
        if (movimientos.size > 15) {
            // Eliminar el movimiento más antiguo
            movimientos.removeAt(movimientos.size - 1) // Eliminamos el último elemento, que es el más antiguo
        }

        // Actualizar la interfaz
        actualizarListaMovimientos()
    }

    // Función para actualizar la lista de movimientos en la interfaz
    @SuppressLint("SetTextI18n")
    private fun actualizarListaMovimientos() {
        val movimientosString = movimientos.map { movimiento ->
            "${movimiento.tipo}: ${movimiento.monto} - ${movimiento.descripcion} - ${movimiento.fecha}"
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, movimientosString)
        movimientosListView.adapter = adapter

        // Actualizar el monto actual en la interfaz
        montoTextView.text = "$montoActual"
    }

    private fun agregarMovimientoDeRelleno() {
        // Solo agregar movimientos si la lista está vacía
        if (movimientos.isEmpty()) {
            // Agregar movimientos a la base de datos solo si no existen
            if (dbHelper.contarMovimientos() == 0) {
                dbHelper.insertarMovimiento(20.0, "Depósito plin", "23/09/24 10:00")
                dbHelper.insertarMovimiento(12.0, "Depósito yape", "22/09/24 10:00")
                dbHelper.insertarMovimiento(10.0, "Depósito plin", "18/09/24 10:00")
                dbHelper.insertarMovimiento(30.0, "Depósito plin", "15/08/24 10:00")
                dbHelper.insertarMovimiento(15.0, "Depósito plin", "03/08/24 10:00")
                dbHelper.insertarMovimiento(10.0, "Depósito yape", "30/07/24 10:00")
                dbHelper.insertarMovimiento(17.0, "Depósito plin", "28/07/24 10:00")
                dbHelper.insertarMovimiento(25.0, "Depósito yape", "23/06/24 10:00")
                dbHelper.insertarMovimiento(50.0, "Depósito yape", "19/06/24 10:00")
            }
        }
    }
}