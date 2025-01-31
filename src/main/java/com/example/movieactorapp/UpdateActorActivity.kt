package com.example.movieactorapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.models.Actor

class UpdateActorActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var actorListView: ListView
    private lateinit var searchView: SearchView
    private lateinit var actorAdapter: ArrayAdapter<Actor>
    private var selectedActor: Actor? = null // Cambiado a nullable
    private val actors = mutableListOf<Actor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_actor)

        databaseHelper = DatabaseHelper(this)

        // Referencias a las vistas
        searchView = findViewById(R.id.actorSearchView)
        actorListView = findViewById(R.id.actorListView)
        val nameEditText: EditText = findViewById(R.id.actorNameEditText)
        val ageEditText: EditText = findViewById(R.id.actorAgeEditText)
        val nationalityEditText: EditText = findViewById(R.id.actorNationalityEditText)
        val oscarWinnerSwitch: Switch = findViewById(R.id.actorOscarWinnerSwitch)
        val salaryEditText: EditText = findViewById(R.id.actorSalaryEditText)
        val updateButton: Button = findViewById(R.id.updateActorButton)

        // Cargar actores desde la base de datos
        loadActorsFromDatabase()

        // Configurar el adaptador
        actorAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, actors)
        actorListView.adapter = actorAdapter

        // Buscar actores
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                actorAdapter.filter.filter(newText)
                return true
            }
        })

        // Seleccionar actor de la lista
        actorListView.setOnItemClickListener { _, _, position, _ ->
            selectedActor = actorAdapter.getItem(position)
            selectedActor?.let {
                nameEditText.setText(it.name)
                ageEditText.setText(it.age.toString())
                nationalityEditText.setText(it.nationality)
                oscarWinnerSwitch.isChecked = it.isOscarWinner
                salaryEditText.setText(it.salary.toString())
            }
        }

        // Al actualizar el actor
        updateButton.setOnClickListener {
            if (selectedActor != null) {
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_ACTOR_NAME, nameEditText.text.toString())
                    put(DatabaseHelper.COLUMN_ACTOR_AGE, ageEditText.text.toString().toIntOrNull() ?: 0)
                    put(DatabaseHelper.COLUMN_ACTOR_NATIONALITY, nationalityEditText.text.toString())
                    put(DatabaseHelper.COLUMN_ACTOR_IS_OSCAR_WINNER, if (oscarWinnerSwitch.isChecked) 1 else 0)
                    put(DatabaseHelper.COLUMN_ACTOR_SALARY, salaryEditText.text.toString().toDoubleOrNull() ?: 0.0)
                }

                databaseHelper.updateActor(selectedActor!!.id.toLong(), values)
                loadActorsFromDatabase() // Recargar la lista de actores
                actorAdapter.notifyDataSetChanged()
            }

            finish()
        }
    }

    private fun loadActorsFromDatabase() {
        actors.clear()
        actors.addAll(databaseHelper.getAllActors())
    }
}
