package com.example.gatitogoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormularioPaso2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_paso2)

        val editExperiencia = findViewById<EditText>(R.id.editExperiencia)
        val buttonEnviar = findViewById<Button>(R.id.buttonEnviarSolicitud)

        val cedula = intent.getStringExtra("cedula") ?: "No especificado"
        val nombre = intent.getStringExtra("nombre") ?: "No especificado"
        val email = intent.getStringExtra("email") ?: "No especificado"
        val direccion = intent.getStringExtra("direccion") ?: "No especificado"

        buttonEnviar.setOnClickListener {
            val experiencia = editExperiencia.text.toString().trim()

            if (experiencia.isEmpty()) {
                Toast.makeText(this, "Por favor describe tu experiencia con mascotas", Toast.LENGTH_SHORT).show()
            } else {
                // Crear una nueva solicitud de adopción
                val solicitud = SolicitudAdopcion(
                    nombreGatito = "Pendiente", // Puedes cambiarlo si se obtiene de antes
                    nombreAdoptante = nombre,
                    telefono = cedula,
                    direccion = direccion,
                    detalles = "Email: $email\nExperiencia: $experiencia"
                )

                // Guardar la solicitud en memoria
                SolicitudManager.agregarSolicitud(solicitud)

                Toast.makeText(this, "Solicitud guardada correctamente", Toast.LENGTH_SHORT).show()

                // Volver a la pantalla principal
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
