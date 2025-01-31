package com.example.movieactorapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.models.Actor

class SelectActorsActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private val actors = mutableListOf<Actor>()
    private val selectedActors = mutableListOf<Actor>()
    private lateinit var actorAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_actors)


        databaseHelper = DatabaseHelper(this)

        val actorsListView: ListView = findViewById(R.id.actorsListView)

        // Cargar actores desde la base de datos
        actors.addAll(databaseHelper.getAllActors())


        // Configurar el adaptador para mostrar los actores
        actorAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, actors.map { it.name })
        actorsListView.adapter = actorAdapter
        actorsListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE


        // Manejar selección de actores
        actorsListView.setOnItemClickListener { _, _, position, _ ->
            val actor = actors[position]
            if (selectedActors.contains(actor)) {
                selectedActors.remove(actor)
            } else {
                selectedActors.add(actor)
            }
        }

        // Confirmar selección de actores
        val confirmButton: Button = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra("selected_actors", ArrayList(selectedActors))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
