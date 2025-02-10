package com.example.gatitogoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormularioPaso1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_paso1)

        val editCedula = findViewById<EditText>(R.id.editCedula)
        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editDireccion = findViewById<EditText>(R.id.editDireccion)
        val buttonSiguiente = findViewById<Button>(R.id.buttonSiguientePaso1)

        buttonSiguiente.setOnClickListener {
            val cedula = editCedula.text.toString().trim()
            val nombre = editNombre.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val direccion = editDireccion.text.toString().trim()

            if (cedula.isEmpty() || nombre.isEmpty() || email.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Ingresa un email válido", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, FormularioPaso2Activity::class.java)
                intent.putExtra("cedula", cedula)
                intent.putExtra("nombre", nombre)
                intent.putExtra("email", email)
                intent.putExtra("direccion", direccion)
                startActivity(intent)
            }
        }
    }
}

