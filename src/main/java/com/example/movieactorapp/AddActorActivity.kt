package com.example.movieactorapp

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.models.Actor

class AddActorActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_actor)

        databaseHelper = DatabaseHelper(this) // Instancia de la BD

        val nameEditText: EditText = findViewById(R.id.actorNameEditText)
        val ageEditText: EditText = findViewById(R.id.actorAgeEditText)
        val nationalityEditText: EditText = findViewById(R.id.actorNationalityEditText)
        val oscarWinnerCheckBox: CheckBox = findViewById(R.id.actorOscarWinnerCheckBox)
        val salaryEditText: EditText = findViewById(R.id.actorSalaryEditText)
        val addButton: Button = findViewById(R.id.addActorButton)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull() ?: 0
            val nationality = nationalityEditText.text.toString()
            val isOscarWinner = oscarWinnerCheckBox.isChecked
            val salary = salaryEditText.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isBlank() || nationality.isBlank()) {
                Toast.makeText(this, "Complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newActor = Actor(
                id = 0, // SQLite generará el ID automáticamente
                name = name,
                age = age,
                nationality = nationality,
                isOscarWinner = isOscarWinner,
                salary = salary
            )

            val actorId = databaseHelper.addActor(newActor)

            if (actorId != -1L) {
                Toast.makeText(this, "Actor agregado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al agregar el actor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
