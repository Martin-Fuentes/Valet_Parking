package com.example.valet_parking.Classes

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import com.example.valet_parking.DataClasses.Data_ConductorVehiculo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

class ConductorVehiculo(private val dbHelper: DatabaseHelper) {
    suspend fun obtenerConductorVehiculo(): List<Data_ConductorVehiculo> = withContext(Dispatchers.IO){
        val lista = mutableListOf<Data_ConductorVehiculo>()
        val db = dbHelper.openDatabase()
        val query = "SELECT c.cedula, c.nombre, c.contacto, v.placa, v.marca, v.modelo, v.color, v.tipo, p.hora_entrada, p.hora_salida, p.costo_pagar FROM parking p INNER JOIN conductor c ON p.cedula = c.cedula INNER JOIN vehiculo v ON p.placa = v.placa"
        val cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()){
            do{
                val item = Data_ConductorVehiculo(
                    cedulaConductor = cursor.getString(cursor.getColumnIndexOrThrow("cedula")),
                    nombreConductor = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    placaVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("placa")),
                    marcaVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                    modeloVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("modelo")),
                    colorVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("color")),
                    tipoVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                )
                lista.add(item)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return@withContext lista
    }
    suspend fun existeConductorVehiculo(cedula: String, placa: String): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()
        val query = "SELECT COUNT(*) as total FROM parking WHERE cedula = ? AND placa = ?"
        val cursor = db.rawQuery(query, arrayOf(cedula, placa))
        var existe = false
        if (cursor.moveToFirst()) {
            val total = cursor.getInt(cursor.getColumnIndexOrThrow("total"))
            existe = total > 0
        }
        cursor.close()
        db.close()
        return@withContext existe
    }
    suspend fun existeConductorVehiculoRegistrados(cedula: String, placa: String): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()
        val cursorConductor = db.rawQuery(
            "SELECT 1 FROM conductor WHERE cedula = ? LIMIT 1",
            arrayOf(cedula)
        )
        val existeConductor = cursorConductor.moveToFirst()
        cursorConductor.close()
        val cursorVehiculo = db.rawQuery(
            "SELECT 1 FROM vehiculo WHERE placa = ? LIMIT 1",
            arrayOf(placa)
        )
        val existeVehiculo = cursorVehiculo.moveToFirst()
        cursorVehiculo.close()
        db.close()
        return@withContext existeConductor && existeVehiculo
    }
    suspend fun insertarParkingEntrada(cedula: String, placa: String): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val horaEntrada = formato.format(Date())
        val cursor = db.rawQuery("SELECT MAX(id) FROM parking", null)
        var nuevoId = 1
        if (cursor.moveToFirst()) {
            val maxId = cursor.getInt(0)
            nuevoId = maxId + 1
        }
        cursor.close()
        val values = ContentValues().apply {
            put("id", nuevoId)
            put("cedula", cedula)
            put("placa", placa)
            put("hora_entrada", horaEntrada)
        }

        val resultado = db.insert("parking", null, values)
        db.close()

        return@withContext resultado != -1L
    }
}