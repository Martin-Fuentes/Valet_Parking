package com.example.valet_parking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnClick = findViewById<Button>(R.id.btnClick)
        btnClick.setOnClickListener{
            val intent = Intent(this, Registro_Vehiculo::class.java)
            startActivity(intent)
        }
    }
}