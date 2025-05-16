package com.example.valet_parking

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.valet_parking.Classes.DatabaseHelper
import com.example.valet_parking.Classes.Vehiculo
import com.example.valet_parking.DataClasses.Data_Vehiculo
import kotlinx.coroutines.launch


class Registro_Vehiculo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_vehiculo)
        setupSpinnerBasic()
        val btnRegistrarVehiculo = findViewById<Button>(R.id.btnRegistrarVehiculo)
        btnRegistrarVehiculo.setOnClickListener{
            agregarVehiculo()
        }

    }
    private fun setupSpinnerBasic() {
        val spinner = findViewById<Spinner>(R.id.spTipo)
        val adaptador = ArrayAdapter.createFromResource(
            this,
            R.array.str_spTipo,
            android.R.layout.simple_spinner_item
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }
    private fun agregarVehiculo(){
        val placa = findViewById<TextView>(R.id.txtPlaca)
        val marca = findViewById<TextView>(R.id.txtMarca)
        val modelo = findViewById<TextView>(R.id.txtModelo)
        val color = findViewById<TextView>(R.id.txtColor)
        val tipo = findViewById<Spinner>(R.id.spTipo)
        val dbHelper = DatabaseHelper(this)
        dbHelper.copyDB()
        val con = Vehiculo(dbHelper)
        lifecycleScope.launch {
            val newVehiculo = Data_Vehiculo(placa = placa.text.toString(), marca = marca.text.toString(), modelo = modelo.text.toString(),color=color.text.toString(),tipo=tipo.selectedItem.toString())
            val exito = con.agregarVehiculo(newVehiculo)
            if (exito){
                Toast.makeText(this@Registro_Vehiculo, "Guardado", Toast.LENGTH_SHORT).show()
            }
        }
        val intent = Intent(this, Registro_Conductor::class.java)
        startActivity(intent)
    }
}