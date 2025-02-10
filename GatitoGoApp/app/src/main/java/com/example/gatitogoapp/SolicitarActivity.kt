package com.example.gatitogoapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gatitogoapp.R
import com.example.gatitogoapp.FormularioPaso1Activity
import com.example.gatitogoapp.SolicitudManager
import com.example.gatitogoapp.SolicitudAdopcion




class SolicitarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar)

        // Referencias a los elementos del layout
        val imageGatito = findViewById<ImageView>(R.id.imageGatito)
        val textNombreGatito = findViewById<TextView>(R.id.textNombreGatito)
        val textDetallesGatito = findViewById<TextView>(R.id.textDetallesGatito)
        val buttonAdoptar = findViewById<Button>(R.id.buttonAdoptar)

        // Obtener los datos enviados desde VerGatitosActivity
        val gatitoNombre = intent.getStringExtra("gatitoNombre") ?: "Gatito Desconocido"
        val gatitoImagen = intent.getIntExtra("gatitoImagen", R.drawable.ic_launcher_background)
        val gatitoDetalles = intent.getStringExtra("gatitoDetalles") ?: "Detalles no disponibles"

        // Actualizar la UI con los datos del gatito
        textNombreGatito.text = gatitoNombre
        imageGatito.setImageResource(gatitoImagen)
        textDetallesGatito.text = gatitoDetalles

        buttonAdoptar.setOnClickListener {
            // Guardar en memoria usando SolicitudManager
            val solicitud = SolicitudAdopcion(
                nombreGatito = gatitoNombre,
                nombreAdoptante = "Pendiente",
                telefono = "Pendiente",
                direccion = "Pendiente",
                detalles = "Solicitud en proceso"
            )
            SolicitudManager.agregarSolicitud(solicitud)

            Toast.makeText(this, "Solicitud guardada en memoria", Toast.LENGTH_SHORT).show()

            // Verificar que la actividad FormularioPaso1Activity exista antes de lanzar el Intent
            try {
                val intent = Intent(this, FormularioPaso1Activity::class.java)
                intent.putExtra("nombreGatito", gatitoNombre)
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                Toast.makeText(this, "Error: FormularioPaso1Activity no encontrado", Toast.LENGTH_LONG).show()
            }
        }
    }
}
