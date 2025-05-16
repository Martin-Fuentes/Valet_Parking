package com.example.valet_parking.Classes

import android.content.ContentValues
import com.example.valet_parking.DataClasses.Data_Vehiculo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Vehiculo(private val dbHelper: DatabaseHelper) {
    suspend fun agregarVehiculo(vehiculo: Data_Vehiculo): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()
        val values = ContentValues().apply {
            put("placa", vehiculo.placa)
            put("marca", vehiculo.marca)
            put("modelo", vehiculo.modelo)
            put("color", vehiculo.color)
            put("tipo", vehiculo.tipo)
        }
        val result = db.insert("vehiculo", null, values)
        db.close()
        return@withContext result != -1L
    }
}