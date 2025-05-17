package com.example.valet_parking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.valet_parking.Classes.Adaptador_Listview
import com.example.valet_parking.Classes.ConductorVehiculo
import com.example.valet_parking.Classes.DatabaseHelper
import kotlinx.coroutines.launch

class MarcarSalida : AppCompatActivity() {
    private lateinit var adapter: Adaptador_Listview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcar_salida)

        val dbHelper = DatabaseHelper(this)
        dbHelper.copyDB()

        val listView = findViewById<ListView>(R.id.listView)
        val searchInput = findViewById<TextView>(R.id.txtCedulaConductor)

        lifecycleScope.launch {

            val conductorVehiculo = ConductorVehiculo(dbHelper)
            Toast.makeText(this@MarcarSalida, "Llegue aqui", Toast.LENGTH_SHORT).show()
            val datos = conductorVehiculo.obtenerConductorVehiculo()

            adapter = Adaptador_Listview(this@MarcarSalida, datos)
            listView.adapter = adapter
            listView.setOnItemClickListener { _, _, position, _ ->
                val item = adapter.getItem(position)
                adapter.mostrarSolo(item)
            }

            searchInput.addTextChangedListener {
                adapter.filter.filter(it.toString())
            }
        }
        val btnClick = findViewById<Button>(R.id.btnClick)
        btnClick.setOnClickListener{
            val intent = Intent(this, MarcarEntrada::class.java)
            startActivity(intent)
        }
    }
}