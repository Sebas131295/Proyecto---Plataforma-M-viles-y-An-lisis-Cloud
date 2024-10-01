package com.example.atu_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Definir nombres de tablas y columnas
const val DATABASE_NAME = "miApp.db"
const val DATABASE_VERSION = 1

const val TABLE_USUARIO = "usuario"
const val COL_DNI = "dni"
const val COL_NOMBRE = "nombre"
const val COL_APELLIDO = "apellido"
const val COL_TELEFONO = "telefono"
const val COL_CORREO = "correo"
const val COL_CONTRASEÑA = "contraseña"

const val TABLE_LOGIN = "login"
const val COL_LOGIN_ID = "id"
const val COL_NOMBRE_USUARIO = "nombre_usuario"

const val TABLE_TARJETA = "tarjeta"
const val COL_TARJETA_ID = "id"
const val COL_NUMERO_TARJETA = "numero_tarjeta"
const val COL_MONTO = "monto"

const val TABLE_HISTORIAL = "historial"
const val COL_HISTORIAL_ID = "id"
const val COL_FECHA = "fecha"
const val COL_CANTIDAD = "cantidad"
const val COL_DESCRIPCION = "descripcion"

const val TABLE_ESTACIONES = "estaciones"
const val COL_ESTACION_ID = "id"
const val COL_NOMBRE_ESTACION = "nombre"
const val COL_DIRECCION = "horario_cierre"
const val COL_DISTRITO = "distrito"

class databaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla usuario
        db?.execSQL(
            "CREATE TABLE $TABLE_USUARIO (" +
                    "$COL_DNI TEXT PRIMARY KEY," +
                    "$COL_NOMBRE TEXT," +
                    "$COL_APELLIDO TEXT," +
                    "$COL_TELEFONO TEXT," +
                    "$COL_CORREO TEXT," +
                    "$COL_CONTRASEÑA TEXT)"
        )

        // Crear tabla login
        db?.execSQL(
            "CREATE TABLE $TABLE_LOGIN (" +
                    "$COL_LOGIN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COL_DNI TEXT," +
                    "$COL_NOMBRE_USUARIO TEXT," +
                    "$COL_CONTRASEÑA TEXT," +
                    "FOREIGN KEY($COL_DNI) REFERENCES $TABLE_USUARIO($COL_DNI))"
        )

        // Crear tabla tarjeta
        db?.execSQL(
            "CREATE TABLE $TABLE_TARJETA (" +
                    "$COL_TARJETA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COL_DNI TEXT," +
                    "$COL_NUMERO_TARJETA TEXT," +
                    "$COL_MONTO REAL," +
                    "FOREIGN KEY($COL_DNI) REFERENCES $TABLE_USUARIO($COL_DNI))"
        )

        // Crear tabla historial
        db?.execSQL(
            "CREATE TABLE $TABLE_HISTORIAL (" +
                    "$COL_HISTORIAL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COL_DNI TEXT," +
                    "$COL_FECHA TEXT," +
                    "$COL_CANTIDAD REAL," +
                    "$COL_DESCRIPCION TEXT," +
                    "FOREIGN KEY($COL_DNI) REFERENCES $TABLE_USUARIO($COL_DNI))"
        )

        // Crear tabla estaciones
        db?.execSQL(
            "CREATE TABLE $TABLE_ESTACIONES (" +
                    "$COL_ESTACION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COL_NOMBRE_ESTACION TEXT," +
                    "$COL_DIRECCION TEXT," +
                    "$COL_DISTRITO TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Eliminar tablas si existen y volver a crear
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOGIN")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TARJETA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIAL")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ESTACIONES")
        onCreate(db)
    }

    // Crear métodos para insertar datos

    fun insertarUsuario(dni: String, nombre: String, apellido: String, telefono: String, correo: String, contraseña: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_DNI, dni)
        values.put(COL_NOMBRE, nombre)
        values.put(COL_APELLIDO, apellido)
        values.put(COL_TELEFONO, telefono)
        values.put(COL_CORREO, correo)
        values.put(COL_CONTRASEÑA, contraseña)
        return db.insert(TABLE_USUARIO, null, values)
    }

    fun insertarLogin(dni: String, nombreUsuario: String, contraseña: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_DNI, dni)
        values.put(COL_NOMBRE_USUARIO, nombreUsuario)
        values.put(COL_CONTRASEÑA, contraseña)
        return db.insert(TABLE_LOGIN, null, values)
    }

    fun insertarTarjeta(dni: String, numeroTarjeta: String, monto: Double): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_DNI, dni)
        values.put(COL_NUMERO_TARJETA, numeroTarjeta)
        values.put(COL_MONTO, monto)
        return db.insert(TABLE_TARJETA, null, values)
    }

    fun insertarMovimiento(monto: Double, descripcion: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("monto", monto)
            put("descripcion", descripcion)
            // Otros campos como fecha, etc.
        }
        db.insert("historial", null, contentValues)
        db.close()
    }

    fun insertarEstacion(nombre: String, distrito: String, horarioCierre: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_NOMBRE_ESTACION, nombre)
        values.put(COL_DIRECCION, horarioCierre)
        values.put(COL_DISTRITO, distrito)
        return db.insert(TABLE_ESTACIONES, null, values)
    }

    // Métodos para leer datos

    fun leerUsuarios(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_USUARIO", null)
    }

    fun leerLogin(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_LOGIN", null)
    }

    fun leerTarjetas(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_TARJETA", null)
    }

    @SuppressLint("Range")
    fun leerHistorial(): MutableList<String> {
        val historial = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM historial ORDER BY fecha DESC LIMIT 5", null)

        if (cursor.moveToFirst()) {
            do {
                val descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))
                historial.add(descripcion)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return historial
    }

    @SuppressLint("Range")
    fun leerEstaciones(): List<estacion> {
        val estaciones = mutableListOf<estacion>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ESTACIONES, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_ESTACION_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE_ESTACION))
                val direccion = cursor.getString(cursor.getColumnIndex(COL_DIRECCION))
                val distrito = cursor.getString(cursor.getColumnIndex(COL_DISTRITO))
                estaciones.add(estacion(id, nombre, direccion, distrito))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return estaciones
    }

    // Métodos para actualizar datos

    fun actualizarUsuario(dni: String, nombre: String, apellido: String, telefono: String, correo: String, contraseña: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_NOMBRE, nombre)
        values.put(COL_APELLIDO, apellido)
        values.put(COL_TELEFONO, telefono)
        values.put(COL_CORREO, correo)
        values.put(COL_CONTRASEÑA, contraseña)
        return db.update(TABLE_USUARIO, values, "$COL_DNI=?", arrayOf(dni))
    }

    fun actualizarTarjeta(dni: String, numeroTarjeta: String, monto: Double): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_NUMERO_TARJETA, numeroTarjeta)
        values.put(COL_MONTO, monto)
        return db.update(TABLE_TARJETA, values, "$COL_DNI=?", arrayOf(dni))
    }

    fun actualizarLogin(dni: String, nombreUsuario: String, contraseña: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_NOMBRE_USUARIO, nombreUsuario)
        values.put(COL_CONTRASEÑA, contraseña)
        return db.update(TABLE_LOGIN, values, "$COL_DNI=?", arrayOf(dni))
    }

    fun actualizarHistorial(dni: String, fecha: String, cantidad: Double, descripcion: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_FECHA, fecha)
        values.put(COL_CANTIDAD, cantidad)
        values.put(COL_DESCRIPCION, descripcion)
        return db.update(TABLE_HISTORIAL, values, "$COL_DNI=?", arrayOf(dni))
    }

    @SuppressLint("Range")
    fun buscarEstacionesPorNombre(nombre: String): List<estacion> {
        val estaciones = mutableListOf<estacion>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ESTACIONES, null, "$COL_NOMBRE LIKE ?", arrayOf("%$nombre%"), null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COL_ESTACION_ID))
                val nombreEstacion = cursor.getString(cursor.getColumnIndex(COL_NOMBRE))
                val direccion = cursor.getString(cursor.getColumnIndex(COL_DIRECCION))
                val distrito = cursor.getString(cursor.getColumnIndex(COL_DISTRITO))
                estaciones.add(estacion(id, nombreEstacion, direccion, distrito))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return estaciones
    }

    fun obtenerMontoActual(): Double {
        var montoActual = 0.0
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(cantidad) FROM historial", null)

        if (cursor.moveToFirst()) {
            montoActual = cursor.getDouble(0) // Sumar todas las cantidades
        }
        cursor.close()
        return montoActual
    }
}