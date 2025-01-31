package com.example.movieactorapp

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.adapters.ActorAdapter
import com.example.movieactorapp.models.Actor

class ViewActorsActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var actorListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_actors)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Referencia al ListView
        actorListView = findViewById(R.id.actorListView)

        // Obtener la lista de actores desde la base de datos
        val actors: List<Actor> = databaseHelper.getAllActors()

        // Configurar el adaptador
        val actorAdapter = ActorAdapter(this, actors)
        actorListView.adapter = actorAdapter
    }
}
