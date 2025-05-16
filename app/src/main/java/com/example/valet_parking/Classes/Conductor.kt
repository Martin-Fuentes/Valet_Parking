package com.example.valet_parking.Classes

import android.content.ContentValues
import com.example.valet_parking.DataClasses.Data_Conductor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Conductor(private val dbHelper: DatabaseHelper){
    suspend fun agregarConductor(conductor: Data_Conductor): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.openDatabase()
        val values = ContentValues().apply {
            put("nombre", conductor.nombre)
            put("cedula", conductor.cedula)
            put("contacto", conductor.contacto)
        }
        val result = db.insert("conductor", null, values)
        db.close()
        return@withContext result != -1L
    }
}