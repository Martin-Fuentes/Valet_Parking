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
import com.example.valet_parking.DataClasses.Data_ConductorVehiculo
import kotlinx.coroutines.launch

class MarcarSalida : AppCompatActivity() {
    private lateinit var adapter: Adaptador_Listview
    private var itemSeleccionado: Data_ConductorVehiculo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcar_salida)

        val dbHelper = DatabaseHelper(this)
        dbHelper.copyDB()

        val listView = findViewById<ListView>(R.id.listView)
        val searchInput = findViewById<TextView>(R.id.txtCedulaConductor)

        lifecycleScope.launch {

            val conductorVehiculo = ConductorVehiculo(dbHelper)
            val datos = conductorVehiculo.obtenerConductorVehiculo()

            adapter = Adaptador_Listview(this@MarcarSalida, datos)
            listView.adapter = adapter
            listView.setOnItemClickListener { _, _, position, _ ->
                val item = adapter.getItem(position)
                adapter.mostrarSolo(item)
                itemSeleccionado = item
                searchInput.text = item.cedulaConductor.toString()
            }

            searchInput.addTextChangedListener {
                adapter.filter.filter(it.toString())
            }
        }
        val btnMarcarSalida = findViewById<Button>(R.id.btnMarcarSalida)
        btnMarcarSalida.setOnClickListener{
            val item = itemSeleccionado

            if (item != null) {
                lifecycleScope.launch {
                    val conductorVehiculo = ConductorVehiculo(dbHelper)
                    val exito = conductorVehiculo.registrarSalidaPorPlaca(item.placaVehiculo)

                    if (exito) {
                        Toast.makeText(this@MarcarSalida, "Salida registrada correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MarcarSalida, SaldoPagar::class.java)
                        intent.putExtra("Placa", item.placaVehiculo.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MarcarSalida, "No se pudo registrar la salida", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Selecciona un item primero", Toast.LENGTH_SHORT).show()
            }
        }
    }
}