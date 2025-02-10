package com.example.gatitogoapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListaSolicitudesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_solicitudes)

        val textSolicitudes = findViewById<TextView>(R.id.textSolicitudes)
        val solicitudes = SolicitudManager.obtenerSolicitudes()
        val texto = StringBuilder()

        if (solicitudes.isEmpty()) {
            texto.append("No hay solicitudes registradas.")
        } else {
            for (solicitud in solicitudes) {
                texto.append("🐾 Gatito: ${solicitud.nombreGatito}\n")
                    .append("👤 Adoptante: ${solicitud.nombreAdoptante}\n")
                    .append("📞 Teléfono: ${solicitud.telefono}\n")
                    .append("📍 Dirección: ${solicitud.direccion}\n")
                    .append("📧 ${solicitud.detalles}\n")
                    .append("----------------------\n")
            }
        }

        textSolicitudes.text = texto.toString()
    }
}
