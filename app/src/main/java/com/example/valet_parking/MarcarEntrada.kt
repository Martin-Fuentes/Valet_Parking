package com.example.valet_parking

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.valet_parking.Classes.ConductorVehiculo
import com.example.valet_parking.Classes.DatabaseHelper
import kotlinx.coroutines.launch

class MarcarEntrada : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcar_entrada)
        val btnMarcarEntrada = findViewById<Button>(R.id.btnMarcarEntrada)
        btnMarcarEntrada.setOnClickListener {
            lifecycleScope.launch {
                val txtCedula = findViewById<TextView>(R.id.txtBuscarCedula).text.toString()
                val txtPlaca = findViewById<TextView>(R.id.txtBuscarPlaca).text.toString()
                val dbHelper = DatabaseHelper(this@MarcarEntrada)
                dbHelper.copyDB()
                val conductorVehiculo = ConductorVehiculo(dbHelper)

                if (conductorVehiculo.existeConductorVehiculoRegistrados(txtCedula, txtPlaca)) {
                    conductorVehiculo.insertarParkingEntrada(txtCedula,txtPlaca)
                    Toast.makeText(this@MarcarEntrada, "Entrada marcada", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MarcarEntrada, "No se encuentra el conductor o el vehículo", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}