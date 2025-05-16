package com.example.valet_parking.Classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class DatabaseHelper (private val context: Context): SQLiteOpenHelper(context, DB_NAME, null,
    DB_VERSION
){
    companion object{
        private const val DB_NAME = "ValetParking.db"
        private const val DB_VERSION = 1
        private const val DB_PATH = "/data/data/com.example.valet_parking/databases/"
    }
    private val dbFile: File
        get()=File(DB_PATH + DB_NAME)

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    fun copyDB(){
        val dbDir = File(DB_PATH)
        if(!dbDir.exists()) dbDir.mkdirs()

        if(!dbFile.exists()){
            context.assets.open(DB_NAME).use{ input ->
                FileOutputStream(dbFile).use{ output ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while(input.read(buffer).also{length = it}>0){
                        output.write(buffer,0,length)
                    }
                    output.flush()
                }
            }
        }
    }
    fun openDatabase():SQLiteDatabase{
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

}