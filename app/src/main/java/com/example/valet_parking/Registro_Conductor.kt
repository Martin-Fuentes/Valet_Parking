package com.example.valet_parking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.valet_parking.Classes.Conductor
import com.example.valet_parking.Classes.DatabaseHelper
import com.example.valet_parking.DataClasses.Data_Conductor
import kotlinx.coroutines.launch

class Registro_Conductor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_conductor)
        val btnRegistrarConductor = findViewById<Button>(R.id.btnRegistrarConductor)
        btnRegistrarConductor.setOnClickListener{
            agregarConductor()
        }
    }
    private fun agregarConductor(){
        val nombre = findViewById<TextView>(R.id.txtNombre)
        val cedula = findViewById<TextView>(R.id.txtCedula)
        val contacto = findViewById<TextView>(R.id.txtContacto)
        val dbHelper = DatabaseHelper(this)
        dbHelper.copyDB()
        val con = Conductor(dbHelper)
        lifecycleScope.launch {
            val newConductor = Data_Conductor(nombre = nombre.text.toString(), cedula = cedula.text.toString(), contacto = contacto.text.toString())
            val exito = con.agregarConductor(newConductor)
            if (exito){
                Toast.makeText(this@Registro_Conductor, "Guardado", Toast.LENGTH_SHORT).show()
            }
        }
        val intent = Intent(this, MarcarSalida::class.java)
        startActivity(intent)
    }
}