package com.example.atu_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estaciones)

        // Inicializar el DatabaseHelper
        databaseHelper = databaseHelper(this)

        listViewEstaciones = findViewById(R.id.listViewEstaciones)
        editTextBuscarEstacion = findViewById(R.id.editTextBuscarEstacion)
        buttonBuscarEstacion = findViewById(R.id.buttonBuscarEstacion)

        // Agregar estaciones iniciales si no se han insertado aún
        agregarEstacionesIniciales()

        // Mostrar todas las estaciones al iniciar la actividad
        mostrarEstaciones()

        // Configurar el botón de búsqueda
        buttonBuscarEstacion.setOnClickListener {
            buscarEstaciones(editTextBuscarEstacion.text.toString())
        }

        // Inicializar TabLayout con opciones
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_bus))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_check))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_card))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_list))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_user))

        // Configurar acciones de los tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> startActivity(Intent(this@estacionesActivity, estacionesActivity::class.java))
                    1 -> startActivity(Intent(this@estacionesActivity, depositarActivity::class.java))
                    2 -> startActivity(Intent(this@estacionesActivity, tarjetaActivity::class.java))
                    3 -> startActivity(Intent(this@estacionesActivity, saldoActivity::class.java))
                    4 -> startActivity(Intent(this@estacionesActivity, perfilActivity::class.java))
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

    private fun agregarEstacionesIniciales() {

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val estacionesIniciadas = sharedPreferences.getBoolean("estacionesIniciadas", false)

        // Verificar si ya se agregaron las estaciones
        if (!estacionesIniciadas) {
            databaseHelper.insertarEstacion("Estación Los Incas", "Av. Universitaria 9265", "Comas")
            databaseHelper.insertarEstacion("Estación 22 de Agosto", "Av. Universitaria 6481", "Comas")
            databaseHelper.insertarEstacion("Estación Tomás Valle", "Av. Túpac Amaru con Avenida Tomás Valle", "Independencia")
            databaseHelper.insertarEstacion("Estación UNI", "Av. Túpac Amaru con Av. Eduardo de Habich", "Rimac")
            databaseHelper.insertarEstacion("Estación Naranjal", "Av. Túpac Amaru con Av. Alisos", "Independencia")
            databaseHelper.insertarEstacion("Estación Pacífico", "Av. Túpac Amaru con Av. El Pacífico", "Independencia")
            databaseHelper.insertarEstacion("Estación España", "Av. Alfonso Ugarte con Avenida España", "Breña")
            databaseHelper.insertarEstacion("Estación Dos de Mayo", "Av. Alfonso Ugarte con Plaza Dos de Mayo", "Cercado de Lima")
            databaseHelper.insertarEstacion("Estación Central", "Paseo de los Héroes Navales entre la Plaza Grau y Av. Bolivia/Roosevelt", "Cercado de Lima")
            databaseHelper.insertarEstacion("Estación Benavides", "Vía Expresa Paseo de la República con Av. Benavides", "Miraflores")
            databaseHelper.insertarEstacion("Estación 28 de Julio", "Vía Expresa Paseo de la República con Av. 28 de Julio", "Miraflores")
            databaseHelper.insertarEstacion("Estación México", "Av. México", "La Victoria")
            databaseHelper.insertarEstacion("Estación Aramburú", "Vía Expresa Paseo de la República con Av. Aramburú", "San Isidro")
            databaseHelper.insertarEstacion("Estación Angamos", "Vía Expresa Paseo de la República con Av. Angamos Este", "Surquillo")
            databaseHelper.insertarEstacion("Estación Andrés Belaunde", "Av. Universitaria 7433", "Comas")
            databaseHelper.insertarEstacion("Estadio Nacional", "Vía Expresa Paseo de la República con Jr. Madre de Dios", "La Victoria")
            databaseHelper.insertarEstacion("Estación Andrés Reyes", "Vía Expresa Paseo de la República con Av. Andrés Reyes", "San Isidro")
            databaseHelper.insertarEstacion("Estación Jirón de la Unión", "Av. Emancipación y Jirón Cusco con Jirón de la Unión", "Cercado de Lima")

            // Marcar que las estaciones ya fueron agregadas
            editor.putBoolean("estacionesIniciadas", true)
            editor.apply()
        }
    }
}