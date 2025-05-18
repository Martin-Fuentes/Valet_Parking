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
        val btnBuscar = findViewById<Button>(R.id.btnBuscarConductorVehiculo)
        val txtConductor = findViewById<TextView>(R.id.txtConductor_Marcar)
        val txtVehiculo = findViewById<TextView>(R.id.txtVehiculo_Marcar)
        btnBuscar.setOnClickListener {
            lifecycleScope.launch {
                val txtCedula = findViewById<TextView>(R.id.txtBuscarCedula).text.toString()
                val txtPlaca = findViewById<TextView>(R.id.txtBuscarPlaca).text.toString()
                val dbHelper = DatabaseHelper(this@MarcarEntrada)
                dbHelper.copyDB()
                val conductorVehiculo = ConductorVehiculo(dbHelper)
                val data = conductorVehiculo.existeConductorVehiculoRegistrados(txtCedula, txtPlaca)

                if (data != null) {

                    val nombre = data[0]
                    val cedula = data[1]
                    val placa = data[2]
                    val marca = data[3]
                    val modelo = data[4]
                    val color = data[5]
                    val tipo = data[6]

                    txtConductor.text = "${nombre} | ${cedula}"
                    txtVehiculo.text ="${placa} | ${marca} | ${modelo} | ${color} | ${tipo}"
                    Toast.makeText(this@MarcarEntrada, "Encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    txtConductor.text = ""
                    txtVehiculo.text =""
                    Toast.makeText(
                        this@MarcarEntrada,
                        "No se encuentra el conductor o el vehículo",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        btnMarcarEntrada.setOnClickListener {
            lifecycleScope.launch {
                val txtCedula = findViewById<TextView>(R.id.txtBuscarCedula).text.toString()
                val txtPlaca = findViewById<TextView>(R.id.txtBuscarPlaca).text.toString()
                val dbHelper = DatabaseHelper(this@MarcarEntrada)
                dbHelper.copyDB()
                val conductorVehiculo = ConductorVehiculo(dbHelper)
                val data=conductorVehiculo.existeConductorVehiculoRegistrados(txtCedula, txtPlaca)
                if (data!=null) {
                    conductorVehiculo.insertarParkingEntrada(txtCedula,txtPlaca)
                    Toast.makeText(this@MarcarEntrada, "Entrada marcada", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MarcarEntrada, "No se encuentra el conductor o el vehículo", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}