package com.example.gatitogoapp

object SolicitudManager {
    private val listaSolicitudes = mutableListOf<SolicitudAdopcion>()

    fun agregarSolicitud(solicitud: SolicitudAdopcion) {
        listaSolicitudes.add(solicitud)
    }

    fun obtenerSolicitudes(): List<SolicitudAdopcion> {
        return listaSolicitudes
    }
}

data class SolicitudAdopcion(
    val nombreGatito: String,
    val nombreAdoptante: String,
    val telefono: String,
    val direccion: String,
    val detalles: String
)
