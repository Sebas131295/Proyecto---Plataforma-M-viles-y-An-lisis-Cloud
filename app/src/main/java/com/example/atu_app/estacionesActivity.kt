package com.example.atu_app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout

class estacionesActivity : AppCompatActivity() {

    private lateinit var estacionesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estaciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar TabLayout con 6 opciones
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_bus))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_check))
        tabLayout.addTab(tabLayout.newTab().setText("Opción 3"))
        tabLayout.addTab(tabLayout.newTab().setText("Opción 4"))
        tabLayout.addTab(tabLayout.newTab().setText("Opción 5"))
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

        // Crear una lista de estaciones
        val estaciones = listOf(
            estacion("Estación Central", "Lima", "22:00"),
            estacion("Estación Norte", "San Martín de Porres", "23:00"),
            estacion("Estación Sur", "Surco", "21:00"),
            estacion("Estación Este", "Ate", "20:30"),
            estacion("Estación Oeste", "Magdalena", "22:30")
        )

        // Mapear las estaciones a un formato de string para mostrar en el ListView
        val estacionesString = estaciones.map { estacion ->
            "${estacion.nombre} - ${estacion.distrito} (Cierra a las ${estacion.horarioCierre})"
        }

        // Crear un adaptador para la lista
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, estacionesString)

        // Asignar el adaptador a la lista
        estacionesListView.adapter = adapter
    }
}