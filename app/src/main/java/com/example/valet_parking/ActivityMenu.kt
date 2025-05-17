package com.example.valet_parking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ActivityMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val btnRegistro_Conductor = findViewById<Button>(R.id.btnRegistrarConductor_Menu)
        val btnRegistro_Vehiculo = findViewById<Button>(R.id.btnRegistrarVehiculo_Menu)
        val btnMarcarEntrada = findViewById<Button>(R.id.btnMarcarEntrada_Menu)
        val btnMarcarSalida = findViewById<Button>(R.id.btn_MarcarSalida_Menu)
        btnRegistro_Conductor.setOnClickListener {
            val intent = Intent(this, Registro_Conductor::class.java)
            startActivity(intent)
        }
        btnRegistro_Vehiculo.setOnClickListener {
            val intent = Intent(this, Registro_Vehiculo::class.java)
            startActivity(intent)
        }
        btnMarcarEntrada.setOnClickListener {
            val intent = Intent(this, MarcarEntrada::class.java)
            startActivity(intent)
        }
        btnMarcarSalida.setOnClickListener {
            val intent = Intent(this, MarcarSalida::class.java)
            startActivity(intent)
        }
    }
}