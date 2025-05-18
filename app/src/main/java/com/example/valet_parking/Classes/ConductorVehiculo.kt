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
        val query = "SELECT c.cedula, c.nombre, c.contacto, v.placa, v.marca, v.modelo, v.color, v.tipo, p.hora_entrada, p.hora_salida, p.costo_pagar FROM parking p INNER JOIN conductor c ON p.cedula = c.cedula INNER JOIN vehiculo v ON p.placa = v.placa WHERE p.hora_salida IS NULL"
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
                    hora_entrada = cursor.getString(cursor.getColumnIndexOrThrow("hora_entrada"))
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
    suspend fun existeConductorVehiculoRegistrados(cedula: String, placa: String): Array<String>? = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()
        val cursorConductor = db.rawQuery(
            "SELECT nombre, cedula FROM conductor WHERE cedula = ? LIMIT 1",
            arrayOf(cedula)
        )

        if (!cursorConductor.moveToFirst()) {
            cursorConductor.close()
            db.close()
            return@withContext null
        }

        val nombreConductor = cursorConductor.getString(cursorConductor.getColumnIndexOrThrow("nombre"))
        val cedulaConductor = cursorConductor.getString(cursorConductor.getColumnIndexOrThrow("cedula"))
        cursorConductor.close()


        val cursorVehiculo = db.rawQuery(
            "SELECT placa, marca, modelo, color, tipo FROM vehiculo WHERE placa = ? LIMIT 1",
            arrayOf(placa)
        )

        if (!cursorVehiculo.moveToFirst()) {
            cursorVehiculo.close()
            db.close()
            return@withContext null
        }

        val placaVehiculo = cursorVehiculo.getString(cursorVehiculo.getColumnIndexOrThrow("placa"))
        val marcaVehiculo = cursorVehiculo.getString(cursorVehiculo.getColumnIndexOrThrow("marca"))
        val modeloVehiculo = cursorVehiculo.getString(cursorVehiculo.getColumnIndexOrThrow("modelo"))
        val colorVehiculo = cursorVehiculo.getString(cursorVehiculo.getColumnIndexOrThrow("color"))
        val tipoVehiculo = cursorVehiculo.getString(cursorVehiculo.getColumnIndexOrThrow("tipo"))
        cursorVehiculo.close()

        db.close()


        return@withContext arrayOf(
            nombreConductor,
            cedulaConductor,
            placaVehiculo,
            marcaVehiculo,
            modeloVehiculo,
            colorVehiculo,
            tipoVehiculo
        )
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
    suspend fun registrarSalidaPorPlaca(placa: String): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()

        val query = "SELECT id, hora_entrada FROM parking WHERE placa = ? AND hora_salida IS NULL ORDER BY hora_entrada DESC LIMIT 1"

        val cursor = db.rawQuery(query, arrayOf(placa))
        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return@withContext false
        }

        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        val horaEntradaStr = cursor.getString(cursor.getColumnIndexOrThrow("hora_entrada"))
        cursor.close()

        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val horaEntrada: Date = formato.parse(horaEntradaStr) ?: return@withContext false
        val horaSalida = Date()

        val diferenciaMs = horaSalida.time - horaEntrada.time
        val minutos = diferenciaMs / (1000 * 60)
        val tarifa = minutos / 60.0

        val values = ContentValues().apply {
            put("hora_salida", formato.format(horaSalida))
            put("costo_pagar", String.format(Locale.US, "%.2f", tarifa))
        }

        val filasActualizadas = db.update("parking", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return@withContext filasActualizadas > 0
    }
    suspend fun obtenerDuracionYSaldoPorPlaca(placa: String): Array<Any>? = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()

        val query = "SELECT hora_entrada, hora_salida, costo_pagar FROM parking WHERE placa = ? AND hora_salida IS NOT NULL ORDER BY hora_salida DESC LIMIT 1"
        val cursor = db.rawQuery(query, arrayOf(placa))

        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return@withContext null
        }

        val horaEntradaStr = cursor.getString(cursor.getColumnIndexOrThrow("hora_entrada"))
        val horaSalidaStr = cursor.getString(cursor.getColumnIndexOrThrow("hora_salida"))
        val costo = cursor.getString(cursor.getColumnIndexOrThrow("costo_pagar")).toDoubleOrNull() ?: 0.0

        cursor.close()
        db.close()

        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val horaEntrada = formato.parse(horaEntradaStr)
        val horaSalida = formato.parse(horaSalidaStr)

        if (horaEntrada == null || horaSalida == null) return@withContext null

        val diferenciaMs = horaSalida.time - horaEntrada.time
        val totalMinutos = diferenciaMs / (1000 * 60)
        val horas = (totalMinutos / 60).toInt()
        val minutos = (totalMinutos % 60).toInt()

        return@withContext arrayOf(horas, minutos, costo)
    }
}