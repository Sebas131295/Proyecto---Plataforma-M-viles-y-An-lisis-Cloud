package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

class estacionesActivity : AppCompatActivity() {

    private lateinit var databaseHelper: databaseHelper
    private lateinit var listViewEstaciones: ListView
    private lateinit var editTextBuscarEstacion: EditText
    private lateinit var buttonBuscarEstacion: Button
    private lateinit var estacionesAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estaciones)

        // Inicializar el DatabaseHelper
        databaseHelper = databaseHelper(this)

        listViewEstaciones = findViewById(R.id.listViewEstaciones)
        editTextBuscarEstacion = findViewById(R.id.editTextBuscarEstacion)
        buttonBuscarEstacion = findViewById(R.id.buttonBuscarEstacion)

        // Mostrar todas las estaciones al iniciar la actividad
        mostrarEstaciones()

        // Configurar el botón de búsqueda
        buttonBuscarEstacion.setOnClickListener {
            buscarEstaciones(editTextBuscarEstacion.text.toString())
        }

        // Inicializar TabLayout con 6 opciones
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_bus))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_check))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_card))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_list))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.user))

        // Configurar acciones de los tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // Mantenerse en HomeActivity
                        startActivity(Intent(this@estacionesActivity, estacionesActivity::class.java))
                    }
                    1 -> {
                        // Ir a DepositarActivity
                        startActivity(Intent(this@estacionesActivity, depositarActivity::class.java))
                    }
                    2 -> {
                        // Ir a DepositarActivity
                        startActivity(Intent(this@estacionesActivity, tarjetaActivity::class.java))
                    }
                    3 -> {
                        // Ir a DepositarActivity
                        startActivity(Intent(this@estacionesActivity, saldoActivity::class.java))
                    }
                    4 -> {
                        // Ir a DepositarActivity
                        startActivity(Intent(this@estacionesActivity, perfilActivity::class.java))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun mostrarEstaciones() {
        val estaciones = databaseHelper.leerEstaciones()
        val listaEstaciones = estaciones.map { "${it.nombre} - ${it.direccion} - ${it.distrito}" }

        estacionesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaEstaciones)
        listViewEstaciones.adapter = estacionesAdapter
    }

    private fun buscarEstaciones(nombre: String) {
        val estaciones = databaseHelper.buscarEstacionesPorNombre(nombre)
        val listaEstaciones = estaciones.map { "${it.nombre} - ${it.direccion} - ${it.distrito}" }

        estacionesAdapter.clear()
        estacionesAdapter.addAll(listaEstaciones)
        estacionesAdapter.notifyDataSetChanged()
    }
}