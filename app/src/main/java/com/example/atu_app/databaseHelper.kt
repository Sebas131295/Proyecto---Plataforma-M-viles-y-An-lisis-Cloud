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
    private val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

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

    fun insertarMovimiento(monto: Double, descripcion: String, fechaActual: String) {
        // Obtener el DNI del usuario desde SharedPreferences
        val dniUsuario = sharedPreferences.getString("dniUsuario", null)

        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_CANTIDAD, monto)
            put(COL_FECHA, fechaActual)
            put(COL_DESCRIPCION, descripcion)
            put(COL_DNI, dniUsuario)  // Agregar el DNI del usuario
        }
        db.insert(TABLE_HISTORIAL, null, contentValues)
        db.close()
    }

    fun insertarEstacion(nombre: String, direccion: String, distrito: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_NOMBRE_ESTACION, nombre)
        values.put(COL_DIRECCION, direccion)
        values.put(COL_DISTRITO, distrito)
        return db.insert(TABLE_ESTACIONES, null, values)
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

    fun leerMovimientosPorDNI(dni: String): Cursor {
        val db = this.readableDatabase

        // Consulta que obtiene movimientos del usuario actual y movimientos de relleno (sin DNI)
        val query = "SELECT * FROM $TABLE_HISTORIAL WHERE $COL_DNI = ? OR $COL_DNI IS NULL"

        return db.rawQuery(query, arrayOf(dni))
    }

    // Métodos para actualizar datos

    fun actualizarUsuario(dni: String, contraseña: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_CONTRASEÑA, contraseña)
        return db.update(TABLE_USUARIO, values, "$COL_DNI=?", arrayOf(dni))
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

    fun contarMovimientos(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_HISTORIAL", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0) // Obtener el recuento del primer (y único) campo
        }
        cursor.close()
        return count
    }

    fun obtenerMovimientosPorDNI(dni: String): List<String> {
        val db = this.readableDatabase
        val movimientosList = mutableListOf<String>()

        val query = "SELECT * FROM $TABLE_HISTORIAL WHERE $COL_DNI = ?"
        val cursor = db.rawQuery(query, arrayOf(dni))

        if (cursor.moveToFirst()) {
            do {
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA))
                val cantidad = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CANTIDAD))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION))

                val movimiento = "ngreso $cantidad, $descripcion, $fecha"
                movimientosList.add(movimiento)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return movimientosList
    }


    fun obtenerSumaMontosPorDNI(dni: String): Double {
        val db = this.readableDatabase
        var sumaMontos = 0.0

        val query = "SELECT SUM($COL_CANTIDAD) AS total FROM $TABLE_HISTORIAL WHERE $COL_DNI = ?"
        val cursor = db.rawQuery(query, arrayOf(dni))

        if (cursor.moveToFirst()) {
            sumaMontos = cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
        }

        cursor.close()
        db.close()

        return sumaMontos
    }


    fun obtenerUsuarioPorDni(dni: String): usuario? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuario WHERE dni = ?", arrayOf(dni))

        return if (cursor.moveToFirst()) {
            // Crear un objeto Usuario con los datos obtenidos
            val usuario = usuario(
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                correo = cursor.getString(cursor.getColumnIndexOrThrow("correo")),
                contrasena = cursor.getString(cursor.getColumnIndexOrThrow("contraseña"))
            )
            cursor.close()
            usuario
        } else {
            cursor.close()
            null
        }
    }

    fun obtenerTarjetaPorDni(dni: String): tarjeta? {
        val db = this.readableDatabase
        var tarjeta: tarjeta? = null

        // Consulta SQL para buscar la tarjeta según el DNI
        val query = "SELECT * FROM $TABLE_TARJETA WHERE $COL_DNI = ?"
        val cursor = db.rawQuery(query, arrayOf(dni))

        // Si se encuentra la tarjeta, crear un objeto Tarjeta
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TARJETA_ID))
            val numeroTarjeta = cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMERO_TARJETA))
            val monto = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MONTO))

            tarjeta = tarjeta(id, dni, numeroTarjeta, monto)
        }

        cursor.close()
        db.close()

        return tarjeta
    }

    fun existeTarjeta(dni: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TARJETA WHERE $COL_DNI = ?"
        val cursor = db.rawQuery(query, arrayOf(dni))
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }
}