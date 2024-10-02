package com.example.atu_app

data class movimiento(
    val tipo: String,
    val monto: Double,
    val descripcion: String,
    val fecha: String,
    val dniUsuario: String? = null
)