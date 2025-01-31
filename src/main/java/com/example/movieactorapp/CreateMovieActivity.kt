package com.example.movieactorapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.movieactorapp.models.Actor
import com.example.movieactorapp.models.Movie

class CreateMovieActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper // Instancia de DatabaseHelper
    private val selectedActors = mutableListOf<Actor>() // Lista de actores seleccionados
    private lateinit var actorAdapter: ArrayAdapter<String> // Adaptador para la lista de actores seleccionados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_movie)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Referencias a las vistas
        val titleEditText: EditText = findViewById(R.id.movieTitleEditText)
        val genreEditText: EditText = findViewById(R.id.movieGenreEditText)
        val directorEditText: EditText = findViewById(R.id.movieDirectorEditText)
        val durationEditText: EditText = findViewById(R.id.movieDurationEditText)
        val releaseDateEditText: EditText = findViewById(R.id.movieReleaseDateEditText)
        val saveButton: Button = findViewById(R.id.saveMovieButton)
        val addActorsButton: Button = findViewById(R.id.addActorsButton)
        val selectedActorsListView: ListView = findViewById(R.id.selectedActorsListView)

        // Configurar el adaptador para la lista de actores seleccionados
        actorAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedActors.map { it.name })
        selectedActorsListView.adapter = actorAdapter

        // Abrir actividad para seleccionar actores
        addActorsButton.setOnClickListener {
            val intent = Intent(this, SelectActorsActivity::class.java)
            startActivityForResult(intent, SELECT_ACTORS_REQUEST)
        }

        // Acción al hacer clic en "Guardar"
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val genre = genreEditText.text.toString()
            val director = directorEditText.text.toString()
            val duration = durationEditText.text.toString().toIntOrNull() ?: 0
            val releaseDate = releaseDateEditText.text.toString()

            // Validar los campos
            if (title.isBlank() || genre.isBlank() || director.isBlank() || releaseDate.isBlank()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear un objeto Movie
            val movie = Movie(
                id = 0, // SQLite generará automáticamente el ID
                title = title,
                genre = genre,
                director = director,
                duration = duration,
                releaseDate = releaseDate
            )

            // Insertar la película en la base de datos
            val movieId = databaseHelper.addMovie(movie)
            if (movieId != -1L) {
                // Asociar actores seleccionados a la película
                selectedActors.forEach { actor ->
                    databaseHelper.linkActorToMovie(movieId, actor.id.toLong())
                }

                Toast.makeText(this, "Película guardada con éxito.", Toast.LENGTH_SHORT).show()
                finish() // Finalizar la actividad y volver
            } else {
                Toast.makeText(this, "Error al guardar la película.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    // Manejar resultado de la selección de actores
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_ACTORS_REQUEST && resultCode == RESULT_OK) {
            val actors = data?.getParcelableArrayListExtra<Actor>("selected_actors") ?: return
            Log.d("CreateMovieActivity", "Actors received: $selectedActors")
            selectedActors.clear()
            selectedActors.addAll(actors)
            actorAdapter.clear()
            actorAdapter.addAll(selectedActors.map { it.name })
            actorAdapter.notifyDataSetChanged()

        }
    }

    companion object {
        const val SELECT_ACTORS_REQUEST = 1
    }
}
