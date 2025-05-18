package com.example.valet_parking

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.valet_parking.Classes.ConductorVehiculo
import com.example.valet_parking.Classes.DatabaseHelper
import kotlinx.coroutines.launch
import java.util.Locale

class SaldoPagar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saldo_pagar)
        val txtTiempo = findViewById<TextView>(R.id.txtTiempo)
        val txtSaldo = findViewById<TextView>(R.id.txtSaldoPagar)
        val dbHelper = DatabaseHelper(this)
        dbHelper.copyDB()
        lifecycleScope.launch {
            val conductorVehiculo = ConductorVehiculo(dbHelper)
            val resultado = conductorVehiculo.obtenerDuracionYSaldoPorPlaca(intent.getStringExtra("Placa").toString())
            resultado?.let{
                val horas = it[0] as Int
                val minutos = it[1] as Int
                val costo = it[2] as Double
                var strHorasYMinutos = "Tiempo transcurrido: "

                if (horas == 1) {
                    strHorasYMinutos += "1 hora"
                } else if (horas > 1) {
                    strHorasYMinutos += "$horas horas"
                }

                if (minutos == 1) {
                    strHorasYMinutos += if (horas > 0) " y 1 minuto" else "1 minuto"
                } else if (minutos > 1) {
                    strHorasYMinutos += if (horas > 0) " y $minutos minutos" else "$minutos minutos"
                }

                if (horas == 0 && minutos == 0) {
                    strHorasYMinutos += "Menos de un minuto"
                }
                txtTiempo.text = strHorasYMinutos
                txtSaldo.text = "B/. ${String.format(Locale.US, "%.2f", costo)}"

            }
        }
    }
}